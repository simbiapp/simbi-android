package org.simbi.simbiapp.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import org.simbi.simbiapp.R;
import org.simbi.simbiapp.api.interafaces.UserClient;
import org.simbi.simbiapp.api.retrofit.RetrofitUserClient;
import org.simbi.simbiapp.events.user.UserLoginEvent;
import org.simbi.simbiapp.events.user.UserLoginFailedEvent;
import org.simbi.simbiapp.utils.AlertDialogManager;
import org.simbi.simbiapp.utils.SessionManagement;
import org.simbi.simbiapp.utils.SimbiConstants;
import org.simbi.simbiapp.utils.Utils;

public class LoginActivity extends Activity implements SimbiConstants {

    private static Context context;
    // Email, password edittext
    EditText txtUsername, txtPassword;
    // login button
    Button btnLogin;
    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();
    // Session Manager Class
    SessionManagement session;

    ProgressDialog dialog;

    UserClient userClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Session Manager
        session = new SessionManagement(getApplicationContext());

        userClient = RetrofitUserClient.getClient();
        context = getBaseContext();

        // Email, Password input text
        txtUsername = (EditText) findViewById(R.id.edtUserName);
        txtPassword = (EditText) findViewById(R.id.edtPassword);

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

                if (username.length() > 0 && password.length() > 0) {

                    if (Utils.hasInternetConnectivity(context)) {

                        dialog = new ProgressDialog(LoginActivity.this);
                        dialog.setMessage("Please Wait");
                        dialog.setIndeterminate(true);
                        dialog.show();

                        userClient.getBus().register(new LoginEventListeners());

                        userClient.login(username, password);

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

    private class LoginEventListeners {

        @Subscribe
        public void onLoginSuccess(final UserLoginEvent event) {

            userClient.getBus().unregister(this);
            Toast.makeText(getBaseContext(), "Successfully Logged In", Toast.LENGTH_SHORT)
                    .show();

            //creating login session
            SessionManagement sessionManagement = new SessionManagement(context);
            sessionManagement.createLoginSession(event.getUserName(),
                    event.getToken());

            //login success starting main activity
            Intent i = new Intent(context, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);

            dialog.dismiss();

            finish();//close login activity
        }

        @Subscribe
        public void onLoginFailed(final UserLoginFailedEvent event) {
            dialog.dismiss();

            userClient.getBus().unregister(this);
            Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT)
                    .show();
        }
    }

}
