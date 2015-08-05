package org.simbi.simbiapp.api.retrofit;

import android.content.Context;

import com.squareup.otto.Bus;

import org.json.JSONException;
import org.json.JSONObject;
import org.simbi.simbiapp.api.interfaces.DoctorsClient;
import org.simbi.simbiapp.api.models.Response.Doctor;
import org.simbi.simbiapp.events.doctors.DoctorProfileEvent;
import org.simbi.simbiapp.events.doctors.DoctorProfileFailedEvent;
import org.simbi.simbiapp.events.doctors.DoctorsListEvent;
import org.simbi.simbiapp.events.doctors.DoctorsListFailedEvent;
import org.simbi.simbiapp.utils.SimbiConstants;
import org.simbi.simbiapp.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

public class RetrofitDoctorsClient implements DoctorsClient {

    private static RetrofitDoctorsClient mDoctorClient;
    private final Bus mBus;
    private DoctorsApi doctorsApi;

    private RetrofitDoctorsClient(Context ctx) {
        doctorsApi = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(SimbiConstants.baseApiUrl)
                .build()
                .create(DoctorsApi.class);
        mBus = new Bus();
    }

    public static RetrofitDoctorsClient getClient(final Context context) {
        if (mDoctorClient == null) {
            mDoctorClient = new RetrofitDoctorsClient(context);
        }
        return mDoctorClient;
    }

    @Override
    public void getDoctors(final String token) {

        if (token != null && token.length() > 0) {

            doctorsApi.getAllDoctors("Token " + token, new Callback<List<Doctor>>() {

                @Override
                public void success(List<Doctor> doctors, Response response) {
                    if (doctors != null && doctors.size() > 0) {
                        getBus().post(new DoctorsListEvent(doctors));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Utils.handleRetrofitErrorQuietly(error);
                    getBus().post(new DoctorsListFailedEvent());
                }
            });
        }

    }

    @Override
    public void getDoctorsById(String id, String token) {

        TypedInput inputJson = null; //we are posting input json as raw data
        JSONObject postData = new JSONObject();

        try {
            postData.put("id", id);
            inputJson = new TypedByteArray("application/json",
                    postData.toString().getBytes("UTF-8"));

        } catch (JSONException j) {
            j.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (token != null && token.length() > 0) {
            doctorsApi.getDoctorById(inputJson, "Token " + token, new Callback<Doctor>() {

                @Override
                public void success(Doctor doctor, Response response) {
                    if (doctor != null) {
                        getBus().post(new DoctorProfileEvent(doctor));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Utils.handleRetrofitErrorQuietly(error);
                    getBus().post(new DoctorProfileFailedEvent());
                }
            });
        }
    }

    @Override
    public Bus getBus() {
        return mBus;
    }

    public interface DoctorsApi {

        String DOCTORS = "/doctors/";

        @GET(DOCTORS)
        void getAllDoctors(@Header("Authorization") String authorization,
                           Callback<List<Doctor>> callback);

        @POST(DOCTORS)
        void getDoctorById(@Body TypedInput doctorId,
                           @Header("Authorization") String authorization,
                           Callback<Doctor> callback);
    }
}
