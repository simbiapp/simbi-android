package org.simbi.simbiapp.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import org.simbi.simbiapp.R;
import org.simbi.simbiapp.api.interfaces.QuestionsClient;
import org.simbi.simbiapp.api.retrofit.RetrofitQuestionsClient;
import org.simbi.simbiapp.events.Questions.QuestionListEvent;
import org.simbi.simbiapp.events.Questions.QuestionsListFailedEvent;
import org.simbi.simbiapp.ui.adapters.QuestionsAdapter;
import org.simbi.simbiapp.utils.AlertDialogManager;
import org.simbi.simbiapp.utils.SessionManagement;
import org.simbi.simbiapp.utils.Utils;

public class QuestionsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private AlertDialogManager alert = new AlertDialogManager();
    private SharedPreferences prefs;
    private ProgressBar progressBar;

    private QuestionsClient questionsClient;

    private Context context;
    private View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        questionsClient = RetrofitQuestionsClient.getClient(context);
        progressBar = (ProgressBar) getActivity().findViewById(R.id.progress_bar);
    }

    @Override
    public void onResume() {
        super.onResume();

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Forum");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.questions_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mView = getView();

        swipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.refresh_questions_list);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.questions_list_recycler_view);

        swipeRefreshLayout.setColorSchemeColors(R.color.color_primary, R.color.color_primary_dark);
        swipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setVisibility(View.GONE);

        refreshQuestions();
        progressBar.setVisibility(View.VISIBLE);//show progress bar
    }


    @Override
    public void onRefresh() {
        refreshQuestions();
    }

    private void refreshQuestions() {

        if (Utils.hasInternetConnectivity(context)) {

            questionsClient.getBus().register(new QuestionsListener());

            String token = prefs.getString(SessionManagement.KEY_AUTH_TOKEN, "");
            questionsClient.getAllQuestions(token);

        } else {
            alert.showAlertDialog(context, getString(
                            R.string.message_login_fail),
                    getString(R.string.message_internet_disconnected), true);
        }
    }

    public ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    private class QuestionsListener {

        @Subscribe
        public void onQuestionsReceived(QuestionListEvent event) {

            questionsClient.getBus().unregister(this);

            swipeRefreshLayout.setRefreshing(false);
            mRecyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            QuestionsAdapter adapter = new QuestionsAdapter(context,
                    event.getQuestions());
            mRecyclerView.setAdapter(adapter);
        }

        @Subscribe
        public void onQuestionReceiveFailed(QuestionsListFailedEvent event) {

            questionsClient.getBus().unregister(this);
            Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT)
                    .show();
            //TODO handle error in a better way
        }
    }

}
