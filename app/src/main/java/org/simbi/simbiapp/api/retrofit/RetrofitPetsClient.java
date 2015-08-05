package org.simbi.simbiapp.api.retrofit;

import com.squareup.otto.Bus;

import org.simbi.simbiapp.api.interfaces.PetsClient;
import org.simbi.simbiapp.api.models.Response.Pet;
import org.simbi.simbiapp.utils.SimbiConstants;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Header;

public class RetrofitPetsClient implements PetsClient {

    private static RetrofitPetsClient mPetsClient;
    private final Bus mBus;
    private PetsApi petsApi;

    private RetrofitPetsClient() {
        petsApi = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(SimbiConstants.baseApiUrl)
                .build()
                .create(PetsApi.class);
        mBus = new Bus();
    }

    public static PetsClient getClient() {
        if (mPetsClient == null) {
            mPetsClient = new RetrofitPetsClient();
        }
        return mPetsClient;
    }

    @Override
    public List<Pet> getPets() {
        return null;
    }

    @Override
    public Bus getBus() {
        return mBus;
    }

    public interface PetsApi {

        String PETS = "/pets/";

        @GET(PETS)
        void getAllPets(@Header("Authorization") String authorization,
                        Callback<List<Pet>> callback);
    }
}
