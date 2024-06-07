package com.projecttask.zheimer.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.projecttask.zheimer.MainActivity;
import com.projecttask.zheimer.R;
import com.projecttask.zheimer.ui.auth.ForgotPasswordActivity;
import com.projecttask.zheimer.ui.auth.LoginActivity;
import com.projecttask.zheimer.ui.auth.SignUpActivity;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

    }
    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, IntroOneActivity.class));
            finish();
        },3000);
    }
}