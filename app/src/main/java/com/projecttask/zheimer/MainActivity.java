package com.projecttask.zheimer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.projecttask.zheimer.ui.auth.LoginActivity;
import com.projecttask.zheimer.ui.auth.LoginTowActivity;

public class MainActivity extends AppCompatActivity {
    private Button logoutBtn;
    private boolean isLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        logoutBtn = findViewById(R.id.logoutBtn);


        logoutBtn.setOnClickListener(v -> {
            getSharedPreferences("userShared", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isLogin",false)
                    .clear()
                    .apply();

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginTowActivity.class));
            finish();
            Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show();

        });
    }
}