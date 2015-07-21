package org.simbi.simbiapp.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.simbi.simbiapp.R;
import org.simbi.simbiapp.adapters.QuestionsAdapter;
import org.simbi.simbiapp.utils.AlertDialogManager;
import org.simbi.simbiapp.utils.MiscUtils;
import org.simbi.simbiapp.utils.Question;
import org.simbi.simbiapp.utils.SimbiApi;

import java.util.List;

public class QuestionsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private Toolbar toolBar;

    private AlertDialogManager alert = new AlertDialogManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        toolBar = (Toolbar) findViewById(R.id.toolbar_questions_list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_questions_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.questions_list_recycler_view);

        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    public void refreshQuestions() {

        if (MiscUtils.hasInternetConnectivity(getBaseContext())) {
            //populate recycler view with questions asynchronously
            new getAllQuestionsTask().execute();
        } else {
            alert.showAlertDialog(QuestionsActivity.this, getString(
                            R.string.message_login_fail),
                    getString(R.string.message_internet_disconnected), true);
        }
    }

    private class getAllQuestionsTask extends AsyncTask<Void, Void, List<Question>> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(QuestionsActivity.this);
            dialog.setMessage("Please Wait");
            dialog.setIndeterminate(true);
            dialog.show();
        }

        @Override
        protected List<Question> doInBackground(Void... voids) {
            List<Question> questions = SimbiApi.getInstance(getBaseContext()).getAllQuestions();
            return questions;
        }

        @Override
        protected void onPostExecute(List<Question> questions) {
            super.onPostExecute(questions);

            if (questions != null && questions.size() > 0) {
                mRecyclerView.setAdapter(new QuestionsAdapter(getBaseContext(), questions));
            }

            swipeRefreshLayout.setRefreshing(false);
            dialog.dismiss();
        }
    }
}
