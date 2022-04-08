package com.rohfl.pagination.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.rohfl.pagination.R;
import com.rohfl.pagination.utils.SharedPreferenceManager;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferenceManager.init(getApplicationContext());

        // using handler and passing runnable to execute after 2 seconds. the user will see this screen for 2 seconds.
        new Handler().postDelayed(() -> {
            Intent intent;
            if (SharedPreferenceManager.getBoolValue("is_user_logged_in")) {
                intent = new Intent(this, DashboardActivity.class);
            } else {
                intent = new Intent(this, LoginActivity.class);
            }
            startActivity(intent);
            finish();
        }, 2000);
    }
}
