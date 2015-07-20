package org.simbi.simbiapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rahul on 16/7/15.
 */
public class SimbiApi implements Constants {

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    //API Endpoints
    public static String baseApiUrl = "http://162.216.17.207:80";
    private static SimbiApi simbiApi = null;
    private static String API_TOKEN_AUTH = baseApiUrl + "/api-token-auth/";
    private static String AUTH_TEST = baseApiUrl + "/auth-test/";
    private static String DOCTORS = baseApiUrl + "/doctors/";
    private static String PETS = baseApiUrl + "/pets/";
    private static String QUESTIONS = baseApiUrl + "/question/";

    private static String STATUS_OK = "1";

    private Context context;
    private SharedPreferences prefs;
    private OkHttpClient client;


    private Gson gson = new Gson();

    private SimbiApi(Context ctx) {
        this.context = ctx;
        client = new OkHttpClient();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SimbiApi getInstance(Context ctx) {
        if (simbiApi == null)
            simbiApi = new SimbiApi(ctx);
        return simbiApi;
    }

    /**
     * Function to login into Simbi
     * returns true if login is successful
     *
     * @param user - Username
     * @param pass - Password
     */
    public boolean doLogin(String user, String pass) {
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

            JSONObject jsonResult = new JSONObject(result);
            String status = jsonResult.getString("status");

            if (status.equals(STATUS_OK)) {
                String token = jsonResult.getString("token");

                if (token != null && token.length() > 0) {
                    //creating login session
                    SessionManagement sessionManagement = new SessionManagement(context);
                    sessionManagement.createLoginSession(user, token);
                    //login success
                    return true;
                }
            }
        } catch (JSONException j) {
            j.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        //Login Fail wrong username password
        return false;
    }

    /**
     * Function to check whether you are authenticated
     * to the server
     */
    public boolean canAuthenticateToServer() {
        try {
            Request request = buildGetRequestWithAuthToken(AUTH_TEST);
            Response response = client.newCall(request).execute();
            String str = response.body().string();

            if (new JSONObject(str).getString("detail").equals("I suppose you are authenticated")) {
                //success
                return true;
            }
        } catch (IOException ioe) {
        } catch (JSONException j) {
        }
        return false;
    }

    /**
     * Function retrieve all Doctors
     */
    public List<Doctor> getAllDoctors() {
        Doctor[] doctors = null;
        try {
            Request request = buildGetRequestWithAuthToken(DOCTORS);
            Response response = client.newCall(request).execute();
            String str = response.body().string();

            doctors = gson.fromJson(str, Doctor[].class);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return Arrays.asList(doctors);
    }

    /**
     * Function to retrieve a Doctor object by id
     *
     * @param id - id of the doctor to be retrieved
     */
    public Doctor getDoctorById(String id) {
        Doctor doctor = null;
        try {
            JSONObject postData = new JSONObject();
            postData.put("id", id);
            Request request = buildPostRequestWithAuthToken(DOCTORS, postData.toString());

            Response response = client.newCall(request).execute();
            String result = response.body().string();

            doctor = gson.fromJson(result, Doctor.class);
        } catch (IOException io) {
            io.printStackTrace();
        } catch (JSONException j) {
            j.printStackTrace();
        }
        return doctor;
    }

    /**
     * Function to retrieve all Pets
     */
    public List<Pet> getAllPets() {
        Pet[] pets = null;
        try {
            Request request = buildGetRequestWithAuthToken(PETS);
            Response response = client.newCall(request).execute();
            String result = response.body().string();

            pets = gson.fromJson(result, Pet[].class);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return Arrays.asList(pets);
    }

    /**
     * Function to build a GET Request object
     * with authentication token in its header
     *
     * @param requestEndpoint - api endpoint to which request is to be made
     */
    public Request buildGetRequestWithAuthToken(String requestEndpoint) {

        String token = prefs.getString(SessionManagement.KEY_AUTH_TOKEN, "");
        Request request = null;

        if (token != null && token.length() != 0) {
            //adding authentication token received during login to the header
            request = new Request.Builder()
                    .url(requestEndpoint)
                    .addHeader("Authorization", "Token " + token)
                    .build();
        }
        return request;
    }

    /**
     * Function to build a POST Request object
     * with authentication token in its header
     *
     * @param requestEndpoint - api endpoint to which request is to be made
     * @param postData        - data to be posted on the endpoint
     */
    public Request buildPostRequestWithAuthToken(String requestEndpoint, String postData) {
        String token = prefs.getString(SessionManagement.KEY_AUTH_TOKEN, "");
        Request request = null;

        if (token != null && token.length() != 0) {
            //adding authentication token received during login to the header
            RequestBody body = RequestBody.create(JSON, postData);
            request = new Request.Builder()
                    .url(requestEndpoint)
                    .post(body)
                    .addHeader("Authorization", "Token " + token)
                    .build();
        }
        return request;
    }
}