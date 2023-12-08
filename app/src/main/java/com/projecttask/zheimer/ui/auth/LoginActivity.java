package com.projecttask.zheimer.ui.auth;

import static com.projecttask.zheimer.R.id.LoginProgressBar;
import static com.projecttask.zheimer.R.id.SignUpRedirectBTN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.projecttask.zheimer.MainActivity;
import com.projecttask.zheimer.R;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText login_email, login_password;
    private TextView SignUpRedirectBTN;
    private Button loginButton;
    private ProgressBar LoginProgressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth  = FirebaseAuth.getInstance();
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        SignUpRedirectBTN = findViewById(R.id.SignUpRedirectBTN);
        loginButton = findViewById(R.id.login_button);
        LoginProgressBar = findViewById(R.id.LoginProgressBar);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View v) {
                String email = login_email.getText().toString();
                String pass = login_password.getText().toString();
                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!pass.isEmpty()) {
                        auth.signInWithEmailAndPassword(email, pass)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult){
                                        LoginProgressBar.setVisibility(View.VISIBLE);
                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }else {
                        login_password.setError("Password Cannot be Empty");
                        Toast.makeText(LoginActivity.this, "Password Cannot be Empty", Toast.LENGTH_SHORT).show();
                    }
                }else if (email.isEmpty()){
                    login_email.setError("Email Cannot be Empty");
                    Toast.makeText(LoginActivity.this, "Email Cannot be Empty", Toast.LENGTH_SHORT).show();
                }else {
                    login_email.setError("Please Enter Valid Email");
                    Toast.makeText(LoginActivity.this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
                }
            }
        });
        SignUpRedirectBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });
    }
}