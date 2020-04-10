package com.gibatekpro.psd;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import za.co.riggaroo.materialhelptutorial.TutorialItem;
import za.co.riggaroo.materialhelptutorial.tutorial.MaterialTutorialActivity;


public class CompareActivity extends AppCompatActivity implements nowFragment.OnFragmentInteractionListener {



    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private int REQUEST_CODE = 1234;

    private int numtabs = 0;

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
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOffscreenPageLimit(numtabs);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        switch (id) {

            case R.id.action_about:
                final Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_about);
                dialog.show();

                Button moreApps = (Button) dialog.findViewById(R.id.accept);
                Button dismiss = (Button) dialog.findViewById(R.id.back);


                moreApps.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewMoreApps("gibatekpro");
                        //moreApps();
                        dialog.cancel();
                    }
                });

                dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                return true;
            case R.id.action_rate:
              rate();
            return true;
            case R.id.action_help:
                loadTutorial();
                return true;
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_viewmore:
            viewMoreApps("gibatekpro");
            return true;
            //case R.id.action_share:
            //shareApp(getString(R.string.app_name), MainActivity.this);
            //return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void viewMoreApps(@NonNull String devName) {
        //noinspection ConstantConditions
        if (devName == null || devName.isEmpty()) {
            throwIllegalArgument("Developer name cannot be null or empty.", "Viewmoreapps");
        }
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:" + devName)));
        } catch (ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/search?q=pub:" + devName)));
        }
    }

    private void throwIllegalArgument(@NonNull String message, @NonNull String cause) {
        Exception r = new NullPointerException(cause);
        throw new IllegalArgumentException(message, r);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
            return new nowFragment();
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return numtabs;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return"Device " + (position + 1);
        }
    }
    public void loadTutorial() {
        Intent mainAct = new Intent(this, MaterialTutorialActivity.class);
        mainAct.putParcelableArrayListExtra(MaterialTutorialActivity.MATERIAL_TUTORIAL_ARG_TUTORIAL_ITEMS, getTutorialItems(this));
        startActivityForResult(mainAct, REQUEST_CODE);

    }

    public void showTutorial() {
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (s.getBoolean("Help", true)) {
            loadTutorial();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE) {
            SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            s.edit().putBoolean("Help", false).apply();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private ArrayList<TutorialItem> getTutorialItems(Context context) {
        TutorialItem tutorialItem1 = new TutorialItem("MULTIPLE DEVICES", "Simply press the button of the device and select the brand to get specifications.",
                R.color.colorhelp1, R.drawable.help1);

        // You can also add gifs, [IN BETA YET] (because Glide is awesome!)
        TutorialItem tutorialItem2 = new TutorialItem("COMPARE DEVICES", "Click the button and select the number of devices you wish to compare.",
                R.color.colorhelp2, R.drawable.help2);


        TutorialItem tutorialItem3 = new TutorialItem("EASY COMPARISON", "Slide through tabs easily to view specs of devices you are comparing.",
                R.color.colorhelp3, R.drawable.help3);


        TutorialItem tutorialItem4 = new TutorialItem("RATE APP", "Please support the developer by taking out time to rate this app. It is a fast and easy process.",
                R.color.colorhelp4, R.drawable.help4);


        ArrayList<TutorialItem> tutorialItems = new ArrayList<>();
        tutorialItems.add(tutorialItem1);
        tutorialItems.add(tutorialItem2);
        tutorialItems.add(tutorialItem3);
        tutorialItems.add(tutorialItem4);

        return tutorialItems;
    }

    public void rate() {
        String pName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pName)));
        } catch (ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + pName)));
        }
    }
}


