package com.rohfl.pagination.activities;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.rohfl.pagination.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // using handler and passing runnable to execute after 2 seconds. the user will see this screen for 2 seconds.
        new Handler().postDelayed(() -> {

        }, 2000);
    }
}
