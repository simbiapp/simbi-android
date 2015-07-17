package org.simbi.simbiapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rahul on 16/7/15.
 */
public class SimbiApi implements Constants {

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private static SimbiApi simbiApi = null;

    //API Endpoints
    private static String baseApiUrl = "http://162.216.17.207:80";
    private static String API_TOKEN_AUTH = baseApiUrl + "/api-token-auth/";
    private static String AUTH_TEST = baseApiUrl + "/auth-test/";
    private static String DOCTORS = baseApiUrl + "/doctors/";
    private static String PETS = baseApiUrl + "/pets/";
    private static String QUESTIONS = baseApiUrl + "/question/";

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

    public void doLogin(String user, String pass) {

        //posting a json object with username and password
        //to get authentication token

        try {
            JSONObject credentials = new JSONObject();
            credentials.put("username", user);
            credentials.put("password", pass);

            RequestBody body = RequestBody.create(JSON, credentials.toString());
            Request request = new Request.Builder()
                    .url(API_TOKEN_AUTH)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            String result = response.body().string();

            Log.d("Token", " " + result);
            String token = new JSONObject(result).getString("token");

            //creating login session
            SessionManagement sessionManagement = new SessionManagement(context);
            sessionManagement.createLoginSession(user, token);
        } catch (JSONException j) {
        } catch (IOException ioe) {
        }
    }

    public boolean canAuthenticateToServer() {
        String token = prefs.getString(SessionManagement.KEY_AUTH_TOKEN, "");

        if (token != null && token.length() != 0) {
            //adding authentication token received during login to the header
            Request request = new Request.Builder()
                    .url(baseApiUrl + "/auth-test/")
                    .addHeader("Authorization", "Token " + token)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String str = response.body().string();

                if (new JSONObject(str).getString("detail").equals("I suppose you are authenticated")) {
                    //success
                    return true;
                }
            } catch (IOException ioe) {
            } catch (JSONException j) {
            }
        }
        return false;
    }

    public List<Doctor> getAllDoctors() {
        Doctor[] doctors=null;
        Request request;

        String token = prefs.getString(SessionManagement.KEY_AUTH_TOKEN, "");

        if (token != null && token.length() != 0) {
            //adding authentication token received during login to the header
            request = new Request.Builder()
                    .url(DOCTORS)
                    .addHeader("Authorization", "Token " + token)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String str = response.body().string();

                Gson gson = new Gson();
                doctors = gson.fromJson(str, Doctor[].class);

                Log.d("666",  doctors[0].getId()+ " "+doctors.length);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return Arrays.asList(doctors);
    }
}