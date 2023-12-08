package com.projecttask.zheimer.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

        signup_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = signup_username.getText().toString().trim();
                String user = signup_email.getText().toString().trim();
                String phone = signup_phone.getText().toString().trim();
                String pass = signup_password.getText().toString().trim();
                String confirmPass = signup_confirm_password.getText().toString().trim();

                if (userName.isEmpty()) {
                    signup_username.setError("User Name Cannot be Empty");
                    signup_username.findFocus();
                    return;
                }if (user.isEmpty()) {
                    signup_email.setError("Email Cannot be Empty");
                    signup_email.findFocus();
                    return;
                }if (phone.isEmpty()) {
                    signup_phone.setError("Phone Cannot be Empty");
                    signup_phone.findFocus();
                    return;
                }if (pass.isEmpty()) {
                    signup_password.setError("Password Cannot be Empty");
                    signup_password.findFocus();
                    return;
                }if (confirmPass.isEmpty()) {
                    signup_confirm_password.setError("Password Cannot be Empty");
                    signup_confirm_password.findFocus();
                    return;
                }else{
                    auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
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