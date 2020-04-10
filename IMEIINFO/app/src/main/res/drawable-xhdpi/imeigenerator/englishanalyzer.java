package com.gibatekpro.imeigenerator;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;

/**
 * Created by Anthony Gibah on 6/11/2017.
 */
public class englishanalyzer extends AppCompatActivity {
    private Button check, clear, imei2;
    private Toolbar toolbar;
    public TextView corrimeiv, tacv, rbiv, mebv, facv, snv, ldv, redv, greenv;
    public EditText inss;
    private InterstitialAd mInterstitialAd;
    private TelephonyManager telephonyManager;


    private static int TELEPHONE = 23;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_englishanalyzer);

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        NativeExpressAdView adView = (NativeExpressAdView) findViewById(R.id.adView);
        adView.loadAd(new AdRequest.Builder().build());

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1488497497647050/2576755524");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });


        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        inss = (EditText) findViewById(R.id.inputimei);
        corrimeiv = (TextView) findViewById(R.id.corrimei);
        tacv = (TextView) findViewById(R.id.tac);
        rbiv = (TextView) findViewById(R.id.rbi);
        mebv = (TextView) findViewById(R.id.meb);
        facv = (TextView) findViewById(R.id.fac);
        snv = (TextView) findViewById(R.id.sn);
        ldv = (TextView) findViewById(R.id.ld);
        greenv = (TextView) findViewById(R.id.green);
        redv = (TextView) findViewById(R.id.red);
        check = (Button) findViewById(R.id.check);
        clear = (Button) findViewById(R.id.clear);
        imei2 = (Button) findViewById(R.id.imei2);

        corrimeiv.setText("Correct IMEI:");
        tacv.setText("Type Allocation Code:");
        rbiv.setText("Reporting Body Identifier:");
        mebv.setText("Mobile Equipment Type:");
        facv.setText("Final Assembly Code:");
        snv.setText("Serial Number:");
        ldv.setText("Luhn Digit:");
       // instruct.setTextColor(Color.parseColor("#FFFFFF"));
        //instruct.setText("INPUT 14 OR 15 DIGITS BELOW");

        imei2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                askForPermission(Manifest.permission.READ_PHONE_STATE, TELEPHONE);
            }
        });


        clear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                corrimeiv.setText("Correct IMEI:");
                tacv.setText("Type Allocation Code:");
                rbiv.setText("Reporting Body Identifier:");
                mebv.setText("Mobile Equipment Type:");
                facv.setText("Final Assembly Code:");
                snv.setText("Serial Number:");
                ldv.setText("Luhn Digit:");
                redv.setTextColor(Color.parseColor("#D50000"));
                redv.setText("");
                greenv.setTextColor(Color.parseColor("#00C853"));
                greenv.setText("");
                inss.setText("");
                //instruct.setTextColor(Color.parseColor("#FFFFFF"));
                //instruct.setText("INPUT 14 OR 15 DIGITS BELOW");
            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                String ins = inss.getText().toString();
                int count = 0;
                com.gibatekpro.imeigenerator.Digseperator fifdig = new com.gibatekpro.imeigenerator.Digseperator(ins);
                com.gibatekpro.imeigenerator.Digseperator checks = new com.gibatekpro.imeigenerator.Digseperator(ins);
                com.gibatekpro.imeigenerator.Digseperator bac = new com.gibatekpro.imeigenerator.Digseperator(ins);
                com.gibatekpro.imeigenerator.analyst tester = new com.gibatekpro.imeigenerator.analyst(ins);
                com.gibatekpro.imeigenerator.Iffour tester2 = new com.gibatekpro.imeigenerator.Iffour(ins);
                for (int i = 0; i < ins.length(); i++) {
                    if (ins.charAt(i) != ' ') {
                        ++count;
                        //instruct.setTextColor(Color.parseColor("#FFFFFF"));
                        //instruct.setText("DIGITS ENTERED = " + count);
                    }
                }

                if (count < 14 || count > 15) {
                    redv.setTextColor(Color.parseColor("#D50000"));
                    redv.setText("INVALID INPUT!!!!!!!!");
                    greenv.setTextColor(Color.parseColor("#D50000"));
                    greenv.setText("ENTER 14 or 15 DIGITS");
                    corrimeiv.setText("Correct IMEI:");
                    tacv.setText("Type Allocation Code:");
                    rbiv.setText("Reporting Body Identifier:");
                    mebv.setText("Mobile Equipment Type:");
                    facv.setText("Final Assembly Code:");
                    snv.setText("Serial Number:");
                    ldv.setText("Luhn Digit:");
                }
                if (count == 15) {
                    if (fifdig.Sepfifdig() == checks.checkluhn()) {
                        redv.setTextColor(Color.parseColor("#00C853"));
                        redv.setText("IMEI IS VALID!!");
                        greenv.setTextColor(Color.parseColor("#00C853"));
                        greenv.setText("VIEW ANALYSIS BELOW!!");
                        corrimeiv.setText("Correct IMEI:\n" + (bac.newnum14()));
                        tacv.setText("Type Allocation Code:\n" + tester.tac());
                        rbiv.setText("Reporting Body Identifier:\n" + tester.rbi());
                        mebv.setText("Mobile Equipment Type:\n" + tester.meb());
                        facv.setText("Final Assembly Code:\n" + tester.fac());
                        snv.setText("Serial Number:\n" + tester.sn());
                        //cdv.setText(tester.cd());
                        ldv.setText("Luhn Digit:\n" + tester.ld());
                    }
                    if (fifdig.Sepfifdig() != checks.checkluhn()) {
                        redv.setTextColor(Color.parseColor("#D50000"));
                        redv.setText("IMEI IS INCORRECT!!!!!!!!");
                        greenv.setTextColor(Color.parseColor("#00C853"));
                        greenv.setText("VIEW CORRECT IMEI BELOW!!!");
                        corrimeiv.setText("Correct IMEI:\n" + (bac.newnum14()));
                        tacv.setText("Type Allocation Code:\n" + tester.tac());
                        rbiv.setText("Reporting Body Identifier:\n" + tester.rbi());
                        mebv.setText("Mobile Equipment Type:\n" + tester.meb());
                        facv.setText("Final Assembly Code:\n" + tester.fac());
                        snv.setText("Serial Number:\n" + tester.sn());
                        //cdv.setText(tester.cd());
                        ldv.setText("Luhn Digit:\n" + tester.ld());
                    }
                }
                if (count == 14) {
                    redv.setTextColor(Color.parseColor("#D50000"));
                    redv.setText("IMEI IS NOT COMPLETE!!!!!!!!");
                    greenv.setTextColor(Color.parseColor("#00C853"));
                    greenv.setText("VIEW COMPLETE IMEI BELOW!!!");
                    corrimeiv.setText("Correct IMEI:\n" + (tester2.iffournew()));
                    tacv.setText("Type Allocation Code:\n" + tester.tac());
                    rbiv.setText("Reporting Body Identifier:\n" + tester.rbi());
                    mebv.setText("Mobile Equipment Type:\n" + tester.meb());
                    facv.setText("Final Assembly Code:\n" + tester.fac());
                    snv.setText("Serial Number:\n" + tester.sn());
                    //cdv.setText(tester.cd());
                    ldv.setText("Luhn Digit:\n" + tester2.ld2());
                }

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.analyze_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.home) {
            startActivity(new Intent(this, com.gibatekpro.imeigenerator.englishgen.class));
        }

        return super.onOptionsItemSelected(item);
    }


    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(com.gibatekpro.imeigenerator.englishanalyzer.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(com.gibatekpro.imeigenerator.englishanalyzer.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(com.gibatekpro.imeigenerator.englishanalyzer.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(com.gibatekpro.imeigenerator.englishanalyzer.this, new String[]{permission}, requestCode);
            }
        } else {
            if (telephonyManager == null || telephonyManager.getDeviceId() == null) {
                redv.setTextColor(Color.parseColor("#D50000"));
                redv.setText("SORRY!!!!!!!");
                greenv.setTextColor(Color.parseColor("#D50000"));
                greenv.setText("UNABLE TO FETCH IMEI");
            } else {
                int count = 0;
                String mine = telephonyManager.getDeviceId();
                for (int i = 0; i < mine.length(); i++) {
                    if (mine.charAt(i) != ' ') {
                        ++count;
                    }
                }
                if (count == 15) {
                    com.gibatekpro.imeigenerator.Digseperator fif = new com.gibatekpro.imeigenerator.Digseperator(mine);
                    com.gibatekpro.imeigenerator.analyst test = new com.gibatekpro.imeigenerator.analyst(mine);
                    redv.setTextColor(Color.parseColor("#00C853"));
                    redv.setText("THIS PHONE'S IMEI ANALYSIS");
                    greenv.setTextColor(Color.parseColor("#00C853"));
                    greenv.setText("IS DISPLAYED BELOW!!");
                    corrimeiv.setText("THIS PHONE'S IMEI:\n" + mine);
                    tacv.setText("Type Allocation Code:\n" + test.tac());
                    rbiv.setText("Reporting Body Identifier:\n" + test.rbi());
                    mebv.setText("Mobile Equipment Type:\n" + test.meb());
                    facv.setText("Final Assembly Code:\n" + test.fac());
                    snv.setText("Serial Number:\n" + test.sn());
                    ldv.setText("Luhn Digit:\n" + test.ld());
                }
                if (count == 14) {
                    com.gibatekpro.imeigenerator.analyst tester = new com.gibatekpro.imeigenerator.analyst(mine);
                    com.gibatekpro.imeigenerator.Iffour tester2 = new com.gibatekpro.imeigenerator.Iffour(mine);
                    redv.setTextColor(Color.parseColor("#00C853"));
                    redv.setText("THIS PHONE'S IMEI ANALYSIS");
                    greenv.setTextColor(Color.parseColor("#00C853"));
                    greenv.setText("IS DISPLAYED BELOW!!");
                    corrimeiv.setText("Correct IMEI:\n" + (tester2.iffournew()));
                    tacv.setText("Type Allocation Code:\n" + tester.tac());
                    rbiv.setText("Reporting Body Identifier:\n" + tester.rbi());
                    mebv.setText("Mobile Equipment Type:\n" + tester.meb());
                    facv.setText("Final Assembly Code:\n" + tester.fac());
                    snv.setText("Serial Number:\n" + tester.sn());
                    //cdv.setText(tester.cd());
                    ldv.setText("Luhn Digit:\n" + tester2.ld2());
                }
                if (count < 14 || count > 15){

                    corrimeiv.setText("THIS PHONE'S IMEI:\n" + mine);
                    tacv.setText("Type Allocation Code:");
                    rbiv.setText("Reporting Body Identifier:");
                    mebv.setText("Mobile Equipment Type:");
                    facv.setText("Final Assembly Code:");
                    snv.setText("Serial Number:");
                    ldv.setText("Luhn Digit:");
                }
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == TELEPHONE) {
                if (telephonyManager == null || telephonyManager.getDeviceId() == null) {
                    redv.setTextColor(Color.parseColor("#D50000"));
                    redv.setText("SORRY!!!!!!!");
                    greenv.setTextColor(Color.parseColor("#D50000"));
                    greenv.setText("UNABLE TO FETCH IMEI");
                } else {
                    int count = 0;
                    String mine = telephonyManager.getDeviceId();
                    for (int i = 0; i < mine.length(); i++) {
                        if (mine.charAt(i) != ' ') {
                            ++count;
                        }
                    }
                    if (count == 15) {
                        com.gibatekpro.imeigenerator.Digseperator fif = new com.gibatekpro.imeigenerator.Digseperator(mine);
                        com.gibatekpro.imeigenerator.analyst test = new com.gibatekpro.imeigenerator.analyst(mine);
                        redv.setTextColor(Color.parseColor("#00C853"));
                        redv.setText("THIS PHONE'S IMEI ANALYSIS");
                        greenv.setTextColor(Color.parseColor("#00C853"));
                        greenv.setText("IS DISPLAYED BELOW!!");
                        corrimeiv.setText("THIS PHONE'S IMEI:\n" + mine);
                        tacv.setText("Type Allocation Code:\n" + test.tac());
                        rbiv.setText("Reporting Body Identifier:\n" + test.rbi());
                        mebv.setText("Mobile Equipment Type:\n" + test.meb());
                        facv.setText("Final Assembly Code:\n" + test.fac());
                        snv.setText("Serial Number:\n" + test.sn());
                        ldv.setText("Luhn Digit:\n" + test.ld());
                    }
                    if (count == 14) {
                        com.gibatekpro.imeigenerator.analyst tester = new com.gibatekpro.imeigenerator.analyst(mine);
                        com.gibatekpro.imeigenerator.Iffour tester2 = new com.gibatekpro.imeigenerator.Iffour(mine);
                        redv.setTextColor(Color.parseColor("#00C853"));
                        redv.setText("THIS PHONE'S IMEI ANALYSIS");
                        greenv.setTextColor(Color.parseColor("#00C853"));
                        greenv.setText("IS DISPLAYED BELOW!!");
                        corrimeiv.setText("Correct IMEI:\n" + (tester2.iffournew()));
                        tacv.setText("Type Allocation Code:\n" + tester.tac());
                        rbiv.setText("Reporting Body Identifier:\n" + tester.rbi());
                        mebv.setText("Mobile Equipment Type:\n" + tester.meb());
                        facv.setText("Final Assembly Code:\n" + tester.fac());
                        snv.setText("Serial Number:\n" + tester.sn());
                        //cdv.setText(tester.cd());
                        ldv.setText("Luhn Digit:\n" + tester2.ld2());
                    }
                    if (count < 14 || count > 15){

                        corrimeiv.setText("THIS PHONE'S IMEI:\n" + mine);
                        tacv.setText("Type Allocation Code:");
                        rbiv.setText("Reporting Body Identifier:");
                        mebv.setText("Mobile Equipment Type:");
                        facv.setText("Final Assembly Code:");
                        snv.setText("Serial Number:");
                        ldv.setText("Luhn Digit:");
                    }
                }

                /*for (int i = 1; i < 5; i++) {
                    loadInterstitial();
                    showInterstitial();
                }*/


            }

            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }


}

