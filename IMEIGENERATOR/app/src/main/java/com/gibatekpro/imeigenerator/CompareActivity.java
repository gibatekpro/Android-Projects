package com.gibatekpro.imeigenerator;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codemybrainsout.ratingdialog.RatingDialog;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class CompareActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    public ViewPager mViewPagers;
    private int REQUEST_CODE = 1234;

    private int numtabs = 0;

    SpinnerDialog spinnerDialog;

    public String device1 = "";

    public TabLayout tabLayouts;
    public int tabPositions;

    InterstitialAd mInterstitialAd;

    int back = 0;

    //Package Name Initialization
    private final static String APP_PACKAGE_NAME = "com.gibatekpro.imeigenerator";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        if (getIntent().hasExtra("tabs")){
            try{
                numtabs=Integer.parseInt(getIntent().getStringExtra("tabs"));
            }catch (NumberFormatException nfe){
                nfe.printStackTrace();
                numtabs=1;
            }

        }

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-1488497497647050~6912883522");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1488497497647050/8862120323");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPagers = (ViewPager) findViewById(R.id.container);
        mViewPagers.setAdapter(mSectionsPagerAdapter);
        mViewPagers.setOffscreenPageLimit(numtabs);


        tabLayouts = findViewById(R.id.tabs);
        tabLayouts.setupWithViewPager(mViewPagers);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compare, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_language) {
            return true;
        }

        if (id == R.id.action_rate) {
            rate(this);
            return true;
        }

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return new Compare_Tab();
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return numtabs;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            tabPositions = position;

            return getString(R.string.device) + " "  + (position + 1);
        }



    }

    //[START App Rate Function]
    public void rate(Context context) {

        final RatingDialog ratingDialog = new RatingDialog.Builder(context)
                .icon(getResources().getDrawable(R.mipmap.ic_launcher))
                .threshold(4)
                .title(getString(R.string.rate_app))
                .titleTextColor(R.color.black)
                .positiveButtonText(getString(R.string.later))
                .positiveButtonTextColor(R.color.colorPrimary)
                .negativeButtonText(getString(R.string.no))
                .negativeButtonTextColor(R.color.colorPrimary)
                .formTitle(getString(R.string.submit_feedback))
                .formHint(getString(R.string.tell_us_where_to_improve))
                .formSubmitText(getString(R.string.submit))
                .formCancelText(getString(R.string.cancel))
                .ratingBarColor(R.color.colorAccent)
                .playstoreUrl("https://play.google.com/store/apps/details?id=com.gibatekpro.imeigenerator")
                .onThresholdCleared(new RatingDialog.Builder.RatingThresholdClearedListener() {
                    @Override
                    public void onThresholdCleared(RatingDialog ratingDialog, float rating, boolean thresholdCleared) {

                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=" + APP_PACKAGE_NAME)));
                        } catch (ActivityNotFoundException e) {
                            // Toast.makeText(getActivity(), R.string.You_have_pressed_Rate_Button, Toast.LENGTH_SHORT).show();
                        }
                        ratingDialog.dismiss();
                    }
                })
                .onRatingChanged(new RatingDialog.Builder.RatingDialogListener() {
                    @Override
                    public void onRatingSelected(float rating, boolean thresholdCleared) {

                    }
                })
                .onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
                    @Override
                    public void onFormSubmitted(String feedback) {
                        Log.i("TAG", "Feedback:" + feedback);
                    }
                }).build();
        ratingDialog.show();

    }
    //[END App Rate Function]


    private void loadAds() {

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (back == 0) {

            try {
                loadAds();
            } catch (Exception e) {
                e.printStackTrace();
            }

            back++;

        }
    }
}
