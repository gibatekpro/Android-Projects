package com.gibatekpro.imeigenerator;

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

public class hindianalyzer extends AppCompatActivity {

    public TextView corrimeiv, tacv, rbiv, mebv, facv, snv, ldv, redv, greenv;
    public EditText inss;
    private InterstitialAd mInterstitialAd;
    private TelephonyManager telephonyManager;


    private static int TELEPHONE = 23;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hindianalyzer);

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);


        // englishAppRater.app_launched(this);

        NativeExpressAdView adView = (NativeExpressAdView) findViewById(R.id.adView);
        adView.loadAd(new AdRequest.Builder().build());

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1488497497647050/2576755524");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
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
        Button check = (Button) findViewById(R.id.check);
        Button clear = (Button) findViewById(R.id.clear);
        Button imei2 = (Button) findViewById(R.id.imei2);

        corrimeiv.setText(R.string.hin_correct_imei);
        tacv.setText(R.string.tac);
        rbiv.setText(R.string.hin_rbi);
        mebv.setText(R.string.hin_MET);
        facv.setText(R.string.hin_fac);
        snv.setText(R.string.hin_sn);
        ldv.setText(R.string.hin_ln);

        imei2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                askForPermission(android.Manifest.permission.READ_PHONE_STATE, TELEPHONE);
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
                corrimeiv.setText(R.string.hin_correct_imei);
                tacv.setText(R.string.tac);
                rbiv.setText(R.string.hin_rbi);
                mebv.setText(R.string.hin_MET);
                facv.setText(R.string.hin_fac);
                snv.setText(R.string.hin_sn);
                ldv.setText(R.string.hin_ln);
                redv.setTextColor(Color.parseColor("#D50000"));
                redv.setText("");
                greenv.setTextColor(Color.parseColor("#00C853"));
                greenv.setText("");
                inss.setText("");
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
                analyst tester = new analyst(ins);
                com.gibatekpro.imeigenerator.Iffour tester2 = new com.gibatekpro.imeigenerator.Iffour(ins);
                for (int i = 0; i < ins.length(); i++) {
                    if (ins.charAt(i) != ' ') {
                        ++count;
                    }
                }

                if (count < 14 || count > 15) {
                    redv.setTextColor(Color.parseColor("#D50000"));
                    redv.setText("अमान्य संख्याएं!!!!!!!!");
                    greenv.setTextColor(Color.parseColor("#D50000"));
                    greenv.setText("14 या 15 नंबर लिखें");
                    corrimeiv.setText(R.string.hin_correct_imei);
                    tacv.setText(R.string.tac);
                    rbiv.setText(R.string.hin_rbi);
                    mebv.setText(R.string.hin_MET);
                    facv.setText(R.string.hin_fac);
                    snv.setText(R.string.hin_sn);
                    ldv.setText(R.string.hin_ln);
                }
                if (count == 15) {
                    if (fifdig.Sepfifdig() == checks.checkluhn()) {
                        redv.setTextColor(Color.parseColor("#00C853"));
                        redv.setText("IMEI  मान्य है!!");
                        greenv.setTextColor(Color.parseColor("#00C853"));
                        greenv.setText("नीचे विश्लेषण देखें!!");
                        corrimeiv.setText("सही IMEI:\n" + (bac.newnum14()));
                        tacv.setText("प्रकार आवंटन कोड:\n" + tester.tac());
                        rbiv.setText("शारीरिक पहचानकर्ता सूचित करना:\n" + tester.rbi());
                        mebv.setText("मोबाइल डिवाइस प्रकार:\n" + tester.meb());
                        facv.setText("अंतिम विधानसभा कोड:\n" + tester.fac());
                        snv.setText("क्रमांक:\n" + tester.sn());
                        //cdv.setText(tester.cd());
                        ldv.setText("Luhn संख्या:\n" + tester.ld());
                    }
                    if (fifdig.Sepfifdig() != checks.checkluhn()) {
                        redv.setTextColor(Color.parseColor("#D50000"));
                        redv.setText("IMEI गलत है!!!!!!!!");
                        greenv.setTextColor(Color.parseColor("#00C853"));
                        greenv.setText("नीचे सही IMEI देखें!!!");
                        corrimeiv.setText("सही IMEI:\n" + (bac.newnum14()));
                        tacv.setText("प्रकार आवंटन कोड:\n" + tester.tac());
                        rbiv.setText("शारीरिक पहचानकर्ता सूचित करना:\n" + tester.rbi());
                        mebv.setText("मोबाइल डिवाइस प्रकार:\n" + tester.meb());
                        facv.setText("अंतिम विधानसभा कोड:\n" + tester.fac());
                        snv.setText("क्रमांक:\n" + tester.sn());
                        //cdv.setText(tester.cd());
                        ldv.setText("Luhn संख्या:\n" + tester.ld());
                    }
                }
                if (count == 14) {
                    redv.setTextColor(Color.parseColor("#D50000"));
                    redv.setText("IMEI पूरा नहीं है!!!!!!!!");
                    greenv.setTextColor(Color.parseColor("#00C853"));
                    greenv.setText("नीचे पूर्ण IMEI देखें!!!");
                    corrimeiv.setText("सही IMEI:\n" + (tester2.iffournew()));
                    tacv.setText("प्रकार आवंटन कोड:\n" + tester.tac());
                    rbiv.setText("शारीरिक पहचानकर्ता सूचित करना:\n" + tester.rbi());
                    mebv.setText("मोबाइल डिवाइस प्रकार:\n" + tester.meb());
                    facv.setText("अंतिम विधानसभा कोड:\n" + tester.fac());
                    snv.setText("क्रमांक:\n" + tester.sn());
                    //cdv.setText(tester.cd());
                    ldv.setText("Luhn संख्या:\n" + tester2.ld2());
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
            startActivity(new Intent(this, com.gibatekpro.imeigenerator.hindigen.class));
        }

        return super.onOptionsItemSelected(item);
    }


    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(com.gibatekpro.imeigenerator.hindianalyzer.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(com.gibatekpro.imeigenerator.hindianalyzer.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(com.gibatekpro.imeigenerator.hindianalyzer.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(com.gibatekpro.imeigenerator.hindianalyzer.this, new String[]{permission}, requestCode);
            }
        } else {
            if (telephonyManager == null || telephonyManager.getDeviceId() == null) {
                redv.setTextColor(Color.parseColor("#D50000"));
                redv.setText("माफ़ कीजिये!!!!!!!");
                greenv.setTextColor(Color.parseColor("#D50000"));
                greenv.setText("IMEI दिखाने में असमर्थ");
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
                    analyst test = new analyst(mine);
                    redv.setTextColor(Color.parseColor("#00C853"));
                    redv.setText("इस फोन IMEI विश्लेषण");
                    greenv.setTextColor(Color.parseColor("#00C853"));
                    greenv.setText("नीचे दिखाया गया है!!");
                    corrimeiv.setText("इस फोन IMEI:\n" + mine);
                    tacv.setText("प्रकार आवंटन कोड:\n" + test.tac());
                    rbiv.setText("शारीरिक पहचानकर्ता सूचित करना:\n" + test.rbi());
                    mebv.setText("मोबाइल डिवाइस प्रकार:\n" + test.meb());
                    facv.setText("अंतिम विधानसभा कोड:\n" + test.fac());
                    snv.setText("क्रमांक:\n" + test.sn());
                    ldv.setText("Luhn संख्या:\n" + test.ld());
                }
                if (count == 14) {
                    analyst tester = new analyst(mine);
                    com.gibatekpro.imeigenerator.Iffour tester2 = new com.gibatekpro.imeigenerator.Iffour(mine);
                    redv.setTextColor(Color.parseColor("#00C853"));
                    redv.setText("इस फोन IMEI विश्लेषण");
                    greenv.setTextColor(Color.parseColor("#00C853"));
                    greenv.setText("नीचे दिखाया गया है!!");
                    corrimeiv.setText("सही IMEI:\n" + (tester2.iffournew()));
                    tacv.setText("प्रकार आवंटन कोड:\n" + tester.tac());
                    rbiv.setText("शारीरिक पहचानकर्ता सूचित करना:\n" + tester.rbi());
                    mebv.setText("मोबाइल डिवाइस प्रकार:\n" + tester.meb());
                    facv.setText("अंतिम विधानसभा कोड:\n" + tester.fac());
                    snv.setText("क्रमांक:\n" + tester.sn());
                    //cdv.setText(tester.cd());
                    ldv.setText("Luhn संख्या:\n" + tester2.ld2());
                }
                if ( count < 14 || count > 15) {
                    corrimeiv.setText("इस फोन IMEI:\n" + mine);
                    tacv.setText(R.string.tac);
                    rbiv.setText(R.string.hin_rbi);
                    mebv.setText(R.string.hin_MET);
                    facv.setText(R.string.hin_fac);
                    snv.setText(R.string.hin_sn);
                    ldv.setText(R.string.hin_ln);
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
                    redv.setText("माफ़ कीजिये!!!!!!!");
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
                        analyst test = new analyst(mine);
                        redv.setTextColor(Color.parseColor("#00C853"));
                        redv.setText("इस फोन IMEI विश्लेषण");
                        greenv.setTextColor(Color.parseColor("#00C853"));
                        greenv.setText("नीचे दिखाया गया है!!");
                        corrimeiv.setText("THIS PHONE'S IMEI:\n" + mine);
                        tacv.setText("प्रकार आवंटन कोड:\n" + test.tac());
                        rbiv.setText("शारीरिक पहचानकर्ता सूचित करना:\n" + test.rbi());
                        mebv.setText("मोबाइल डिवाइस प्रकार:\n" + test.meb());
                        facv.setText("अंतिम विधानसभा कोड:\n" + test.fac());
                        snv.setText("क्रमांक:\n" + test.sn());
                        ldv.setText("Luhn संख्या:\n" + test.ld());
                    }
                    if (count == 14) {
                        analyst tester = new analyst(mine);
                        com.gibatekpro.imeigenerator.Iffour tester2 = new com.gibatekpro.imeigenerator.Iffour(mine);
                        redv.setTextColor(Color.parseColor("#00C853"));
                        redv.setText("इस फोन IMEI विश्लेषण");
                        greenv.setTextColor(Color.parseColor("#00C853"));
                        greenv.setText("नीचे दिखाया गया है!!");
                        corrimeiv.setText("सही IMEI:\n" + (tester2.iffournew()));
                        tacv.setText("प्रकार आवंटन कोड:\n" + tester.tac());
                        rbiv.setText("शारीरिक पहचानकर्ता सूचित करना:\n" + tester.rbi());
                        mebv.setText("मोबाइल डिवाइस प्रकार:\n" + tester.meb());
                        facv.setText("अंतिम विधानसभा कोड:\n" + tester.fac());
                        snv.setText("क्रमांक:\n" + tester.sn());
                        //cdv.setText(tester.cd());
                        ldv.setText("Luhn संख्या:\n" + tester2.ld2());
                    }
                    if ( count < 14 || count > 15) {
                        corrimeiv.setText("इस फोन IMEI:\n" + mine);
                        tacv.setText(R.string.tac);
                        rbiv.setText(R.string.hin_rbi);
                        mebv.setText(R.string.hin_MET);
                        facv.setText(R.string.hin_fac);
                        snv.setText(R.string.hin_sn);
                        ldv.setText(R.string.hin_ln);
                    }
                }

                /*for (int i = 1; i < 5; i++) {
                    loadInterstitial();
                    showInterstitial();
                }*/


            }

            Toast.makeText(this, "अनुमति प्रदान की गई", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "अनुमति नहीं मिली", Toast.LENGTH_SHORT).show();
        }
    }
}
