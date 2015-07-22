package org.simbi.simbiapp;

import android.app.Application;

import com.squareup.okhttp.OkHttpClient;

import org.simbi.simbiapp.utils.SimbiApi;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class SimbiApp extends Application {

    private SimbiApi apiService;

    @Override
    public void onCreate() {
        super.onCreate();

        //build a rest adapter for accessing the web service through retrofit
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(new OkHttpClient()))
                .setEndpoint(SimbiApi.baseApiUrl)
                .build();

        apiService = restAdapter.create(SimbiApi.class);
    }

    public SimbiApi getSimbiApiService() {
        return apiService;
    }
}
