package com.rohfl.pagination.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.rohfl.pagination.R;
import com.rohfl.pagination.utils.SharedPreferenceManager;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        updateStatusBarColorMain();
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

    /**
     * updating the status bar color by this method because from xml it hides the data in the status bar
     */
    @SuppressLint("ObsoleteSdkInt")
    public void updateStatusBarColorMain() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setNavigationBarColor(Color.parseColor("#FFFFFF"));
                window.setStatusBarColor(Color.parseColor("#FFFFFF"));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int systemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
                int flagLightStatusBar = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                systemUiVisibility |= flagLightStatusBar;
                getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
