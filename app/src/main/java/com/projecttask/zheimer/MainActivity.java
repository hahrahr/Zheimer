package com.projecttask.zheimer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.projecttask.zheimer.ui.auth.LoginActivity;

public class MainActivity extends AppCompatActivity {


    private Button logoutBtn;
    private TextView mainName, mainEmail, mainPhone;
    private ProgressBar mainProgress;
    private boolean isLogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mainName = findViewById(R.id.mainName);
        mainEmail = findViewById(R.id.mainEmail);
        mainPhone = findViewById(R.id.mainPhone);
        logoutBtn = findViewById(R.id.logoutBtn);
        mainProgress = findViewById(R.id.mainProgress);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        logoutBtn.setOnClickListener(v -> {
            getSharedPreferences("userShared", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isLogin", false)
                    .clear()
                    .apply();

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show();

        });

        if (mAuth.getCurrentUser() != null) {
            String loggedEmail = mAuth.getCurrentUser().getEmail();
        } else {
            Toast.makeText(MainActivity.this, "Error = No users Found !", Toast.LENGTH_SHORT).show();
        }

        db.collection("users")
                .document()
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot != null && documentSnapshot.exists()) {
                                String getName = documentSnapshot.getString("name");
                                String getEmail = documentSnapshot.getString("email");
                                String getMobile = documentSnapshot.getString("phone");

                                mainName.setText(getName);
                                mainEmail.setText(getEmail);
                                mainPhone.setText(getMobile);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error = " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}