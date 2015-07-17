package org.simbi.simbiapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by rahul on 16/7/15.
 */
public class SimbiApi implements Constants {

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private static SimbiApi simbiApi = null;
    private static String baseApiUrl = "http://162.216.17.207:80";
    private String CANNOT_AUTH_TO_SERVER = "Cannot Authenticate To Server";
    private Context context;

    private SharedPreferences prefs;

    private OkHttpClient client;

    private SimbiApi(Context ctx) {
        this.context = ctx;
        client = new OkHttpClient();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit();
    }

    public static SimbiApi getInstance(Context ctx) {
        if (simbiApi == null)
            simbiApi = new SimbiApi(ctx);
        return simbiApi;
    }

    public void doLogin(String user, String pass) throws IOException, JSONException {
        JSONObject credentials = new JSONObject();
        credentials.put("username", user);
        credentials.put("password", pass);

        RequestBody body = RequestBody.create(JSON, credentials.toString());
        Request request = new Request.Builder()
                .url(baseApiUrl + "/api-token-auth/")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        String result = response.body().string();

        Log.d("Token", " " + result);
        String token = new JSONObject(result).getString("token");

        SessionManagement sessionManagement = new SessionManagement(context);
        sessionManagement.createLoginSession(user, token);
    }

    public boolean canAuthenticateToServer() throws IOException, JSONException {
        String token = prefs.getString(SessionManagement.KEY_AUTH_TOKEN, "");

        if (token != null && token.length() != 0) {

            Request request = new Request.Builder()
                    .url(baseApiUrl + "/auth-test/")
                    .addHeader("Authorization", "Token " + token)
                    .build();
            Response response = client.newCall(request).execute();
            String str = response.body().string();

            if (new JSONObject(str).getString("detail").equals("I suppose you are authenticated")) {
                return true;
            }
        }
        return false;
    }
}