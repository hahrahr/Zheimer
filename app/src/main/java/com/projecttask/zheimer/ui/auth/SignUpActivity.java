package com.projecttask.zheimer.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.projecttask.zheimer.R;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText signup_username, signup_email,signup_phone, signup_password, signup_confirm_password;
    public Button signup_Button;
    public TextView loginDirectBtn;
    private ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth = FirebaseAuth.getInstance();
        signup_username = findViewById(R.id.signup_UserName);
        signup_email = findViewById(R.id.signup_email);
        signup_phone = findViewById(R.id.signup_phone);
        signup_password = findViewById(R.id.signup_password);
        signup_confirm_password = findViewById(R.id.signup_confirm_password);
        signup_Button = findViewById(R.id.signup_button);
        loginDirectBtn = findViewById(R.id.loginDirectBtn);
        progressBar = findViewById(R.id.RegisterProgressBar);

        signup_Button.setOnClickListener(v -> {
            String userName = signup_username.getText().toString().trim();
            String user = signup_email.getText().toString().trim();
            String phone = signup_phone.getText().toString().trim();
            String pass = signup_password.getText().toString().trim();
            String confirmPass = signup_confirm_password.getText().toString().trim();

            if (userName.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "User Name Cannot be Empty", Toast.LENGTH_SHORT).show();
                signup_username.findFocus();
                return;
            }if (user.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Email Cannot be Empty", Toast.LENGTH_SHORT).show();
                signup_email.findFocus();
                return;
            }if (phone.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Phone Cannot be Empty", Toast.LENGTH_SHORT).show();
                signup_phone.findFocus();
                return;
            }if (phone.length()<11) {
                Toast.makeText(SignUpActivity.this, "Phone Should be a 11 char", Toast.LENGTH_SHORT).show();
                signup_phone.findFocus();
                return;
            }if (pass.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Password Cannot be Empty", Toast.LENGTH_SHORT).show();
                signup_password.findFocus();
                return;
            }if (pass.length()<6) {
                Toast.makeText(SignUpActivity.this, "Password Should be a 6 char", Toast.LENGTH_SHORT).show();
                signup_password.findFocus();
                return;
            }if (confirmPass.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Password Cannot be Empty", Toast.LENGTH_SHORT).show();
                signup_confirm_password.findFocus();
                return;
            }if (!pass.equals(confirmPass)) {
                Toast.makeText(SignUpActivity.this, "Confirm Password Should be Equal Your Password", Toast.LENGTH_SHORT).show();
                signup_confirm_password.findFocus();
            }else{
                auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.VISIBLE);
                        Toast.makeText(SignUpActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));

                    } else {
                        Toast.makeText(SignUpActivity.this, "Signup Failed " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        loginDirectBtn.setOnClickListener(view -> startActivity(new Intent(SignUpActivity.this,LoginActivity.class)));
    }

}