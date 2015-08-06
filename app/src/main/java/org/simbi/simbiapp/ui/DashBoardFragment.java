package org.simbi.simbiapp.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.simbi.simbiapp.R;

public class DashBoardFragment extends Fragment {

    View mDashBoardView;

    //Linear layout is acting as a button
    LinearLayout searchVetProfile;
    LinearLayout askQuestions;

    public static Fragment createInstance() {
        return new DashBoardFragment();
    }

    @Override
    public void onResume() {
        super.onResume();

        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setTitle(getString(R.string.app_name));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dashboard_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mDashBoardView = getView();

        searchVetProfile = (LinearLayout) mDashBoardView
                .findViewById(R.id.search_vet_profile_button);
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

    public ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    private void openFragment(Fragment mFragment) {
        getFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_activity_container, mFragment)
                .commit();
    }

}
