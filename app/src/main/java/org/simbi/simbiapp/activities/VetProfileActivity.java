package org.simbi.simbiapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.simbi.simbiapp.R;
import org.simbi.simbiapp.utils.Constants;

public class VetProfileActivity extends AppCompatActivity {

    Toolbar toolbar;

    LinearLayout biographyLayout;
    TextView biographyDetailsTextView;
    TextView biographyTextView;

    LinearLayout languagesLayout;
    TextView languageDetailsTextView;
    TextView languageTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vet_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar_vet_profile);
        biographyLayout = (LinearLayout) findViewById(R.id.layout_biography);
        biographyDetailsTextView = (TextView) findViewById(R.id.biography_details);
        biographyTextView = (TextView) findViewById(R.id.biography_textview);
        languagesLayout = (LinearLayout) findViewById(R.id.layout_languages);
        languageDetailsTextView = (TextView) findViewById(R.id.languages_details);
        languageTextView = (TextView) findViewById(R.id.language_textview);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String doctorName = getIntent().getStringExtra(Constants.DOC_NAME);

        if (doctorName != null && doctorName.length() != 0) {
            getSupportActionBar().setTitle(doctorName);
        }

        biographyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (biographyDetailsTextView.getVisibility() == View.GONE) {
                    biographyDetailsTextView.setVisibility(View.VISIBLE);
                    biographyTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_vet,
                            0,
                            R.drawable.ic_expand_down, 0);
                } else {
                    biographyDetailsTextView.setVisibility(View.GONE);
                    biographyTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_vet,
                            0,
                            R.drawable.ic_expand_right, 0);
                }
            }
        });

        languagesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (languageDetailsTextView.getVisibility() == View.GONE) {
                    languageDetailsTextView.setVisibility(View.VISIBLE);
                    languageTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_language,
                            0, R.drawable.ic_expand_down, 0);
                } else {
                    languageDetailsTextView.setVisibility(View.GONE);
                    languageTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_language,
                            0,
                            R.drawable.ic_expand_right, 0);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vet_profile, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }
}
