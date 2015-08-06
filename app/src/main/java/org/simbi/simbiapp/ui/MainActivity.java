package org.simbi.simbiapp.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.simbi.simbiapp.R;
import org.simbi.simbiapp.utils.SessionManagement;

public class MainActivity extends AppCompatActivity {

    Toolbar toolBar;

    SessionManagement session;

    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolBar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolBar);

        session = new SessionManagement(getApplicationContext());
        if (session.checkLogin()) {
            //open dashboard fragment
            fragment = DashBoardFragment.createInstance();

        } else {
            //not logged in open login fragment
            fragment = LoginFragment.createInstance();
        }

        getFragmentManager().beginTransaction()
                .replace(R.id.main_activity_container, fragment)
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() > 0) {

            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
            Toast.makeText(getBaseContext(), "finish", Toast.LENGTH_SHORT)
                    .show();
        }
    }

}