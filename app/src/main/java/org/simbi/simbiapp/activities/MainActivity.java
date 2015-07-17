package org.simbi.simbiapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import org.simbi.simbiapp.R;
import org.simbi.simbiapp.utils.AlertDialogManager;
import org.simbi.simbiapp.utils.SessionManagement;
import org.simbi.simbiapp.utils.SimbiApi;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    Toolbar toolBar;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

    // Session Manager Class
    SessionManagement session;

    //Linear layout is acting as a button
    LinearLayout searchVetProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolBar = (Toolbar) findViewById(R.id.toolbar);
        searchVetProfile = (LinearLayout) findViewById(R.id.search_vet_profile_button);
        setSupportActionBar(toolBar);

        session = new SessionManagement(getApplicationContext());
        session.checkLogin();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (SimbiApi.getInstance(getBaseContext()).canAuthenticateToServer()) {
                        Log.d("satan", "evil");
                    } else {
                        Log.d("god", "obscene");
                    }
                } catch (Exception e) {

                }
            }
        }).start();


        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(SessionManagement.KEY_NAME);

        // email
        String email = user.get(SessionManagement.KEY_AUTH_TOKEN);

        searchVetProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, VetListActivity.class);
                startActivity(intent);
            }
        });
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
        }

        return super.onOptionsItemSelected(item);
    }
}