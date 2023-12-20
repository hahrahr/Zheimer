package com.projecttask.zheimer.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.projecttask.zheimer.MainActivity;
import com.projecttask.zheimer.R;
import com.projecttask.zheimer.ui.auth.SignUpActivity;

public class IntroOneActivity extends AppCompatActivity {
    Button introBtn;
    private boolean isLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro_one);

        // Read From Shared Preference
        isLogin= getSharedPreferences("userShared",MODE_PRIVATE)
                .getBoolean("isLogin",false);

        introBtn =findViewById(R.id.introBtn);
        introBtn.setOnClickListener(v -> {
            if (isLogin){
            startActivity(new Intent(IntroOneActivity.this, MainActivity.class));
            finish();
        }else {
                startActivity(new Intent(IntroOneActivity.this, SignUpActivity.class));
                finish();
            }
        });
    }
}