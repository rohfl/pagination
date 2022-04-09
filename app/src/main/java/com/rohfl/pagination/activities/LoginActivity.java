package com.rohfl.pagination.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rohfl.pagination.R;
import com.rohfl.pagination.utils.SharedPreferenceManager;
import com.rohfl.pagination.utils.ValidationManager;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText edtPassword, edtEmail;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_login);

        // getting views
        tilEmail = findViewById(R.id.til_email);
        tilPassword = findViewById(R.id.til_password);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);

        btnLogin = findViewById(R.id.btn_login);

        // calling the attachListeners method
        attachListeners();
    }

    /**
     * attaches the listeners for the respective views
     */
    private void attachListeners() {
        // setting the click listener for the button
        btnLogin.setOnClickListener(v -> {
            if(isValidParams()) {
                SharedPreferenceManager.setBoolValue("is_user_logged_in", true);
                Intent intent = new Intent(this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // adding textchangelistener so that we can remove the error after the user starts typing
        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tilEmail.setBoxStrokeColor(getApplicationContext().getResources().getColor(R.color.grey));
                tilEmail.setHintTextColor(ColorStateList.valueOf(getApplicationContext().getResources().getColor(R.color.grey)));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // adding textchangelistener so that we can remove the error after the user starts typing
        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tilPassword.setBoxStrokeColor(getApplicationContext().getResources().getColor(R.color.grey));
                tilPassword.setHintTextColor(ColorStateList.valueOf(getApplicationContext().getResources().getColor(R.color.grey)));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * Checks if all the data provided by the user is valid or not.
     * @return true if all the params are valid else returns false
     */
    private boolean isValidParams() {

        // validation for email
        String email = edtEmail.getText().toString().trim();
        if(!ValidationManager.isEmailAddress(email)) {
            tilEmail.setBoxStrokeColor(getApplicationContext().getResources().getColor(R.color.red));
            tilEmail.setHintTextColor(ColorStateList.valueOf(getApplicationContext().getResources().getColor(R.color.red)));
            edtEmail.requestFocus();
            if(email.length() == 0) {
                showToast("Please enter your email");
            } else {
                showToast("Please enter correct email");
            }
            return false;
        }

        // validation for password
        String password = edtPassword.getText().toString().trim();
        if(!ValidationManager.isValidPassword(password)) {
            tilPassword.setBoxStrokeColor(getApplicationContext().getResources().getColor(R.color.red));
            tilPassword.setHintTextColor(ColorStateList.valueOf(getApplicationContext().getResources().getColor(R.color.red)));
            edtPassword.requestFocus();
            if(password.length() == 0) {
                showToast("Please enter your password");
            } else {
                showToast("Please must contain letter AND numbers and greater than 8 characters");
            }
            return false;
        }

        return true;
    }

    /**
     * method to show a toast message
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
