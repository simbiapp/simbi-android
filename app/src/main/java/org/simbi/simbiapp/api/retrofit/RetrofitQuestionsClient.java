package org.simbi.simbiapp.api.retrofit;

import android.content.Context;

import com.squareup.otto.Bus;

import org.simbi.simbiapp.api.interfaces.QuestionsClient;
import org.simbi.simbiapp.api.models.Response.Question;
import org.simbi.simbiapp.events.Questions.QuestionListEvent;
import org.simbi.simbiapp.events.Questions.QuestionsListFailedEvent;
import org.simbi.simbiapp.utils.SimbiConstants;
import org.simbi.simbiapp.utils.Utils;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Header;

public class RetrofitQuestionsClient implements QuestionsClient {

    private static RetrofitQuestionsClient mQuestionsClient;
    private final Bus mBus;
    private QuestionApi questionApi;

    private RetrofitQuestionsClient(Context ctx) {
        questionApi = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(SimbiConstants.baseApiUrl)
                .build()
                .create(QuestionApi.class);
        mBus = new Bus();
    }

    public static RetrofitQuestionsClient getClient(final Context context) {
        if (mQuestionsClient == null) {
            mQuestionsClient = new RetrofitQuestionsClient(context);
        }
        return mQuestionsClient;
    }

    @Override
    public void getAllQuestions(String token) {

        if (token != null && token.length() > 0) {

            questionApi.getAllQuestions("Token " + token, new Callback<List<Question>>() {

                @Override
                public void success(List<Question> questions, Response response) {

                    if (questions != null && questions.size() > 0) {
                        getBus().post(new QuestionListEvent(questions));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Utils.handleRetrofitErrorQuietly(error);
                    getBus().post(new QuestionsListFailedEvent());
                }
            });
        }
    }

    @Override
    public Bus getBus() {
        return mBus;
    }

    public interface QuestionApi {

        String QUESTIONS = "/question/";

        @GET(QUESTIONS)
        void getAllQuestions(@Header("Authorization") String authorization,
                             Callback<List<Question>> callback);
    }
}
