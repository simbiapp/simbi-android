package org.simbi.simbiapp.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.simbi.simbiapp.R;
import org.simbi.simbiapp.utils.AlertDialogManager;
import org.simbi.simbiapp.utils.MiscUtils;
import org.simbi.simbiapp.utils.SessionManagement;
import org.simbi.simbiapp.utils.SimbiApi;

public class LoginActivity extends Activity {

    private static Context context;
    // Email, password edittext
    EditText txtUsername, txtPassword;
    // login button
    Button btnLogin;
    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();
    // Session Manager Class
    SessionManagement session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Session Manager
        session = new SessionManagement(getApplicationContext());
        context = getBaseContext();

        // Email, Password input text
        txtUsername = (EditText) findViewById(R.id.edtUserName);
        txtPassword = (EditText) findViewById(R.id.edtPassword);

        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        // Login button
        btnLogin = (Button) findViewById(R.id.btnLogin);


        // Login button click event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Get username, password from EditText
                String username = txtUsername.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();

                // Check if username, password is filled

                if (username.trim().length() > 0 && password.trim().length() > 0) {

                    if (MiscUtils.hasInternetConnectivity(context)) {
                        new LoginTask().execute(username.trim(), password.trim());
                    } else {
                        // does not have internet connectivity
                        alert.showAlertDialog(LoginActivity.this, getString(R.string.message_login_fail),
                                getString(R.string.message_internet_disconnected),
                                false);
                    }
                } else {
                    // user didn't entered username or password
                    // Show alert asking him to enter the details
                    alert.showAlertDialog(LoginActivity.this, getString(R.string.message_login_fail),
                            getString(R.string.message_enter_user_pass),
                            false);
                }

            }
        });
    }

    private class LoginTask extends AsyncTask<String, Void, Void> {

        SessionManagement sessionManagement;
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Please Wait");
            dialog.setIndeterminate(true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... credentials) {
            SimbiApi.getInstance(context).doLogin(credentials[0], credentials[1]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            dialog.dismiss();
            sessionManagement = new SessionManagement(context);
            if (sessionManagement.isLoggedIn()) {
                Intent i = new Intent(context, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        }
    }
}
