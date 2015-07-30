package org.simbi.simbiapp.api.retrofit;

import com.google.gson.Gson;
import com.squareup.otto.Bus;

import org.json.JSONException;
import org.json.JSONObject;
import org.simbi.simbiapp.api.interafaces.UserClient;
import org.simbi.simbiapp.events.user.UserLoginEvent;
import org.simbi.simbiapp.events.user.UserLoginFailedEvent;
import org.simbi.simbiapp.utils.SimbiConstants;
import org.simbi.simbiapp.utils.Utils;

import java.io.UnsupportedEncodingException;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

public class RetrofitUserClient implements UserClient {

    private static RetrofitUserClient mUserClient;
    private final Bus mBus;
    private UserApi userApi;

    private RetrofitUserClient() {
        userApi = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(SimbiConstants.baseApiUrl)
                .build()
                .create(UserApi.class);
        mBus = new Bus();
    }

    public static RetrofitUserClient getClient() {
        if (mUserClient == null) {
            mUserClient = new RetrofitUserClient();
        }
        return mUserClient;
    }

    @Override
    public void login(final String user, final String pass) {

        JSONObject credentials = new JSONObject();
        TypedInput inputJson = null; //we are posting input json as raw data

        try {
            credentials.put("username", user);
            credentials.put("password", pass);

            inputJson = new TypedByteArray("application/json",
                    credentials.toString().getBytes("UTF-8"));

        } catch (JSONException j) {
            j.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        userApi.loginToSimbi(inputJson, new Callback<Object>() {
            @Override
            public void success(Object object, retrofit.client.Response response) {

                String result = new Gson().toJson(object); //this is the response json

                JSONObject jsonResult = null;
                try {

                    jsonResult = new JSONObject(result);
                    String status = jsonResult.getString("status");

                    if (status.equals(UserApi.STATUS_OK)) {
                        String token = jsonResult.getString("token");

                        if (token != null && token.length() > 0) {
                            getBus().post(new UserLoginEvent(user, token));
                        }
                    } else {
                        getBus().post(new UserLoginFailedEvent());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.handleRetrofitErrorQuietly(error);
                getBus().post(new UserLoginFailedEvent());
            }
        });

    }

    @Override
    public Bus getBus() {
        return mBus;
    }

    public interface UserApi {

        //API Endpoints
        String API_TOKEN_AUTH = "/api-token-auth/";
        String AUTH_TEST = "/auth-test/";
        String STATUS_OK = "1";

        @POST(API_TOKEN_AUTH)
        void loginToSimbi(@Body TypedInput credentials,//input raw data in form of json(user/pass)
                          Callback<Object> callback);

        @GET(AUTH_TEST)
        void canAuthorizeToServer(@Header("Authorization") String authorization,
                                  Callback<Object> callback);

    }

}
