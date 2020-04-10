package com.gibatekpro.photomanager.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;

public class SplashActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1234;
    private static final String TAG = "NSUDOKU";
    Button new_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
