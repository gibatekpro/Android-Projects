package com.gibatekpro.imeigenerator;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;


public class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NativeExpressAdView adView = (NativeExpressAdView) findViewById(R.id.adView);
        adView.loadAd(new AdRequest.Builder().build());

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        Button b = (Button) findViewById(R.id.english);



        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(com.gibatekpro.imeigenerator.MainActivity.this, com.gibatekpro.imeigenerator.englishgen.class));
            }
        });
        Button b2 = (Button) findViewById(R.id.spanish);



        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(com.gibatekpro.imeigenerator.MainActivity.this, spanishgen.class));
            }
        });
        Button b3 = (Button) findViewById(R.id.hindi);



        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(com.gibatekpro.imeigenerator.MainActivity.this, com.gibatekpro.imeigenerator.hindigen.class));
            }
        });
        Button b4 = (Button) findViewById(R.id.french);



        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(com.gibatekpro.imeigenerator.MainActivity.this, frenchgen.class));
            }
        });
        Button b5 = (Button) findViewById(R.id.turk);



        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(com.gibatekpro.imeigenerator.MainActivity.this, com.gibatekpro.imeigenerator.turkgen.class));
            }
        });
    }
}




