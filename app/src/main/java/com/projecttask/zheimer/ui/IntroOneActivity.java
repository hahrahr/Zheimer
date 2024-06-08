package com.projecttask.zheimer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.projecttask.zheimer.R;
import com.projecttask.zheimer.ui.auth.Select_Step_one;

public class IntroOneActivity extends AppCompatActivity {
    Button introBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_one);

        introBtn =findViewById(R.id.introBtn);
        introBtn.setOnClickListener(v -> {
            Intent intent = new Intent(IntroOneActivity.this, Select_Step_one.class);
            startActivity(intent);
            finish();
        });
    }
}