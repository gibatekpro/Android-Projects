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

public class turkanalyzer extends AppCompatActivity {

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
        setContentView(R.layout.activity_turkanalyzer);

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

        corrimeiv.setText("Doğru IMEI:");
        tacv.setText("Tip Tahsis Kodu:");
        rbiv.setText("Rapor Gövde  Tanımlayıcı:");
        mebv.setText("Mobil Cihaz Tipi:");
        facv.setText("Nihai montaj kodu:");
        snv.setText("Seri numarası:");
        ldv.setText("Luhn hane:");


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
                corrimeiv.setText("Doğru IMEI:");
                tacv.setText("Tip Tahsis Kodu:");
                rbiv.setText("Rapor Gövde  Tanımlayıcı:");
                mebv.setText("Mobil Cihaz Tipi:");
                facv.setText("Nihai montaj kodu:");
                snv.setText("Seri numarası:");
                ldv.setText("Luhn hane:");
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
                Iffour tester2 = new Iffour(ins);
                for (int i = 0; i < ins.length(); i++) {
                    if (ins.charAt(i) != ' ') {
                        ++count;
                    }
                }

                if (count < 14 || count > 15) {
                    redv.setTextColor(Color.parseColor("#D50000"));
                    redv.setText("Geçersiz Girdi!!!!!!!!");
                    greenv.setTextColor(Color.parseColor("#D50000"));
                    greenv.setText("14 veya 15 haneli girin");
                    corrimeiv.setText("Doğru IMEI:");
                    tacv.setText("Tip Tahsis Kodu:");
                    rbiv.setText("Rapor Gövde  Tanımlayıcı:");
                    mebv.setText("Mobil Cihaz Tipi:");
                    facv.setText("Nihai montaj kodu:");
                    snv.setText("Seri numarası:");
                    ldv.setText("Luhn hane:");
                }
                if (count == 15) {
                    if (fifdig.Sepfifdig() == checks.checkluhn()) {
                        redv.setTextColor(Color.parseColor("#00C853"));
                        redv.setText("IMEI geçerlidir!!");
                        greenv.setTextColor(Color.parseColor("#00C853"));
                        greenv.setText("Aşağıdaki IMEI analizine göz atın!!");
                        corrimeiv.setText("Doğru IMEI:\n" + (bac.newnum14()));
                        tacv.setText("Tip Tahsis Kodu:\n" + tester.tac());
                        rbiv.setText("Rapor Gövde  Tanımlayıcı:\n" + tester.rbi());
                        mebv.setText("Mobil Cihaz Tipi:\n" + tester.meb());
                        facv.setText("Nihai montaj kodu:\n" + tester.fac());
                        snv.setText("Seri numarası:\n" + tester.sn());
                        //cdv.setText(tester.cd());
                        ldv.setText("Luhn hane:\n" + tester.ld());
                    }
                    if (fifdig.Sepfifdig() != checks.checkluhn()) {
                        redv.setTextColor(Color.parseColor("#D50000"));
                        redv.setText("IMEI yanlış!!!!!!!!");
                        greenv.setTextColor(Color.parseColor("#00C853"));
                        greenv.setText("Aşağıdaki doğru IMEI'ya bakın!!!");
                        corrimeiv.setText("Doğru IMEI:\n" + (bac.newnum14()));
                        tacv.setText("Tip Tahsis Kodu:\n" + tester.tac());
                        rbiv.setText("Rapor Gövde  Tanımlayıcı:\n" + tester.rbi());
                        mebv.setText("Mobil Cihaz Tipi:\n" + tester.meb());
                        facv.setText("Nihai montaj kodu:\n" + tester.fac());
                        snv.setText("Seri numarası:\n" + tester.sn());
                        //cdv.setText(tester.cd());
                        ldv.setText("Luhn hane:\n" + tester.ld());
                    }
                }
                if (count == 14) {
                    redv.setTextColor(Color.parseColor("#D50000"));
                    redv.setText("IMEI tamamlanmadı!!!!!!!!");
                    greenv.setTextColor(Color.parseColor("#00C853"));
                    greenv.setText("Aşağıdaki tam IMEI bakın!!!");
                    corrimeiv.setText("Doğru IMEI:\n" + (tester2.iffournew()));
                    tacv.setText("Tip Tahsis Kodu:\n" + tester.tac());
                    rbiv.setText("Rapor Gövde  Tanımlayıcı:\n" + tester.rbi());
                    mebv.setText("Mobil Cihaz Tipi:\n" + tester.meb());
                    facv.setText("Nihai montaj kodu:\n" + tester.fac());
                    snv.setText("Seri numarası:\n" + tester.sn());
                    //cdv.setText(tester.cd());
                    ldv.setText("Luhn hane:\n" + tester2.ld2());
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
            startActivity(new Intent(this, com.gibatekpro.imeigenerator.turkgen.class));
        }

        return super.onOptionsItemSelected(item);
    }


    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(com.gibatekpro.imeigenerator.turkanalyzer.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(com.gibatekpro.imeigenerator.turkanalyzer.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(com.gibatekpro.imeigenerator.turkanalyzer.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(com.gibatekpro.imeigenerator.turkanalyzer.this, new String[]{permission}, requestCode);
            }
        } else {
            if (telephonyManager == null || telephonyManager.getDeviceId() == null) {
                redv.setTextColor(Color.parseColor("#D50000"));
                redv.setText("Pardon!!!!!!!");
                greenv.setTextColor(Color.parseColor("#D50000"));
                greenv.setText("IMEI alınamadı");
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
                    redv.setText("Bu telefonun IMEI analizi");
                    greenv.setTextColor(Color.parseColor("#00C853"));
                    greenv.setText("Aşağıda gösterilmektedir!!");
                    corrimeiv.setText("Bu telefon IMEI:\n" + mine);
                    tacv.setText("Tip Tahsis Kodu:\n" + test.tac());
                    rbiv.setText("Rapor Gövde  Tanımlayıcı:\n" + test.rbi());
                    mebv.setText("Mobil Cihaz Tipi:\n" + test.meb());
                    facv.setText("Nihai montaj kodu:\n" + test.fac());
                    snv.setText("Seri numarası:\n" + test.sn());
                    ldv.setText("Luhn hane:\n" + test.ld());
                }
                if (count == 14) {
                    analyst tester = new analyst(mine);
                    Iffour tester2 = new Iffour(mine);
                    redv.setTextColor(Color.parseColor("#00C853"));
                    redv.setText("Bu telefonun IMEI analizi");
                    greenv.setTextColor(Color.parseColor("#00C853"));
                    greenv.setText("Aşağıda gösterilmektedir!!");
                    corrimeiv.setText("Doğru IMEI:\n" + (tester2.iffournew()));
                    tacv.setText("Tip Tahsis Kodu:\n" + tester.tac());
                    rbiv.setText("Rapor Gövde  Tanımlayıcı:\n" + tester.rbi());
                    mebv.setText("Mobil Cihaz Tipi:\n" + tester.meb());
                    facv.setText("Nihai montaj kodu:\n" + tester.fac());
                    snv.setText("Seri numarası:\n" + tester.sn());
                    //cdv.setText(tester.cd());
                    ldv.setText("Luhn hane:\n" + tester2.ld2());
                }
                if ( count < 14 || count > 15 ) {
                    corrimeiv.setText("Bu telefon IMEI:\n" + mine);
                    tacv.setText("Tip Tahsis Kodu:");
                    rbiv.setText("Rapor Gövde  Tanımlayıcı:");
                    mebv.setText("Mobil Cihaz Tipi:");
                    facv.setText("Nihai montaj kodu:");
                    snv.setText("Seri numarası:");
                    ldv.setText("Luhn hane:");
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
                    redv.setText("PARDON!!!!!!!");
                    greenv.setTextColor(Color.parseColor("#D50000"));
                    greenv.setText("IMEI alınamadı");
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
                        redv.setText("Bu telefonun IMEI analizi");
                        greenv.setTextColor(Color.parseColor("#00C853"));
                        greenv.setText("Aşağıda gösterilmektedir!!");
                        corrimeiv.setText("Bu telefon IMEI:\n" + mine);
                        tacv.setText("Tip Tahsis Kodu:\n" + test.tac());
                        rbiv.setText("Rapor Gövde  Tanımlayıcı:\n" + test.rbi());
                        mebv.setText("Mobil Cihaz Tipi:\n" + test.meb());
                        facv.setText("Nihai montaj kodu:\n" + test.fac());
                        snv.setText("Seri numarası:\n" + test.sn());
                        ldv.setText("Luhn hane:\n" + test.ld());
                    }
                    if (count == 14) {
                        analyst tester = new analyst(mine);
                        Iffour tester2 = new Iffour(mine);
                        redv.setTextColor(Color.parseColor("#00C853"));
                        redv.setText("Bu telefonun IMEI analizi");
                        greenv.setTextColor(Color.parseColor("#00C853"));
                        greenv.setText("Aşağıda gösterilmektedir!!");
                        corrimeiv.setText("Doğru IMEI:\n" + (tester2.iffournew()));
                        tacv.setText("Tip Tahsis Kodu:\n" + tester.tac());
                        rbiv.setText("Rapor Gövde  Tanımlayıcı:\n" + tester.rbi());
                        mebv.setText("Mobil Cihaz Tipi:\n" + tester.meb());
                        facv.setText("Nihai montaj kodu:\n" + tester.fac());
                        snv.setText("Seri numarası:\n" + tester.sn());
                        //cdv.setText(tester.cd());
                        ldv.setText("Luhn hane:\n" + tester2.ld2());
                    }
                    if ( count < 14 || count > 15 ) {
                        corrimeiv.setText("Bu telefon IMEI:\n" + mine);
                        tacv.setText("Tip Tahsis Kodu:");
                        rbiv.setText("Rapor Gövde  Tanımlayıcı:");
                        mebv.setText("Mobil Cihaz Tipi:");
                        facv.setText("Nihai montaj kodu:");
                        snv.setText("Seri numarası:");
                        ldv.setText("Luhn hane:");
                    }
                }

                /*for (int i = 1; i < 5; i++) {
                    loadInterstitial();
                    showInterstitial();
                }*/


            }

            Toast.makeText(this, "İzin verildi", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "izin reddedildi", Toast.LENGTH_SHORT).show();
        }
    }
}
