package com.gibatekpro.spanishimeigen;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
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
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

/**
 * Created by Anthony Gibah on 6/20/2017.
 */
public class Analyze extends AppCompatActivity {
    private Button check, clear, imei2;
    private Toolbar toolbar;
    public TextView corrimeiv, tacv, rbiv, mebv, facv, snv, ldv, redv, greenv;
    public EditText inss;
    private InterstitialAd mInterstitialAd;
    private TelephonyManager telephonyManager;


    private static int TELEPHONE = 23;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);



        // AppRater.app_launched(this);

        MobileAds.initialize(this.getApplicationContext(),
                "ca-app-pub-1488497497647050~6151476328");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
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

        corrimeiv.setText("Correcto IMEI:");
        tacv.setText("Tipo Asignacion Codigo:");
        rbiv.setText("Identificador De Organizacion Informante:");
        mebv.setText("Tipo De Equipo movil:");
        facv.setText("Codigo De Montaje Final:");
        snv.setText("Numero De Serie:");
        ldv.setText("Luhn Digito:");

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
                corrimeiv.setText("Correcto IMEI:");
                tacv.setText("Tipo Asignacion Codigo:");
                rbiv.setText("Identificador De Organizacion Informante:");
                mebv.setText("Tipo De Equipo movil:");
                facv.setText("Codigo De Montaje Final:");
                snv.setText("Numero De Serie:");
                ldv.setText("Luhn Digito:");
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
                Digseperator fifdig = new Digseperator(ins);
                Digseperator checks = new Digseperator(ins);
                Digseperator bac = new Digseperator(ins);
                analyst tester = new analyst(ins);
                Iffour tester2 = new Iffour(ins);
                for (int i = 0; i < ins.length(); i++) {
                    if (ins.charAt(i) != ' ') {
                        ++count;
                    }
                }

                if (count < 14 || count > 15) {
                    redv.setTextColor(Color.parseColor("#D50000"));
                    redv.setText("ENTRADA INVALIDA!!!!!!!!");
                    greenv.setTextColor(Color.parseColor("#D50000"));
                    greenv.setText("INTRODUZCA 14 O 15 DIGITOS");
                    corrimeiv.setText("Correcto IMEI:");
                    tacv.setText("Tipo Asignacion Codigo:");
                    rbiv.setText("Identificador De Organizacion Informante:");
                    mebv.setText("Tipo De Equipo movil:");
                    facv.setText("Codigo De Montaje Final:");
                    snv.setText("Numero De Serie:");
                    ldv.setText("Luhn Digito:");
                }
                if (count == 15) {
                    if (fifdig.Sepfifdig() == checks.checkluhn()) {
                        redv.setTextColor(Color.parseColor("#00C853"));
                        redv.setText("IMEI ES VALIDO!!");
                        greenv.setTextColor(Color.parseColor("#00C853"));
                        greenv.setText("VER ANALISIS ABAJO!!");
                        corrimeiv.setText("Correcto IMEI:\n" + (bac.newnum14()));
                        tacv.setText("Tipo Asignacion Codigo:\n" + tester.tac());
                        rbiv.setText("Identificador De Organizacion Informante:\n" + tester.rbi());
                        mebv.setText("Tipo De Equipo movil:\n" + tester.meb());
                        facv.setText("Codigo De Montaje Final:\n" + tester.fac());
                        snv.setText("Numero De Serie:\n" + tester.sn());
                        //cdv.setText(tester.cd());
                        ldv.setText("Luhn Digito:\n" + tester.ld());
                    }
                    if (fifdig.Sepfifdig() != checks.checkluhn()) {
                        redv.setTextColor(Color.parseColor("#D50000"));
                        redv.setText("IMEI ES INCORRECTO!!!!!!!!");
                        greenv.setTextColor(Color.parseColor("#00C853"));
                        greenv.setText("VER EL IMEI CORRECTO ABAJO!!!");
                        corrimeiv.setText("Correcto IMEI:\n" + (bac.newnum14()));
                        tacv.setText("Tipo Asignacion Codigo:\n" + tester.tac());
                        rbiv.setText("Identificador De Organizacion Informante:\n" + tester.rbi());
                        mebv.setText("Tipo De Equipo movil:\n" + tester.meb());
                        facv.setText("Codigo De Montaje Final:\n" + tester.fac());
                        snv.setText("Numero De Serie:\n" + tester.sn());
                        //cdv.setText(tester.cd());
                        ldv.setText("Luhn Digito:\n" + tester.ld());
                    }
                }
                if (count == 14) {
                    redv.setTextColor(Color.parseColor("#D50000"));
                    redv.setText("IMEI NO ESTA COMPLETO!!!!!!!!");
                    greenv.setTextColor(Color.parseColor("#00C853"));
                    greenv.setText("VER EL IMEI COMPLETO ABAJO!!!");
                    corrimeiv.setText("Correcto IMEI:\n" + (tester2.iffournew()));
                    tacv.setText("Tipo Asignacion Codigo:\n" + tester.tac());
                    rbiv.setText("Identificador De Organizacion Informante:\n" + tester.rbi());
                    mebv.setText("Tipo De Equipo movil:\n" + tester.meb());
                    facv.setText("Codigo De Montaje Final:\n" + tester.fac());
                    snv.setText("Numero De Serie:\n" + tester.sn());
                    //cdv.setText(tester.cd());
                    ldv.setText("Luhn Digito:\n" + tester2.ld2());
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
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }


    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(Analyze.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Analyze.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(Analyze.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(Analyze.this, new String[]{permission}, requestCode);
            }
        } else {
            if (telephonyManager == null || telephonyManager.getDeviceId() == null) {
                redv.setTextColor(Color.parseColor("#D50000"));
                redv.setText("LO SIENTO!!!!!!!");
                greenv.setTextColor(Color.parseColor("#D50000"));
                greenv.setText("NO SE PUEDE OBTENER IMEI");
            } else {
                int count = 0;
                String mine = telephonyManager.getDeviceId();
                for (int i = 0; i < mine.length(); i++) {
                    if (mine.charAt(i) != ' ') {
                        ++count;
                    }
                }
                if (count == 15) {
                    Digseperator fif = new Digseperator(mine);
                    analyst test = new analyst(mine);
                    redv.setTextColor(Color.parseColor("#00C853"));
                    redv.setText("IMEI ANALISIS DE ESTE TELEFONO");
                    greenv.setTextColor(Color.parseColor("#00C853"));
                    greenv.setText("SE MUESTRA ABAJO!!");
                    corrimeiv.setText("IMEI DE ESTE TELEFONO:\n" + mine);
                    tacv.setText("Tipo Asignacion Codigo:\n" + test.tac());
                    rbiv.setText("Identificador De Organizacion Informante:\n" + test.rbi());
                    mebv.setText("Tipo De Equipo movil:\n" + test.meb());
                    facv.setText("Codigo De Montaje Final:\n" + test.fac());
                    snv.setText("Numero De Serie:\n" + test.sn());
                    ldv.setText("Luhn Digito:\n" + test.ld());
                }
                if (count == 14) {
                    analyst tester = new analyst(mine);
                    Iffour tester2 = new Iffour(mine);
                    redv.setTextColor(Color.parseColor("#D50000"));
                    redv.setText("IMEI NO ESTA COMPLETO!!!!!!!!");
                    greenv.setTextColor(Color.parseColor("#00C853"));
                    greenv.setText("VER EL IMEI COMPLETO ABAJO!!!");
                    corrimeiv.setText("IMEI DE ESTE TELEFONO:\n" + (tester2.iffournew()));
                    tacv.setText("Tipo Asignacion Codigo:\n" + tester.tac());
                    rbiv.setText("Identificador De Organizacion Informante:\n" + tester.rbi());
                    mebv.setText("Tipo De Equipo movil:\n" + tester.meb());
                    facv.setText("Codigo De Montaje Final:\n" + tester.fac());
                    snv.setText("Numero De Serie:\n" + tester.sn());
                    //cdv.setText(tester.cd());
                    ldv.setText("Luhn Digito:\n" + tester2.ld2());
                }
                if ( count < 14 || count > 15){
                    corrimeiv.setText("IMEI DE ESTE TELEFONO:\n" + mine);
                    tacv.setText("Tipo Asignacion Codigo:");
                    rbiv.setText("Identificador De Organizacion Informante:");
                    mebv.setText("Tipo De Equipo movil:");
                    facv.setText("Codigo De Montaje Final:");
                    snv.setText("Numero De Serie:");
                    ldv.setText("Luhn Digito:");
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
                    redv.setText("LO SIENTO!!!!!!!");
                    greenv.setTextColor(Color.parseColor("#D50000"));
                    greenv.setText("NO SE PUEDE OBTENER IMEI");
                } else {
                    int count = 0;
                    String mine = telephonyManager.getDeviceId();
                    for (int i = 0; i < mine.length(); i++) {
                        if (mine.charAt(i) != ' ') {
                            ++count;
                        }
                    }
                    if (count == 15) {
                        Digseperator fif = new Digseperator(mine);
                        analyst test = new analyst(mine);
                        redv.setTextColor(Color.parseColor("#00C853"));
                        redv.setText("IMEI ANALISIS DE ESTE TELEFONO");
                        greenv.setTextColor(Color.parseColor("#00C853"));
                        greenv.setText("SE MUESTRA ABAJO!!");
                        corrimeiv.setText("IMEI DE ESTE TELEFONO:\n" + mine);
                        tacv.setText("Tipo Asignacion Codigo:\n" + test.tac());
                        rbiv.setText("Identificador De Organizacion Informante:\n" + test.rbi());
                        mebv.setText("Tipo De Equipo movil:\n" + test.meb());
                        facv.setText("Codigo De Montaje Final:\n" + test.fac());
                        snv.setText("Numero De Serie:\n" + test.sn());
                        ldv.setText("Luhn Digito:\n" + test.ld());
                    }
                    if (count == 14) {
                        analyst tester = new analyst(mine);
                        Iffour tester2 = new Iffour(mine);
                        redv.setTextColor(Color.parseColor("#D50000"));
                        redv.setText("IMEI NO ESTA COMPLETO!!!!!!!!");
                        greenv.setTextColor(Color.parseColor("#00C853"));
                        greenv.setText("VER EL IMEI COMPLETO ABAJO!!!");
                        corrimeiv.setText("IMEI DE ESTE TELEFONO:\n" + (tester2.iffournew()));
                        tacv.setText("Tipo Asignacion Codigo:\n" + tester.tac());
                        rbiv.setText("Identificador De Organizacion Informante:\n" + tester.rbi());
                        mebv.setText("Tipo De Equipo movil:\n" + tester.meb());
                        facv.setText("Codigo De Montaje Final:\n" + tester.fac());
                        snv.setText("Numero De Serie:\n" + tester.sn());
                        //cdv.setText(tester.cd());
                        ldv.setText("Luhn Digito:\n" + tester2.ld2());
                    }
                    if ( count < 14 || count > 15){
                        corrimeiv.setText("IMEI DE ESTE TELEFONO:\n" + mine);
                        tacv.setText("Tipo Asignacion Codigo:");
                        rbiv.setText("Identificador De Organizacion Informante:");
                        mebv.setText("Tipo De Equipo movil:");
                        facv.setText("Codigo De Montaje Final:");
                        snv.setText("Numero De Serie:");
                        ldv.setText("Luhn Digito:");
                    }
                }

                /*for (int i = 1; i < 5; i++) {
                    loadInterstitial();
                    showInterstitial();
                }*/


            }

            Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
        }
    }


}
