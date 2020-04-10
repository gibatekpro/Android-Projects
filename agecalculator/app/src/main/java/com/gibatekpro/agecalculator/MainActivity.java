package com.gibatekpro.agecalculator;


import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import me.grantland.widget.AutofitTextView;

import static com.gibatekpro.agecalculator.AesCbcWithIntegrity.generateKeyFromPassword;
import static java.lang.Long.parseLong;

public class MainActivity extends AppCompatActivity {
    DatePicker date;
    TimePicker time;

    private static String YEARS = "YEARS";
    private static String MONTHS = "MONTHS";
    private static String WEEKS = "WEEKS";
    private static String DAYS = "DAYS";
    private static String HOURS = "HOURS";
    private static String MINUTES = "MINUTES";
    private static String SECONDS = "SECONDS";

    private InterstitialAd m_InterstitialAd;

    //runs without timer be reposting self
    Handler timer = new Handler();
    Runnable run = new Runnable() {

        @Override
        public void run() {
            getTime();

            timer.postDelayed(this, 300);
        }
    };
    private boolean isRunning = false;
    private boolean running_before_pause=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        money();

        if (!BuildConfig.DEBUG) {
            if (isDebuggable(getApplicationContext())) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }

            itsMyApp();
        }
        setContentView(R.layout.activity_main);

        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        //toolbar
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //56setSupportActionBar(toolbar);

        date = (DatePicker) findViewById(R.id.birth_date);
        time = (TimePicker) findViewById(R.id.birth_time);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        date.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                startTimer();
            }
        });

        time.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                startTimer();
            }
        });

        showAlertDialog();
    }

    private void showAlertDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.help, null);
        mBuilder.setTitle(R.string.app_name);
        TextView details = (TextView) mView.findViewById(R.id.text);
        details.setText("Use the time and date slider to select the appropriate date and time.\nThis simple app will help calculate the difference between the date imputed and the current date down to the last second.\n Press 'OKAY' to remove this dialog.");

        mBuilder.setPositiveButton("okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showInterstitial();
                dialogInterface.dismiss();
            }
        });
        mBuilder.setNegativeButton("BACK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showInterstitial();
                dialogInterface.dismiss();
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.setCancelable(false).create();
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected void onPause() {
        if(isRunning){
            running_before_pause=true;
        }else{
            running_before_pause=false;
        }
        stopTimer();
        super.onPause();
    }

    @Override
    protected void onResume() {
        if(running_before_pause){
            startTimer();
            running_before_pause=false;
        }
        super.onResume();
    }

    private void startTimer() {
        if (!isRunning) {
            getTime();
            timer.postDelayed(run, 1000);
            isRunning = true;
        }
    }

    private void stopTimer() {
        timer.removeCallbacks(run);
        isRunning = false;
    }

    private void getTime() {
        AutofitTextView res = (AutofitTextView) findViewById(R.id.res_yr);
        AutofitTextView res_mnth = (AutofitTextView) findViewById(R.id.res_mnth);
        AutofitTextView res_wk = (AutofitTextView) findViewById(R.id.res_wk);
        AutofitTextView res_day = (AutofitTextView) findViewById(R.id.res_day);
        AutofitTextView res_hr = (AutofitTextView) findViewById(R.id.res_hr);
        AutofitTextView res_min = (AutofitTextView) findViewById(R.id.res_min);
        AutofitTextView res_sec = (AutofitTextView) findViewById(R.id.res_sec);
        TextView com = (TextView) findViewById(R.id.com);
        Calendar calendar = Calendar.getInstance();
        calendar.set(date.getYear(), date.getMonth(), date.getDayOfMonth(),
                time.getCurrentHour(), time.getCurrentMinute(), 0);
        if (calendar.getTimeInMillis() > System.currentTimeMillis()) {
            com.setText("Are you from the future?");
            res.setText("");
            res_mnth.setText("");
            res_wk.setText("");
            res_day.setText("");
            res_hr.setText("");
            res_min.setText("");
            res_sec.setText("");
            return;
        } else if (calendar.getTimeInMillis() > (System.currentTimeMillis() - (DateUtils.YEAR_IN_MILLIS) * 3)) {
            com.setText("Welcome to our world!!");
        } else if (calendar.getTimeInMillis() < (System.currentTimeMillis() - (DateUtils.YEAR_IN_MILLIS) * 13) && calendar.getTimeInMillis() > (System.currentTimeMillis() - (DateUtils.YEAR_IN_MILLIS) * 20)) {
            com.setText("A teenager");
        }else if (calendar.getTimeInMillis() < (System.currentTimeMillis() - (DateUtils.YEAR_IN_MILLIS) * 20) && calendar.getTimeInMillis() > (System.currentTimeMillis() - (DateUtils.YEAR_IN_MILLIS) * 49)) {
            com.setText("Adulthood is peak");
        } else if (calendar.getTimeInMillis() < (System.currentTimeMillis() - (DateUtils.YEAR_IN_MILLIS) * 50) && calendar.getTimeInMillis() > (System.currentTimeMillis() - (DateUtils.YEAR_IN_MILLIS) * 60)) {
            com.setText("Quinquagenarian \\o/");
        } else if (calendar.getTimeInMillis() < (System.currentTimeMillis() - (DateUtils.YEAR_IN_MILLIS) * 60) && calendar.getTimeInMillis() > (System.currentTimeMillis() - (DateUtils.YEAR_IN_MILLIS) * 70)) {
            com.setText("A Sexagenarian in our midst");
        } else if (calendar.getTimeInMillis() < (System.currentTimeMillis() - DateUtils.YEAR_IN_MILLIS * 100)) {
            com.setText("Woah! A centenarian!!");
        } else {
            com.setText("");
        }
        long startTime = calendar.getTimeInMillis();
        long currentTime = System.currentTimeMillis();
        //years
        Map<String, String> time = convertToyears(currentTime - startTime);
        String text = "";

        if (parseLong(time.get(YEARS)) > 0)
            text += NumberFormat.getInstance().format(parseLong(time.get(YEARS))) + (parseLong(time.get(YEARS)) == 1 ? "year " : "years ");
        if (parseLong(time.get(MONTHS)) > 0)
            text += time.get(MONTHS) + (parseLong(time.get(MONTHS)) == 1 ? "month " : "months ");
        if (parseLong(time.get(WEEKS)) > 0)
            text += time.get(WEEKS) + (parseLong(time.get(WEEKS)) == 1 ? "week " : "weeks ");

        if (parseLong(time.get(DAYS)) > 0)
            text += parseLong(time.get(DAYS)) + (parseLong(time.get(DAYS)) == 1 ? "day " : "days ");

        if (parseLong(time.get(HOURS)) > 0)
            text += time.get(HOURS) + (parseLong(time.get(HOURS)) == 1 ? "hour " : "hours ");
        if (parseLong(time.get(MINUTES)) > 0)
            text += time.get(MINUTES) + (parseLong(time.get(MINUTES)) == 1 ? "minute " : "minutes ");
        if (parseLong(time.get(SECONDS)) > 0)
            text += time.get(SECONDS) + (parseLong(time.get(SECONDS)) == 1 ? "second " : "seconds");

        res.setText(text);

        //months
        time = convertToMonths(currentTime - startTime);
        text = "";

        if (parseLong(time.get(MONTHS)) > 0)
            text += NumberFormat.getInstance().format(parseLong(time.get(MONTHS))) + (parseLong(time.get(MONTHS)) == 1 ? "month " : "months ");
        if (parseLong(time.get(WEEKS)) > 0)
            text += time.get(WEEKS) + (parseLong(time.get(WEEKS)) == 1 ? "week " : "weeks ");

        if (parseLong(time.get(DAYS)) > 0)
            text += time.get(DAYS) + (parseLong(time.get(DAYS)) == 1 ? "day " : "days ");

        if (parseLong(time.get(HOURS)) > 0)
            text += time.get(HOURS) + (parseLong(time.get(HOURS)) == 1 ? "hour " : "hours ");
        if (parseLong(time.get(MINUTES)) > 0)
            text += time.get(MINUTES) + (parseLong(time.get(MINUTES)) == 1 ? "minute " : "minutes ");
        if (parseLong(time.get(SECONDS)) > 0)
            text += time.get(SECONDS) + (parseLong(time.get(SECONDS)) == 1 ? "second " : "seconds");

        res_mnth.setText(text);

        //weeks
        time = convertToWeeks(currentTime - startTime);
        text = "";

        if (parseLong(time.get(WEEKS)) > 0)
            text += NumberFormat.getInstance().format(parseLong(time.get(WEEKS))) + (parseLong(time.get(WEEKS)) == 1 ? "week " : "weeks ");

        if (parseLong(time.get(DAYS)) > 0)
            text += time.get(DAYS) + (parseLong(time.get(DAYS)) == 1 ? "day " : "days ");

        if (parseLong(time.get(HOURS)) > 0)
            text += time.get(HOURS) + (parseLong(time.get(HOURS)) == 1 ? "hour " : "hours ");
        if (parseLong(time.get(MINUTES)) > 0)
            text += time.get(MINUTES) + (parseLong(time.get(MINUTES)) == 1 ? "minute " : "minutes ");
        if (parseLong(time.get(SECONDS)) > 0)
            text += time.get(SECONDS) + (parseLong(time.get(SECONDS)) == 1 ? "second " : "seconds");

        res_wk.setText(text);

        //days
        time = convertToDays(currentTime - startTime);
        text = "";

        if (parseLong(time.get(DAYS)) > 0)
            text += NumberFormat.getInstance().format(parseLong(time.get(DAYS))) + (parseLong(time.get(DAYS)) == 1 ? "day " : "days ");

        if (parseLong(time.get(HOURS)) > 0)
            text += time.get(HOURS) + (parseLong(time.get(HOURS)) == 1 ? "hour " : "hours ");
        if (parseLong(time.get(MINUTES)) > 0)
            text += time.get(MINUTES) + (parseLong(time.get(MINUTES)) == 1 ? "minute " : "minutes ");
        if (parseLong(time.get(SECONDS)) > 0)
            text += time.get(SECONDS) + (parseLong(time.get(SECONDS)) == 1 ? "second " : "seconds");

        res_day.setText(text);

        //hours
        time = convertToHours(currentTime - startTime);
        text = "";

        if (parseLong(time.get(HOURS)) > 0)
            text += NumberFormat.getInstance().format(parseLong(time.get(HOURS))) + (parseLong(time.get(HOURS)) == 1 ? "hour " : "hours ");
        if (parseLong(time.get(MINUTES)) > 0)
            text += time.get(MINUTES) + (parseLong(time.get(MINUTES)) == 1 ? "minute " : "minutes ");
        if (parseLong(time.get(SECONDS)) > 0)
            text += time.get(SECONDS) + (parseLong(time.get(SECONDS)) == 1 ? "second " : "seconds");

        res_hr.setText(text);

        //minutes
        time = convertToMinutes(currentTime - startTime);
        text = "";

        if (parseLong(time.get(MINUTES)) > 0)
            text += NumberFormat.getInstance().format(parseLong(time.get(MINUTES))) + (parseLong(time.get(MINUTES)) == 1 ? "minute " : "minutes ");
        if (parseLong(time.get(SECONDS)) > 0)
            text += time.get(SECONDS) + (parseLong(time.get(SECONDS)) == 1 ? "second " : "seconds");

        res_min.setText(text);

        //seconds
        time = convertToSeconds(currentTime - startTime);
        text = "";

        if (parseLong(time.get(SECONDS)) > 0)
            text += NumberFormat.getInstance().format(parseLong(time.get(SECONDS))) + (parseLong(time.get(SECONDS)) == 1 ? "second " : "seconds");

        res_sec.setText(text);
//        AutoResizeAutofitTextView atext = (AutoResizeAutofitTextView) findViewById(R.id.a_res_yr);
//        atext.setText(text);
    }

    private Map<String, String> convertToyears(long ms) {
        String time = "";
        Map<String, String> result = new HashMap<>();

        long Days = TimeUnit.MILLISECONDS.toDays(ms);
        ms -= TimeUnit.DAYS.toMillis(Days);

        result.put(YEARS, (Days / 365) + "");
        Days = Days % 365;
        result.put(MONTHS, (Days / 30 > 11 ? 0 : Days / 30) + "");
        Days = Days % 30;
        result.put(WEEKS, (Days / 7 > 4 ? 0 : Days / 7) + "");
        Days = Days % 7;
        result.put(DAYS, Days + "");
        long hours = TimeUnit.MILLISECONDS.toHours(ms);
        result.put(HOURS, hours + "");
        ms -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(ms);
        result.put(MINUTES, minutes + "");
        ms -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(ms);
        result.put(SECONDS, seconds + "");

        return result;
    }

    private Map<String, String> convertToMonths(long ms) {
        String time = "";
        Map<String, String> result = new HashMap<>();

        long Days = TimeUnit.MILLISECONDS.toDays(ms);
        ms -= TimeUnit.DAYS.toMillis(Days);

        result.put(MONTHS, (Days / 30) + "");
        Days = Days % 30;
        result.put(WEEKS, (Days / 7 > 4 ? 0 : Days / 7) + "");
        Days = Days % 7;
        result.put(DAYS, Days + "");
        long hours = TimeUnit.MILLISECONDS.toHours(ms);
        result.put(HOURS, hours + "");
        ms -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(ms);
        result.put(MINUTES, minutes + "");
        ms -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(ms);
        result.put(SECONDS, seconds + "");

        return result;
    }

    private Map<String, String> convertToWeeks(long ms) {
        String time = "";
        Map<String, String> result = new HashMap<>();

        long Days = TimeUnit.MILLISECONDS.toDays(ms);
        ms -= TimeUnit.DAYS.toMillis(Days);

        result.put(WEEKS, (Days / 7) + "");
        Days = Days % 7;
        result.put(DAYS, Days + "");
        long hours = TimeUnit.MILLISECONDS.toHours(ms);
        result.put(HOURS, hours + "");
        ms -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(ms);
        result.put(MINUTES, minutes + "");
        ms -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(ms);
        result.put(SECONDS, seconds + "");

        return result;
    }

    private Map<String, String> convertToDays(long ms) {
        String time = "";
        Map<String, String> result = new HashMap<>();

        long Days = TimeUnit.MILLISECONDS.toDays(ms);
        ms -= TimeUnit.DAYS.toMillis(Days);

        result.put(DAYS, Days + "");
        long hours = TimeUnit.MILLISECONDS.toHours(ms);
        result.put(HOURS, hours + "");
        ms -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(ms);
        result.put(MINUTES, minutes + "");
        ms -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(ms);
        result.put(SECONDS, seconds + "");

        return result;
    }

    private Map<String, String> convertToHours(long ms) {
        String time = "";
        Map<String, String> result = new HashMap<>();

        long hours = TimeUnit.MILLISECONDS.toHours(ms);
        result.put(HOURS, hours + "");
        ms -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(ms);
        result.put(MINUTES, minutes + "");
        ms -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(ms);
        result.put(SECONDS, seconds + "");

        return result;
    }

    private Map<String, String> convertToMinutes(long ms) {
        String time = "";
        Map<String, String> result = new HashMap<>();

        long minutes = TimeUnit.MILLISECONDS.toMinutes(ms);
        result.put(MINUTES, minutes + "");
        ms -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(ms);
        result.put(SECONDS, seconds + "");

        return result;
    }

    private Map<String, String> convertToSeconds(long ms) {
        Map<String, String> result = new HashMap<>();

        long seconds = TimeUnit.MILLISECONDS.toSeconds(ms);
        result.put(SECONDS, seconds + "");

        return result;
    }

    private boolean isLeapYear(int year) {
        if (year % 4 != 0) {
            return false;
        } else if (year % 100 != 0) {
            return true;
        } else if (year % 400 != 0) {
            return false;
        } else return true;
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

        if (id == R.id.action_help) {
            showAlertDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void money() {
        // Create the InterstitialAd and set the adUnitId (defined in values/strings.xml).
        m_InterstitialAd = new InterstitialAd(this);
        m_InterstitialAd.setAdUnitId("ca-app-pub-1488497497647050/8151416726");
        m_InterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                //resumeGameTime();
                loadInterstitial();
            }
        });
        loadInterstitial();
    }

    /*
    *Method call for showing Interstitial ads
     */
    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and reload the ad.
        if (m_InterstitialAd != null && m_InterstitialAd.isLoaded()) {
            // pauseGameTime();
            m_InterstitialAd.show();
        } else {
            loadInterstitial();
        }
    }

    private void loadInterstitial() {
        // log("Loading new ad");
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("E9C1DEC2F04C63973F213FD83AAEFEC8").build();/*
                .addTestDevice("@string/test_device_ID")*/
        m_InterstitialAd.loadAd(adRequest);
    }

    private static boolean isDebuggable(Context context) {
        return (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    /**
     * Decrypts an encrypted string value
     *
     * @param text The string to be decrypted
     * @return The decrypted string
     */
    private String decrypt(String text) {
        String decryptedText = "";
        try {
            AesCbcWithIntegrity.CipherTextIvMac cipherTextIvMac = new AesCbcWithIntegrity.CipherTextIvMac(text);

            decryptedText = AesCbcWithIntegrity.decryptString(cipherTextIvMac, generateKeyFromPassword(getAppSignature(getApplicationContext()), getString(R.string.salt)));
            // Log.i(TAG, "Decrypted: " + decryptedText);
        } catch (IllegalArgumentException e) {
            //Log.e(TAG, "IllegalArgumentException", e);
        } catch (GeneralSecurityException e) {
            //Log.e(TAG, "GeneralSecurityException", e);
        } catch (UnsupportedEncodingException e) {
            //Log.e(TAG, "UnsupportedEncodingException", e);
        }
        return decryptedText;
    }

    private String getAppSignature(Context context) {
        String sig = "";
        try {
            Signature[] signatures = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES).signatures;
            sig = signatures[0].toCharsString();
        } catch (PackageManager.NameNotFoundException ex) {

        }
        return sig;
    }

    private void itsMyApp() {

        if (!getAppSignature(getApplicationContext()).equals(getMyString(R.string.sign))) {
            kill();
        }
       /* if (checkAppInstall()) {
            kill();
        }*/
    }

    /**
     * Helper method to decrypt a string from the string resource file
     *
     * @param string The ID of the string to be decrypted
     * @return The decrypted string value (returns an empty string if decrytion fails)
     */
    private String getMyString(int string) {
        String value = "";
        value = decrypt(getString(string));
        return value;
    }

    private void kill() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
