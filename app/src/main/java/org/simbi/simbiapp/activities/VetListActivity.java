package org.simbi.simbiapp.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class VetListActivity extends AppCompatActivity {

    private Toolbar toolBar;

    private RecyclerView mRecyclerView;

    private FloatingActionMenu floatingActionMenu;

    private View transparentOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vet_list);

        toolBar = (Toolbar) findViewById(R.id.toolbar_vet_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.vet_list_recycler_view);
        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.floating_menu_filter);
        transparentOverlay = findViewById(R.id.transparent_overlay_view);
        setSupportActionBar(toolBar);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(VetListActivity.this));

        //populate recycler view with data asynchronously
        new FetchAllDoctorsTask().execute();

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
            mRecyclerView.setAdapter(adapter);
        }
    }

}
