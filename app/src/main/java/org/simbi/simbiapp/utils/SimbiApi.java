package org.simbi.simbiapp.utils;

import org.simbi.simbiapp.models.Doctor;
import org.simbi.simbiapp.models.Pet;
import org.simbi.simbiapp.models.Question;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.mime.TypedInput;


public interface SimbiApi {

    //API Endpoints
    String baseApiUrl = "http://162.216.17.207:80";
    String API_TOKEN_AUTH = "/api-token-auth/";
    String AUTH_TEST = "/auth-test/";
    String DOCTORS = "/doctors/";
    String PETS = "/pets/";
    String QUESTIONS = "/question/";

    String STATUS_OK = "1";

    @POST(API_TOKEN_AUTH)
    void loginToSimbi(@Body TypedInput credentials,//input raw data in form of json(user/pass)
                      Callback<Object> callback);

    @GET(AUTH_TEST)
    void canAuthorizeToServer(@Header("Authorization") String authorization,
                              Callback<Object> callback);

    @GET(DOCTORS)
    void getAllDoctors(@Header("Authorization") String authorization,
                       Callback<List<Doctor>> callback);

    @POST(DOCTORS)
    void getDoctorById(@Body TypedInput doctorId,
                       @Header("Authorization") String authorization,
                       Callback<Doctor> callback);

    @GET(PETS)
    void getAllPets(@Header("Authorization") String authorization,
                    Callback<List<Pet>> callback);

    @GET(QUESTIONS)
    void getAllQuestions(@Header("Authorization") String authorization,
                         Callback<List<Question>> callback);

}
