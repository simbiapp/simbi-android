package org.simbi.simbiapp.ui;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import org.simbi.simbiapp.utils.Utils;

public class LoginFragment extends Fragment {

    private EditText txtUsername, txtPassword;

    private Button btnLogin;

    private View mView;

    private AlertDialogManager alert = new AlertDialogManager();

    // Session Manager Class
    private SessionManagement session;

    private ProgressDialog mProgressDialog;

    private UserClient userClient;

    private LoginEventListener eventListener;

    public static Fragment createInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userClient = RetrofitUserClient.getClient();

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle("Logging in...");
        mProgressDialog.setMessage("Logging in...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mView = getView();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        // Session Manager
        session = new SessionManagement(getActivity());

        userClient = RetrofitUserClient.getClient();

        // Email, Password input text
        txtUsername = (EditText) mView.findViewById(R.id.edtUserName);
        txtPassword = (EditText) mView.findViewById(R.id.edtPassword);

        // Login button
        btnLogin = (Button) mView.findViewById(R.id.btnLogin);

        // Login button click event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Get username, password from EditText
                String username = txtUsername.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();

                // Check if username, password is filled

                if (username.length() > 0 && password.length() > 0) {

                    if (Utils.hasInternetConnectivity(getActivity())) {

                        mProgressDialog.show();

                        eventListener = new LoginEventListener();
                        userClient.getBus().register(eventListener);

                        userClient.login(username, password);

                    } else {
                        // does not have internet connectivity
                        alert.showAlertDialog(getActivity(), getString(R.string.message_login_fail),
                                getString(R.string.message_internet_disconnected),
                                false);
                    }
                } else {
                    // user didn't entered username or password
                    // Show alert asking him to enter the details
                    alert.showAlertDialog(getActivity(), getString(R.string.message_login_fail),
                            getString(R.string.message_enter_user_pass),
                            false);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    private class LoginEventListener {

        @Subscribe
        public void onLoginSuccess(final UserLoginEvent event) {

            userClient.getBus().unregister(this);

            mProgressDialog.dismiss();

            Toast.makeText(getActivity(), "Successfully Logged In", Toast.LENGTH_SHORT)
                    .show();

            //creating login session
            SessionManagement sessionManagement = new SessionManagement(getActivity());
            sessionManagement.createLoginSession(event.getUserName(),
                    event.getToken());

            //login success opening dashboard
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_activity_container, new DashBoardFragment())
                    .commit();
        }

        @Subscribe
        public void onLoginFailed(final UserLoginFailedEvent event) {
            mProgressDialog.dismiss();

            userClient.getBus().unregister(this);
            Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
