package org.simbi.simbiapp.ui;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.squareup.otto.Subscribe;

import org.simbi.simbiapp.R;
import org.simbi.simbiapp.ui.adapters.DoctorListAdapter;
import org.simbi.simbiapp.api.interfaces.DoctorsClient;
import org.simbi.simbiapp.api.retrofit.RetrofitDoctorsClient;
import org.simbi.simbiapp.events.doctors.DoctorsListEvent;
import org.simbi.simbiapp.events.doctors.DoctorsListFailedEvent;
import org.simbi.simbiapp.utils.AlertDialogManager;
import org.simbi.simbiapp.utils.SessionManagement;
import org.simbi.simbiapp.utils.Utils;

public class DoctorsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private View mDoctorsListView;
    private Context context;

    private FloatingActionMenu floatingActionMenu;

    private View transparentOverlay; //semi transparent overlay

    private AlertDialogManager alert = new AlertDialogManager();
    private SharedPreferences prefs;
    private ProgressDialog dialog;

    private DoctorsClient doctorsClient;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        doctorsClient = RetrofitDoctorsClient.getClient(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.doctors_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mDoctorsListView = getView();

        mRecyclerView = (RecyclerView) mDoctorsListView.findViewById
                (R.id.vet_list_recycler_view);
        floatingActionMenu = (FloatingActionMenu) mDoctorsListView.findViewById
                (R.id.floating_menu_filter);
        transparentOverlay = mDoctorsListView.findViewById
                (R.id.transparent_overlay_view);
        swipeRefreshLayout = (SwipeRefreshLayout) mDoctorsListView.findViewById
                (R.id.refresh_doctor_list);

        swipeRefreshLayout.setColorSchemeColors(R.color.color_primary, R.color.color_primary_light);
        swipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

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

        refreshDoctorsList();//fetch the doctors list
    }

    @Override
    public void onRefresh() {
        refreshDoctorsList();
    }

    private void refreshDoctorsList() {
        if (Utils.hasInternetConnectivity(context)) {

            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Please Wait");
            dialog.setIndeterminate(true);
          //  dialog.show();
            //populate doctors list
            String token = prefs.getString(SessionManagement.KEY_AUTH_TOKEN, "");

            doctorsClient.getBus().register(new DoctorsListenerEvent());
            doctorsClient.getDoctors(token);


        } else {
            alert.showAlertDialog(context, getString(R.string.message_login_fail),
                    getString(R.string.message_internet_disconnected), true);
        }
    }

    private class DoctorsListenerEvent {

        @Subscribe
        public void onDoctorsListReceived(DoctorsListEvent event) {

            dialog.dismiss();
            doctorsClient.getBus().unregister(this);

            swipeRefreshLayout.setRefreshing(false);

            mRecyclerView.setAdapter(new DoctorListAdapter(context,
                    event.getDoctors()));
        }

        @Subscribe
        public void onDoctorsListFailed(DoctorsListFailedEvent event) {
            dialog.dismiss();

            doctorsClient.getBus().unregister(this);
            Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}