package com.gibatekpro.imeiinfo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.codemybrainsout.ratingdialog.RatingDialog;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Navigation Drawer and Toolbar View
    NavigationView navigationView;
    Menu nav_Menu;
    Toolbar toolbar;
    View hView;
    // Navigation Drawer and Toolbar View

    TextView language, imei1, imei2, imei1_tag, imei2_tag, device_name;
    SpinnerDialog spinnerDialog;
    ImageButton copy1, copy2;

    ClipboardManager clipboardManager;

    final int reqcode = 1;

    String imeiSIM2, imeiSIM1, DeviceName;

    RelativeLayout relativeLayout;


    //[START] ViewPager and Fragments
    ViewPager mViewPager;
    public final int limit = 2;
    public Fragment[] fragments = new Fragment[]{new CustomImei_Tab(), new PhoneImei_Tab(), new Analyzer_Tab(), new Rate_Tab()};
    //[END] ViewPager and Fragments


    //Droid Appliance//
    SpotsDialog spotsDialog;
    //Droid Appliance//

    InterstitialAd fetchInterstitialAd;


    //Package Name Initialization
    private final static String APP_PACKAGE_NAME = "com.gibatekpro.imeiinfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //<editor-fold desc="Navigation Drawer Declaration">
        //[START Navigation Drawer and Toolbar Declaration]

        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        nav_Menu = navigationView.getMenu();
        hView = navigationView.inflateHeaderView(R.layout.nav_header_navigation_drawer);
        //[END Navigation Drawer and Toolbar Declaration]
        //</editor-fold>

        //<editor-fold desc="Fragments ViewPager">
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(limit);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        //</editor-fold>

        //<editor-fold desc="Droid Appliance">
        //Droid Appliance//
        //Spots Dialog (Loading)
        spotsDialog = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(MainActivity.this)
                .setMessage("Loading...")
                .setTheme(R.style.CustomSpotsDialog)
                .build();
        // Droid Appliance//
        //</editor-fold>

        //<editor-fold desc="Ads">
        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-1488497497647050~7241700328");

        fetchInterstitialAd = new InterstitialAd(this);
        fetchInterstitialAd.setAdUnitId("ca-app-pub-1488497497647050/3470648722");//IMEI.fetch.Interstitial
        fetchInterstitialAd.loadAd(new AdRequest.Builder().build());
        fetchInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                fetchInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });
        //</editor-fold>

        //<editor-fold desc="Language Arrays">
        ArrayList<String> items = new ArrayList<>();
        String[] array_items = this.getResources().getStringArray(R.array.language);
        Collections.addAll(items, array_items);
        //</editor-fold>

        spinnerDialog = new SpinnerDialog(MainActivity.this, items, getString(R.string.select_language));
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {

                chooseLocale(i);

            }
        });

        checkNew();

        try {
            if (!User_Account.isLoggedIn()) {
                showloggedOutUI();
            } else {
                showloggedInUI();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        if (id == R.id.action_language) {

            spinnerDialog.showSpinerDialog();

            return true;
        } else if (id == R.id.action_help) {
            helpFunction();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_imei) {

            try {
                fetchImei();
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if (id == R.id.nav_share) {

            shareFunction();

        } else if (id == R.id.nav_rate) {

            rate();

        } else if (id == R.id.nav_account) {

            Intent intent = new Intent(this, User_Account.class);
            String message = "regcreate";
            intent.putExtra("STRING_I_NEED", message);
            startActivity(intent);

        } else if (id == R.id.nav_logoff) {

            signOut();

        } else if (id == R.id.nav_login) {

            Intent intent = new Intent(this, User_Account.class);
            String message = "accLogin";
            intent.putExtra("STRING_I_NEED", message);
            startActivity(intent);


        }else if (id == R.id.nav_resetpass) {

            Intent intent = new Intent(this, User_Account.class);
            String message = "resetpass";
            intent.putExtra("STRING_I_NEED", message);
            startActivity(intent);


        } else if (id == R.id.nav_help) {

            showTutorial();

        } else if (id == R.id.nav_language) {

            spinnerDialog.showSpinerDialog();

        } else if (id == R.id.more_apps) {

            viewMoreApps("gibatekpro");

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return fragments[position];
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }
    }

    //<editor-fold desc="Droid Appliance">
    //Droid Appliance

    private void signOut() {

        User_Account.signOut();

        Snackbar.make(findViewById(R.id.main_content), "Sign out Successful", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        showloggedOutUI();

        //updateUI(null);

    }

    private void showloggedInUI() {

        //START Nav menu items inflation
        nav_Menu.findItem(R.id.nav_resetpass).setEnabled(true);
        nav_Menu.findItem(R.id.nav_logoff).setEnabled(true);
        nav_Menu.findItem(R.id.nav_login).setEnabled(false);
        nav_Menu.findItem(R.id.nav_account).setEnabled(false);

        Snackbar.make(findViewById(R.id.main_activity), "Signed in as " + User_Account.getEmail(), Snackbar.LENGTH_LONG)
                .show();

        TextView sEmail = hView.findViewById(R.id.no);

        sEmail.setText(User_Account.getEmail());

    }

    private void showloggedOutUI() {

        //START Nav menu items inflation
        nav_Menu.findItem(R.id.nav_resetpass).setEnabled(false);
        nav_Menu.findItem(R.id.nav_logoff).setEnabled(false);
        nav_Menu.findItem(R.id.nav_login).setEnabled(true);
        nav_Menu.findItem(R.id.nav_account).setEnabled(true);

        TextView sEmail = hView.findViewById(R.id.no);

        sEmail.setText("powered by Droid Appliance");

    }

    //Droid Appliance//
    //</editor-fold>

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case reqcode: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    try {
                        fetchImei();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @SuppressLint("NewApi")
    private void fetchImei() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                showimei();
            } else {

                String[] per = {Manifest.permission.READ_PHONE_STATE};
                requestPermissions(per, reqcode);

            }

        }

    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    public void showimei() {

        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            imeiSIM1 = tm.getImei(0);
            imeiSIM2 = tm.getImei(1);
            DeviceName = getDeviceName();
        } else {
            try {
                imeiSIM1 = tm.getDeviceId();
                imeiSIM2 = tm.getDeviceId();
                DeviceName = getDeviceName();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = MainActivity.this.getLayoutInflater().inflate(R.layout.device_info_layout, null);
        imei1 = mView.findViewById(R.id.imei1);
        imei1_tag = mView.findViewById(R.id.imei1_tag);
        imei2 = mView.findViewById(R.id.imei2);
        imei2_tag = mView.findViewById(R.id.imei2_tag);
        copy1 = mView.findViewById(R.id.copy1);
        copy2 = mView.findViewById(R.id.copy2);
        device_name = mView.findViewById(R.id.device_name);

        device_name.setText(DeviceName);

        if (imeiSIM1 != null) {

            imei1_tag.setVisibility(View.VISIBLE);
            imei1.setVisibility(View.VISIBLE);
            copy1.setVisibility(View.VISIBLE);

            imei1_tag.setText(getResources().getString(R.string.imei_1));
            imei1.setText(imeiSIM1);

            copy1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipData clipData = ClipData.newPlainText("imei 1", imeiSIM1);
                    clipboardManager.setPrimaryClip(clipData);

                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.main_activity), getString(R.string.copied_to_clipboard), Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            });
        } else {
            imei1_tag.setVisibility(View.GONE);
            imei1.setVisibility(View.GONE);
            copy1.setVisibility(View.GONE);
        }

        if (imeiSIM2 != null) {

            imei2_tag.setVisibility(View.VISIBLE);
            imei2.setVisibility(View.VISIBLE);
            copy2.setVisibility(View.VISIBLE);

            imei2_tag.setText(getResources().getString(R.string.imei_2));
            imei2.setText(imeiSIM2);

            copy2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipData clipData = ClipData.newPlainText("imei 2", imeiSIM2);
                    clipboardManager.setPrimaryClip(clipData);

                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.main_activity), getString(R.string.copied_to_clipboard), Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            });
        } else {
            imei2_tag.setVisibility(View.GONE);
            imei2.setVisibility(View.GONE);
            copy2.setVisibility(View.GONE);
        }

        mBuilder.setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();

        try {
            fetchLoadAds();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }

    //[START App Rate Function]
    public void rate() {

        final RatingDialog ratingDialog = new RatingDialog.Builder(this)
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
                .playstoreUrl("https://play.google.com/store/apps/details?id=com.gibatekpro.imeiinfo")
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

    //locale
    private void chooseLocale(int index) {

        switch (index) {

            case 0:
                changeLocale("en");
                break;
            case 1:
                changeLocale("fr");
                break;
            case 2:
                changeLocale("it");
                break;
            case 3:
                changeLocale("ru");
                break;
            case 4:
                changeLocale("es");
                break;
            case 5:
                changeLocale("tr");
                break;
            default:
                changeLocale("en");
                break;
        }

    }

    //locale
    private void changeLocale(String mlocale) {

        Locale locale = new Locale(mlocale);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        recreate();

    }

    //Help function
    private void helpFunction() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragmentList) {

            try {
                int current = mViewPager.getCurrentItem();
                if (fragment.isVisible()) {
                    //spotsDialog.dismiss();
                    if (fragment instanceof CustomImei_Tab && current == 0) {
                        ((CustomImei_Tab) fragment).custom_tutorial();
                        break;
                    } else if (fragment instanceof PhoneImei_Tab && current == 1) {
                        if (((PhoneImei_Tab) fragment).phoneRecyclerview.getVisibility() == View.VISIBLE) {
                            ((PhoneImei_Tab) fragment).specific_tutorial();
                        } else {
                        }
                        break;
                    }else if (fragment instanceof Rate_Tab && current == 4) {
                        ((Rate_Tab) fragment).rate_tutorial();
                        break;
                    } else if (fragment instanceof Analyzer_Tab && current == 2) {
                        ((Analyzer_Tab) fragment).analyzer_tutorial();
                        break;
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    //Show tutorial on Start
    private void checkNew() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean showTutorioals = sharedPreferences.getBoolean("ONBOARDINGIMEIINFO", true);

        if (showTutorioals) {

            showTutorial();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("ONBOARDINGIMEIINFO", false);
            editor.apply();

        }

    }

    private void showTutorial() {


        TapTargetSequence sequence = new TapTargetSequence(this)
                .targets(
                        TapTarget.forToolbarMenuItem(toolbar, R.id.action_language, getString(R.string.change_tuts),
                                getString(R.string.changes_tut)).outerCircleColor(R.color.colorPrimaryDark)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                                .targetCircleColor(R.color.white2)   // Specify a color for the target circle
                                .titleTextSize(30)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.colorAccent)      // Specify the color of the title text
                                .descriptionTextSize(18)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.white2)  // Specify the color of the description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(true)                   // Whether to tint the target view's color
                                .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                                .targetRadius(24),                  // Specify the target radius (in dp),
                        TapTarget.forToolbarMenuItem(toolbar, R.id.action_help, getString(R.string.learn_tut),
                                getString(R.string.learn_tuts)).outerCircleColor(R.color.colorPrimaryDark)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                                .targetCircleColor(R.color.white2)   // Specify a color for the target circle
                                .titleTextSize(30)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.colorAccent)      // Specify the color of the title text
                                .descriptionTextSize(18)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.white2)  // Specify the color of the description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(true)                   // Whether to tint the target view's color
                                .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                                .targetRadius(24),                  // Specify the target radius (in dp)
                        TapTarget.forToolbarNavigationIcon(toolbar, getString(R.string.manymore_tut),
                                getString(R.string.manymore_tuto))
                                .outerCircleColor(R.color.colorPrimaryDark)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                                .targetCircleColor(R.color.white2)   // Specify a color for the target circle
                                .titleTextSize(30)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.colorAccent)      // Specify the color of the title text
                                .descriptionTextSize(18)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.white2)  // Specify the color of the description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(true)                   // Whether to tint the target view's color
                                .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                                .targetRadius(30))                  // Specify the target radius (in dp)
                .listener(new TapTargetSequence.Listener() {
                    // This listener will tell us when interesting(tm) events happen in regards
                    // to the sequence
                    @Override
                    public void onSequenceFinish() {
                        // Yay
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        // Boo
                    }
                });
        sequence.start();

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

    //[START Share function]
    public void shareFunction() {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, R.string.app_name + "\n" + "https://play.google.com/store/apps/details?id=" + APP_PACKAGE_NAME);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share)));

    }
    //[END Share function]

    public void fetchLoadAds() {

        if (fetchInterstitialAd.isLoaded()) {
            fetchInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

    }

    @Override
    public void onBackPressed() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragmentList) {

            try {
                int current = mViewPager.getCurrentItem();
                if (fragment.isVisible()) {
                    //spotsDialog.dismiss();
                    if (fragment instanceof CustomImei_Tab && current == 0) {
                        exitApp();
                        break;
                    } else if (fragment instanceof PhoneImei_Tab && current == 1) {
                        if (((PhoneImei_Tab) fragment).imeiRecyclerview.getVisibility() == View.VISIBLE) {

                            //visibility options
                            ((PhoneImei_Tab) fragment).phoneRecyclerview.setVisibility(View.VISIBLE);
                            ((PhoneImei_Tab) fragment).selectLay.setVisibility(View.VISIBLE);
                            ((PhoneImei_Tab) fragment).selectedLay.setVisibility(View.GONE);
                            ((PhoneImei_Tab) fragment).imeiRecyclerview.setVisibility(View.GONE);
                        } else {

                            exitApp();
                        }
                        break;
                    } else if (fragment instanceof Rate_Tab && current == 3) {
                        exitApp();
                        break;
                    } else if (fragment instanceof Analyzer_Tab && current == 2) {
                        exitApp();
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    //Exit App Function
    public void exitApp(){
        Snackbar.make(findViewById(R.id.main_activity), "Exit App?", Snackbar.LENGTH_LONG)
                .setAction("Yes", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finishAffinity();
                        System.exit(0);
                    }
                })
                .show();
    }



}