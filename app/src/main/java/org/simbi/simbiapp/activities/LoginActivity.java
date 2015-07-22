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

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.simbi.simbiapp.BuildConfig;
import org.simbi.simbiapp.R;
import org.simbi.simbiapp.SimbiApp;
import org.simbi.simbiapp.utils.AlertDialogManager;
import org.simbi.simbiapp.utils.Constants;
import org.simbi.simbiapp.utils.MiscUtils;
import org.simbi.simbiapp.utils.SessionManagement;
import org.simbi.simbiapp.utils.SimbiApi;

import java.io.UnsupportedEncodingException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

public class LoginActivity extends Activity implements Constants {

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

                    if (MiscUtils.hasInternetConnectivity(context)) {

                        dialog = new ProgressDialog(LoginActivity.this);
                        dialog.setMessage("Please Wait");
                        dialog.setIndeterminate(true);
                        dialog.show();

                        doLogin(username, password);
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

    private void doLogin(final String user, final String pass) {

        SimbiApi apiService = ((SimbiApp) getApplication()).getSimbiApiService();

        JSONObject credentials = new JSONObject();
        TypedInput inputJson = null; //we are posting input json as raw data

        try {
            credentials.put("username", user);
            credentials.put("password", pass);

            inputJson = new TypedByteArray("application/json", credentials.toString().getBytes("UTF-8"));
        } catch (JSONException j) {
            j.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        apiService.loginToSimbi(inputJson, new Callback<Object>() {
            @Override
            public void success(Object object, retrofit.client.Response response) {

                String result = new Gson().toJson(object); //this is the response json

                dialog.dismiss();
                JSONObject jsonResult = null;
                try {
                    jsonResult = new JSONObject(result);

                    String status = jsonResult.getString("status");

                    if (status.equals(SimbiApi.STATUS_OK)) {
                        String token = jsonResult.getString("token");

                        if (token != null && token.length() > 0) {
                            //creating login session
                            SessionManagement sessionManagement = new SessionManagement(context);
                            sessionManagement.createLoginSession(user,
                                    token);

                            //login success starting main activity
                            Intent i = new Intent(context, MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                            finish();
                        }
                    } else {
                        Toast.makeText(context, "Wrong Username or Password", Toast.LENGTH_SHORT)
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                dialog.dismiss();

                Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }


}
