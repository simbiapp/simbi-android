package org.simbi.simbiapp.activities;

import android.graphics.Color;
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
        transparentOverlay=findViewById(R.id.transparent_overlay_view);
        setSupportActionBar(toolBar);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new VetListAdapter(getBaseContext()));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        floatingActionMenu.setClosedOnTouchOutside(true);

        floatingActionMenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean toggle) {
                if(toggle) {
                    /*
                    add a semi transparent overlay when float menu action is pressed
                     */
                    floatingActionMenu.setBackgroundColor(Color.WHITE);
                    transparentOverlay.setVisibility(View.VISIBLE);
                }
                else {
                    // remove the overlay
                    floatingActionMenu.setBackgroundColor(Color.TRANSPARENT);
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

}
