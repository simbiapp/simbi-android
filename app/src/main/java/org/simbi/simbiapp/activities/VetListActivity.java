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
import android.view.View;

import com.github.clans.fab.FloatingActionMenu;

import org.simbi.simbiapp.R;
import org.simbi.simbiapp.adapters.VetListAdapter;
import org.simbi.simbiapp.utils.AlertDialogManager;
import org.simbi.simbiapp.utils.MiscUtils;

public class VetListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private Toolbar toolBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private FloatingActionMenu floatingActionMenu;

    private View transparentOverlay;


    private AlertDialogManager alert = new AlertDialogManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vet_list);

        toolBar = (Toolbar) findViewById(R.id.toolbar_vet_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.vet_list_recycler_view);
        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.floating_menu_filter);
        transparentOverlay = findViewById(R.id.transparent_overlay_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_doctor_list);
        setSupportActionBar(toolBar);

        swipeRefreshLayout.setColorSchemeColors(R.color.color_primary,R.color.color_primary_lightgit );
        swipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(VetListActivity.this));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        floatingActionMenu.setClosedOnTouchOutside(true);

        floatingActionMenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean toggle) {
                if (toggle) {
                    // add a semi transparent overlay when float menu action is pressed
                    transparentOverlay.setVisibility(View.VISIBLE);
                } else {
                    // remove the overlay
                    transparentOverlay.setVisibility(View.GONE);
                }
            }
        });

        if (MiscUtils.hasInternetConnectivity(getBaseContext())) {
            //populate recycler view with data asynchronously
            new FetchAllDoctorsTask().execute();
        } else {
            alert.showAlertDialog(VetListActivity.this, getString(R.string.message_login_fail),
                    getString(R.string.message_internet_disconnected), true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_vet_profile, menu);
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
        new FetchAllDoctorsTask().execute();
    }

    private class FetchAllDoctorsTask extends AsyncTask<Void, Void, Void> {

        VetListAdapter adapter;
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(VetListActivity.this);
            dialog.setMessage("Please Wait");
            dialog.setIndeterminate(true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            adapter = new VetListAdapter(getBaseContext());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            swipeRefreshLayout.setRefreshing(false);
            mRecyclerView.setAdapter(adapter);
        }
    }

}
