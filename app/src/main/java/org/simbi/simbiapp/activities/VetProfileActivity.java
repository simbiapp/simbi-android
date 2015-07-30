package org.simbi.simbiapp.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.simbi.simbiapp.R;
import org.simbi.simbiapp.api.interafaces.DoctorsClient;
import org.simbi.simbiapp.api.retrofit.RetrofitDoctorsClient;
import org.simbi.simbiapp.events.doctors.DoctorProfileEvent;
import org.simbi.simbiapp.events.doctors.DoctorProfileFailedEvent;
import org.simbi.simbiapp.utils.AlertDialogManager;
import org.simbi.simbiapp.utils.SessionManagement;
import org.simbi.simbiapp.utils.SimbiConstants;
import org.simbi.simbiapp.utils.Utils;

public class VetProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    Menu menu;

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

    Button contactDoctor;
    AlertDialogManager alert = new AlertDialogManager();
    SharedPreferences prefs;
    ProgressDialog dialog;
    boolean addedToFavourite = false;

    DoctorsClient doctorsClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vet_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar_vet_profile);
        doctorImageView = (ImageView) findViewById(R.id.doctor_image_profile);
        doctorOnline = findViewById(R.id.doctor_online);
        doctorSpecialization = (TextView) findViewById(R.id.doctor_specialization_text);
        doctorLocation = (TextView) findViewById(R.id.doctor_location_text);
        experience = (TextView) findViewById(R.id.doc_experience_text);
        rateByHour = (TextView) findViewById(R.id.rate_by_hour_text);
        likes = (TextView) findViewById(R.id.likes_text);
        contactDoctor = (Button) findViewById(R.id.contact_vet);
        biographyLayout = (LinearLayout) findViewById(R.id.layout_biography);
        biographyDetailsTextView = (TextView) findViewById(R.id.biography_details);
        biographyTextView = (TextView) findViewById(R.id.biography_textview);
        languagesLayout = (LinearLayout) findViewById(R.id.layout_languages);
        languageDetailsTextView = (TextView) findViewById(R.id.languages_details);
        languageTextView = (TextView) findViewById(R.id.language_textview);

        doctorsClient = RetrofitDoctorsClient.getClient(getBaseContext());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

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

        String doctorId = getIntent().getStringExtra(SimbiConstants.BUNDLE_DOC_ID);

        if (doctorId != null && doctorId.length() != 0) {
            if (Utils.hasInternetConnectivity(getBaseContext())) {

                dialog = new ProgressDialog(VetProfileActivity.this);
                dialog.setMessage("Please Wait");
                dialog.setIndeterminate(true);
                dialog.show();

                doctorsClient.getBus().register(new VetProfileListener());

                String token = prefs.getString(SessionManagement.KEY_AUTH_TOKEN, "");

                doctorsClient.getDoctorsById(doctorId, token);

            } else {
                alert.showAlertDialog(getBaseContext(), getString(R.string.message_login_fail),
                        getString(R.string.message_internet_disconnected), false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vet_profile, menu);
        this.menu = menu;
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
        } else if (id == R.id.add_to_fav) {
            if (!addedToFavourite) {
                Toast.makeText(getBaseContext(), "Added To Favourites", Toast.LENGTH_SHORT)
                        .show();
                addedToFavourite = true;
                menu.findItem(R.id.add_to_fav).setIcon(R.drawable.ic_fab_star);
            } else {
                Toast.makeText(getBaseContext(), "Removed From Favourites", Toast.LENGTH_SHORT)
                        .show();
                addedToFavourite = false;
                menu.findItem(R.id.add_to_fav).setIcon(R.drawable.ic_fab_star_off);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private class VetProfileListener {

        @Subscribe
        public void onProfileReceived(DoctorProfileEvent event) {
            dialog.dismiss();

            Picasso.with(getBaseContext())
                    .load(event.getDoctor().getPhoto())
                    .into(doctorImageView);

            getSupportActionBar().setTitle("Dr Jane Doe");
            doctorSpecialization.setText(event.getDoctor().getSpecialization());
            experience.setText(event.getDoctor().getExperience());
            languageDetailsTextView.setText(event.getDoctor().getLanguage());
        }

        @Subscribe
        public void onProfileFailed(DoctorProfileFailedEvent event) {
            dialog.dismiss();

            doctorsClient.getBus().unregister(this);
            Toast.makeText(getBaseContext(), "Something went wrong", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
