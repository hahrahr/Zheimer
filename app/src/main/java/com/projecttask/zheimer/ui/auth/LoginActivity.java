package com.projecttask.zheimer.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.projecttask.zheimer.MainActivity;
import com.projecttask.zheimer.R;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText login_email, login_password;
    public TextView SignUpRedirectBTN,LoginForgetPasswordBTN;
    public Button loginButton;
    private ProgressBar LoginProgressBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth  = FirebaseAuth.getInstance();
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        SignUpRedirectBTN = findViewById(R.id.SignUpRedirectBTN);
        loginButton = findViewById(R.id.login_button);
        LoginProgressBar = findViewById(R.id.LoginProgressBar);
        LoginForgetPasswordBTN = findViewById(R.id.LoginForgetPasswordBTN);

        loginButton.setOnClickListener(v -> {
            String email = login_email.getText().toString().trim();
            String pass = login_password.getText().toString();
            if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (!pass.isEmpty()) {
                    LoginProgressBar.setVisibility(View.VISIBLE); // Show ProgressBar before the authentication starts
                    auth.signInWithEmailAndPassword(email, pass)
                            .addOnSuccessListener(authResult -> {
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                LoginProgressBar.setVisibility(View.GONE); // Hide ProgressBar after success
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish(); // Finish current activity to prevent going back to it

                            }).addOnFailureListener(e -> {
                                Toast.makeText(LoginActivity.this, "Login Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                LoginProgressBar.setVisibility(View.GONE); // Hide ProgressBar after failure
                            });
                } else {
                    login_password.setError("Password Cannot be Empty");
                    Toast.makeText(LoginActivity.this, "Password Cannot be Empty", Toast.LENGTH_SHORT).show();
                }
            } else if (email.isEmpty()) {
                login_email.setError("Email Cannot be Empty");
                Toast.makeText(LoginActivity.this, "Email Cannot be Empty", Toast.LENGTH_SHORT).show();
            } else {
                login_email.setError("Please Enter Valid Email");
                Toast.makeText(LoginActivity.this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
            }
        });
        LoginForgetPasswordBTN.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class)));
        SignUpRedirectBTN.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this,SignUpActivity.class)));
    }
}