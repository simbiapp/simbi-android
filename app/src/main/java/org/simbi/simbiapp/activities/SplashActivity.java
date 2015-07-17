package org.simbi.simbiapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import org.simbi.simbiapp.R;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_DURATION = 1;
    public String SHOW_SPLASH = "show_splash";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        if (!sharedPreferences.getBoolean(SHOW_SPLASH, false)) {
            setContentView(R.layout.activity_splash);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }, SPLASH_DURATION * 1000);

            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
            prefsEditor.putBoolean(SHOW_SPLASH, true);
            prefsEditor.commit();
        } else {
            Intent goToMainActivity = new Intent(SplashActivity.this, MainActivity.class);
            goToMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(goToMainActivity);
            finish();
        }
    }
}
