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
import android.view.View;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.squareup.otto.Subscribe;

import org.simbi.simbiapp.R;
import org.simbi.simbiapp.adapters.VetListAdapter;
import org.simbi.simbiapp.api.interafaces.DoctorsClient;
import org.simbi.simbiapp.api.retrofit.RetrofitDoctorsClient;
import org.simbi.simbiapp.events.doctors.DoctorsListEvent;
import org.simbi.simbiapp.events.doctors.DoctorsListFailedEvent;
import org.simbi.simbiapp.utils.AlertDialogManager;
import org.simbi.simbiapp.utils.SessionManagement;
import org.simbi.simbiapp.utils.Utils;

public class VetListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private Toolbar toolBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private FloatingActionMenu floatingActionMenu;

    private View transparentOverlay; //semi transparent overlay

    private AlertDialogManager alert = new AlertDialogManager();
    private SharedPreferences prefs;
    private ProgressDialog dialog;

    private DoctorsClient doctorsClient;

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
        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        doctorsClient = RetrofitDoctorsClient.getClient(getBaseContext());

        swipeRefreshLayout.setColorSchemeColors(R.color.color_primary, R.color.color_primary_light);
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
        refreshDoctorsList();
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
        refreshDoctorsList();
    }

    private void refreshDoctorsList() {
        if (Utils.hasInternetConnectivity(getBaseContext())) {

            dialog = new ProgressDialog(VetListActivity.this);
            dialog.setMessage("Please Wait");
            dialog.setIndeterminate(true);
            dialog.show();
            //populate doctors list
            String token = prefs.getString(SessionManagement.KEY_AUTH_TOKEN, "");

            doctorsClient.getBus().register(new DoctorsListenerEvent());
            doctorsClient.getDoctors(token);


        } else {
            alert.showAlertDialog(VetListActivity.this, getString(R.string.message_login_fail),
                    getString(R.string.message_internet_disconnected), true);
        }
    }

    private class DoctorsListenerEvent {

        @Subscribe
        public void onDoctorsListReceived(DoctorsListEvent event) {

            doctorsClient.getBus().unregister(this);
            dialog.dismiss();
            swipeRefreshLayout.setRefreshing(false);

            mRecyclerView.setAdapter(new VetListAdapter(getBaseContext(),
                    event.getDoctors()));
        }

        @Subscribe
        public void onDoctorsListFailed(DoctorsListFailedEvent event) {
            dialog.dismiss();

            doctorsClient.getBus().unregister(this);
            Toast.makeText(getBaseContext(), "Something Went Wrong", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
