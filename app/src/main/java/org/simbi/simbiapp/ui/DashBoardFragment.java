package org.simbi.simbiapp.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.simbi.simbiapp.R;
import org.simbi.simbiapp.utils.AlertDialogManager;
import org.simbi.simbiapp.utils.SessionManagement;

public class DashBoardFragment extends Fragment {

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

    // Session Manager Class
    SessionManagement session;

    View mDashBoardView;

    //Linear layout is acting as a button
    LinearLayout searchVetProfile;
    LinearLayout askQuestions;

    Fragment mFragment;

    public static Fragment createInstance() {
        return new DashBoardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dashboard_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mDashBoardView = getView();

        searchVetProfile = (LinearLayout) mDashBoardView.findViewById(R.id.search_vet_profile_button);
        askQuestions = (LinearLayout) mDashBoardView.findViewById(R.id.question_button);

        searchVetProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new DoctorsListFragment());
            }
        });

        askQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new QuestionsFragment());
            }
        });

    }

    private void openFragment(Fragment mFragment) {
        getFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_activity_container, mFragment)
                .commit();
    }
}
