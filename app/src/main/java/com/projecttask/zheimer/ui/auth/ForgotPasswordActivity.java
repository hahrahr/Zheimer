package com.projecttask.zheimer.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.projecttask.zheimer.R;
import com.projecttask.zheimer.ui.auth.LoginActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    //Declaration
    Button btnReset, btnBack;
    EditText edtEmail;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    String strEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(com.projecttask.zheimer.R.layout.activity_forgot_password);

        //Initialization
        btnBack = findViewById(R.id.btnForgotPasswordBack);
        btnReset = findViewById(R.id.btnReset);
        edtEmail = findViewById(R.id.edtForgotPasswordEmail);
        progressBar = findViewById(R.id.forgetPasswordProgressbar);

        mAuth = FirebaseAuth.getInstance();

        //Reset Button Listener
        btnReset.setOnClickListener(v -> {
            strEmail = edtEmail.getText().toString().trim();
            if (!TextUtils.isEmpty(strEmail)) {
                ResetPassword();
            } else {
                edtEmail.setError("Email field can't be Empty");
                Toast.makeText(ForgotPasswordActivity.this, "Whrite Your Email Address to Send Reset Link", Toast.LENGTH_SHORT).show();
            }
        });


        //Back Button Code
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void ResetPassword() {
        progressBar.setVisibility(View.VISIBLE);
        btnReset.setVisibility(View.INVISIBLE);

        mAuth.sendPasswordResetEmail(strEmail)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(ForgotPasswordActivity.this, "Reset Password link has been sent to your registered Email", Toast.LENGTH_LONG).show();
                    onBackPressed();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ForgotPasswordActivity.this, "Error :- " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    btnReset.setVisibility(View.VISIBLE);
                });
    }
}