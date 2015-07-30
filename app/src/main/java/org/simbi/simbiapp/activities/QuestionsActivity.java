package org.simbi.simbiapp.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import org.simbi.simbiapp.R;
import org.simbi.simbiapp.adapters.QuestionsAdapter;
import org.simbi.simbiapp.api.interafaces.QuestionsClient;
import org.simbi.simbiapp.api.retrofit.RetrofitQuestionsClient;
import org.simbi.simbiapp.events.Questions.QuestionListEvent;
import org.simbi.simbiapp.events.Questions.QuestionsListFailedEvent;
import org.simbi.simbiapp.utils.AlertDialogManager;
import org.simbi.simbiapp.utils.SessionManagement;
import org.simbi.simbiapp.utils.Utils;

public class QuestionsActivity extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private Toolbar toolBar;

    private AlertDialogManager alert = new AlertDialogManager();
    private ProgressDialog dialog;
    private SharedPreferences prefs;

    private QuestionsClient questionsClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        toolBar = (Toolbar) findViewById(R.id.toolbar_questions_list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_questions_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.questions_list_recycler_view);

        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        questionsClient = RetrofitQuestionsClient.getClient(getBaseContext());

        swipeRefreshLayout.setColorSchemeColors(R.color.color_primary, R.color.color_primary_dark);
        swipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(QuestionsActivity.this));

        refreshQuestions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_questions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        refreshQuestions();
    }

    private void refreshQuestions() {

        if (Utils.hasInternetConnectivity(getBaseContext())) {

            dialog = new ProgressDialog(QuestionsActivity.this);
            dialog.setMessage("Please Wait");
            dialog.setIndeterminate(true);
            dialog.show();

            questionsClient.getBus().register(new QuestionsListener());

            String token = prefs.getString(SessionManagement.KEY_AUTH_TOKEN, "");
            questionsClient.getAllQuestions(token);

        } else {
            alert.showAlertDialog(QuestionsActivity.this, getString(
                            R.string.message_login_fail),
                    getString(R.string.message_internet_disconnected), true);
        }
    }

    private class QuestionsListener {

        @Subscribe
        public void onQuestionsReceived(QuestionListEvent event) {

            questionsClient.getBus().unregister(this);

            dialog.dismiss();
            swipeRefreshLayout.setRefreshing(false);
            QuestionsAdapter adapter = new QuestionsAdapter(getBaseContext(),
                    event.getQuestions());
            mRecyclerView.setAdapter(adapter);

        }

        @Subscribe
        public void onQuestionReceiveFailed(QuestionsListFailedEvent event) {
            dialog.dismiss();

            questionsClient.getBus().unregister(this);
            Toast.makeText(getBaseContext(), "Something Went Wrong", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
