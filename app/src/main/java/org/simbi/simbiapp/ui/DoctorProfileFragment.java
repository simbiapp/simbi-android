package org.simbi.simbiapp.ui;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.simbi.simbiapp.R;
import org.simbi.simbiapp.api.interfaces.DoctorsClient;
import org.simbi.simbiapp.api.retrofit.RetrofitDoctorsClient;
import org.simbi.simbiapp.events.doctors.DoctorProfileEvent;
import org.simbi.simbiapp.events.doctors.DoctorProfileFailedEvent;
import org.simbi.simbiapp.utils.AlertDialogManager;
import org.simbi.simbiapp.utils.SessionManagement;
import org.simbi.simbiapp.utils.SimbiConstants;
import org.simbi.simbiapp.utils.Utils;

public class DoctorProfileFragment extends Fragment {

    Menu menu;
    View mView;
    Context mContext;

    ImageView doctorImageView;
    View doctorOnline;
    TextView doctorSpecialization, doctorLocation;
    TextView experience, rateByHour, likes;

    LinearLayout biographyLayout;
    TextView biographyDetailsTextView;
    TextView biographyTextView;

    LinearLayout languagesLayout;
    TextView languageDetailsTextView;
    TextView languageTextView;

    LinearLayout profileContainer;

    Button contactDoctor;
    AlertDialogManager alert = new AlertDialogManager();
    SharedPreferences prefs;
    ProgressBar progressBar;
    boolean addedToFavourite = false;

    DoctorsClient doctorsClient;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();
        doctorsClient = RetrofitDoctorsClient.getClient(mContext);
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        progressBar=(ProgressBar) getActivity().findViewById(R.id.progress_bar);
    }

    @Override
    public void onResume() {
        super.onResume();

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(getString(R.string.app_name));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.doctor_profile_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mView = getView();

        profileContainer=(LinearLayout) mView.findViewById(R.id.doctor_profile_container);
        doctorImageView = (ImageView) mView.findViewById(R.id.doctor_image_profile);
        doctorOnline = mView.findViewById(R.id.doctor_online);
        doctorSpecialization = (TextView) mView.findViewById(R.id.doctor_specialization_text);
        doctorLocation = (TextView) mView.findViewById(R.id.doctor_location_text);
        experience = (TextView) mView.findViewById(R.id.doc_experience_text);
        rateByHour = (TextView) mView.findViewById(R.id.rate_by_hour_text);
        likes = (TextView) mView.findViewById(R.id.likes_text);
        contactDoctor = (Button) mView.findViewById(R.id.contact_vet);
        biographyLayout = (LinearLayout) mView.findViewById(R.id.layout_biography);
        biographyDetailsTextView = (TextView) mView.findViewById(R.id.biography_details);
        biographyTextView = (TextView) mView.findViewById(R.id.biography_textview);
        languagesLayout = (LinearLayout) mView.findViewById(R.id.layout_languages);
        languageDetailsTextView = (TextView) mView.findViewById(R.id.languages_details);
        languageTextView = (TextView) mView.findViewById(R.id.language_textview);

        profileContainer.setVisibility(View.GONE);

        biographyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (biographyDetailsTextView.getVisibility() == View.GONE) {
                    biographyDetailsTextView.setVisibility(View.VISIBLE);
                    biographyTextView.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_biography, 0,
                            R.drawable.ic_expand_down, 0);
                } else {
                    biographyDetailsTextView.setVisibility(View.GONE);
                    biographyTextView.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_biography, 0,
                            R.drawable.ic_expand_right, 0);
                }
            }
        });

        languagesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (languageDetailsTextView.getVisibility() == View.GONE) {
                    languageDetailsTextView.setVisibility(View.VISIBLE);
                    languageTextView.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_language, 0,
                            R.drawable.ic_expand_down, 0);
                } else {
                    languageDetailsTextView.setVisibility(View.GONE);
                    languageTextView.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_language, 0,
                            R.drawable.ic_expand_right, 0);
                }
            }
        });

        progressBar.setVisibility(View.VISIBLE);
        String doctorId = getArguments().getString(SimbiConstants.BUNDLE_DOC_ID);

        if (doctorId != null && doctorId.length() != 0) {
            if (Utils.hasInternetConnectivity(mContext)) {

                doctorsClient.getBus().register(new VetProfileListener());

                String token = prefs.getString(SessionManagement.KEY_AUTH_TOKEN, "");

                doctorsClient.getDoctorsById(doctorId, token);

            } else {
                alert.showAlertDialog(mContext, getString(R.string.message_login_fail),
                        getString(R.string.message_internet_disconnected), false);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_vet_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
        } else if (id == R.id.add_to_fav) {
            if (!addedToFavourite) {
                Toast.makeText(mContext, "Added To Favourites", Toast.LENGTH_SHORT)
                        .show();
                addedToFavourite = true;
                menu.findItem(R.id.add_to_fav).setIcon(R.drawable.ic_fab_star);
            } else {
                Toast.makeText(mContext, "Removed From Favourites", Toast.LENGTH_SHORT)
                        .show();
                addedToFavourite = false;
                menu.findItem(R.id.add_to_fav).setIcon(R.drawable.ic_fab_star_off);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    private class VetProfileListener {

        @Subscribe
        public void onProfileReceived(DoctorProfileEvent event) {

            doctorsClient.getBus().unregister(this);
            progressBar.setVisibility(View.GONE);
            profileContainer.setVisibility(View.VISIBLE);

            Picasso.with(mContext)
                    .load(event.getDoctor().getPhoto())
                    .into(doctorImageView);

            getActionBar().setTitle("Dr Jane Doe");
            doctorSpecialization.setText(event.getDoctor().getSpecialization());
            experience.setText(event.getDoctor().getExperience());
            languageDetailsTextView.setText(event.getDoctor().getLanguage());
        }

        @Subscribe
        public void onProfileFailed(DoctorProfileFailedEvent event) {

            doctorsClient.getBus().unregister(this);
            Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT)
                    .show();
            //TODO handle error properly
        }

    }
}
