package com.projecttask.zheimer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.projecttask.zheimer.ui.auth.LoginActivity;

public class MainActivity extends AppCompatActivity {
    TextView main_TxtName;
    public Button main_Btn_Logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_TxtName = findViewById(R.id.main_TxtName);
        main_Btn_Logout = findViewById(R.id.main_Btn_Logout);

        Log.d("MainActivity", "MainActivity started");

        main_Btn_Logout.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("isLogin", MODE_PRIVATE);
            sharedPreferences.edit()
                    .clear()
                    .apply();

            Log.d("MainActivity", "User logged out");

            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish(); // Finish current activity to prevent going back to it
        });
    }
}
