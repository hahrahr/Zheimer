package com.projecttask.zheimer.ui.auth;

// ... [other imports remain unchanged]

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.projecttask.zheimer.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private EditText signup_username, signup_email, signup_phone, signup_password, signup_confirm_password;
    private Button signup_Button;
    private TextView loginRedirectbtn;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        signup_username = findViewById(R.id.signup_UserName);
        signup_email = findViewById(R.id.signup_email);
        signup_phone = findViewById(R.id.signup_phone);
        signup_password = findViewById(R.id.signup_password);
        signup_confirm_password = findViewById(R.id.signup_confirm_password);
        signup_Button = findViewById(R.id.signup_button);
        loginRedirectbtn = findViewById(R.id.loginRedirectbtn);
        progressBar = findViewById(R.id.RegisterProgressBar);
        signup_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = signup_username.getText().toString().trim();
                String email = signup_email.getText().toString().trim();
                String phone = signup_phone.getText().toString().trim();
                String password = signup_password.getText().toString().trim();
                String confirmPass = signup_confirm_password.getText().toString().trim();

                if (name.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "User Name Cannot be Empty", Toast.LENGTH_SHORT).show();
                    signup_username.findFocus();
                    return;
                }
                if (email.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Email Cannot be Empty", Toast.LENGTH_SHORT).show();
                    signup_email.findFocus();
                    return;
                }
                if (phone.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Phone Cannot be Empty", Toast.LENGTH_SHORT).show();
                    signup_phone.findFocus();
                    return;
                }
                if (phone.length() < 11) {
                    Toast.makeText(SignUpActivity.this, "Phone Should be a 11 char", Toast.LENGTH_SHORT).show();
                    signup_phone.findFocus();
                    return;
                }
                if (password.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Password Cannot be Empty", Toast.LENGTH_SHORT).show();
                    signup_password.findFocus();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(SignUpActivity.this, "Password Should be a 6 char", Toast.LENGTH_SHORT).show();
                    signup_password.findFocus();
                    return;
                }
                if (confirmPass.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Password Cannot be Empty", Toast.LENGTH_SHORT).show();
                    signup_confirm_password.findFocus();
                    return;
                }
                if (!password.equals(confirmPass)) {
                    Toast.makeText(SignUpActivity.this, "Confirm Password Should be Equal Your Password", Toast.LENGTH_SHORT).show();
                    signup_confirm_password.findFocus();
                } else {
                    register(email, password, name, phone);

                }
            }
        });
        loginRedirectbtn.setOnClickListener(view ->
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class)));
    }

    private void register(String email, String password, String name, String phone) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        signup_Button.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);

                        //Send Data to Firestore
                        String userId = firebaseAuth.getCurrentUser().getUid();

                        Toast.makeText(this, "Send Data to Firebase Successful", Toast.LENGTH_SHORT).show();
                        sendData(userId, email, password, name, phone);

                    } else {
                        Toast.makeText(SignUpActivity.this, "Signup Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendData(String userId, String email, String password, String name, String phone) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = dateFormat.format(new Date());

        HashMap<String, Object> map = new HashMap<>();
        map.put("Name", name);
        map.put("Email", email);
        map.put("Phone", phone);
        map.put("Password", password);
        map.put("timestamp", timestamp);
        map.put("isDeleted", false);

        firestore.collection("users")
                .document("userId")
                .set(map)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "Data stored successfully", Toast.LENGTH_SHORT).show();

                        Toast.makeText(SignUpActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                        progressBar.setVisibility(View.GONE);
                        signup_Button.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(SignUpActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SignUpActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
