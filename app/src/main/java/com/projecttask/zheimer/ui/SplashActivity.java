package com.projecttask.zheimer.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.projecttask.zheimer.R;
import com.projecttask.zheimer.ui.auth.SignUpActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        },1000);
    }
}