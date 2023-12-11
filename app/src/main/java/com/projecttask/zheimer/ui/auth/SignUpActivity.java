package com.projecttask.zheimer.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.projecttask.zheimer.R;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText signup_username, signup_email,signup_phone, signup_password, signup_confirm_password;
    private Button signup_Button;
    private TextView loginRedirectbtn;
    private ProgressBar progressBar ,RegisterProgressBarto;

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
        loginRedirectbtn = findViewById(R.id.loginRedirectbtn);
        progressBar = findViewById(R.id.RegisterProgressBar);
        RegisterProgressBarto = findViewById(R.id.RegisterProgressBarto);

        signup_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = signup_username.getText().toString().trim();
                String email = signup_email.getText().toString().trim();
                String phone = signup_phone.getText().toString().trim();
                String pass = signup_password.getText().toString().trim();
                String confirmPass = signup_confirm_password.getText().toString().trim();

                if (name.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "User Name Cannot be Empty", Toast.LENGTH_SHORT).show();
                    signup_username.findFocus();
                    return;
                }if (email.isEmpty()) {
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
                }else {

                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.VISIBLE);
                                RegisterProgressBarto.setVisibility(View.VISIBLE);
                                signup_Button.setVisibility(View.INVISIBLE);
                                Toast.makeText(SignUpActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                progressBar.setVisibility(View.INVISIBLE);
                                RegisterProgressBarto.setVisibility(View.INVISIBLE);
                                signup_Button.setVisibility(View.VISIBLE);

                            } else {
                                Toast.makeText(SignUpActivity.this, "Signup Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        loginRedirectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
            }
        });
    }

}