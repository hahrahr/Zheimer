package com.projecttask.zheimer.ui.auth;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.projecttask.zheimer.MainActivity;
import com.projecttask.zheimer.R;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth firebaseAuth;
    private EditText login_email, login_password;
    private TextView SignUpRedirectBTN, LoginForgetPasswordBTN;
    private Button loginButton;
    private ImageView googleICon, facebookICon;
    private ProgressBar LoginProgressBar;
    private FirebaseAuth mAuth;
    private GoogleSignInOptions gso;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        SignUpRedirectBTN = findViewById(R.id.SignUpRedirectBTN);
        loginButton = findViewById(R.id.login_button);
        LoginProgressBar = findViewById(R.id.LoginProgressBar);
        LoginForgetPasswordBTN = findViewById(R.id.LoginForgetPasswordBTN);
        googleICon = findViewById(R.id.googleICon);
        facebookICon = findViewById(R.id.facebookICon);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("457103937476-e0lkqhlskblbksk9uti3dg2t3ltosud6.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mAuth = FirebaseAuth.getInstance();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = login_email.getText().toString();
                String pass = login_password.getText().toString();
                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!pass.isEmpty()) {
                        firebaseAuth.signInWithEmailAndPassword(email, pass)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        LoginProgressBar.setVisibility(View.VISIBLE);
                                        loginButton.setVisibility(View.INVISIBLE);

                                        getSharedPreferences("userShared", MODE_PRIVATE)
                                                .edit()
                                                .putBoolean("isLogin", true)
                                                .apply();

                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                        LoginProgressBar.setVisibility(View.INVISIBLE);
                                        loginButton.setVisibility(View.VISIBLE);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        login_password.setError("Password Cannot be Empty");
                        login_password.findFocus();
                        Toast.makeText(LoginActivity.this, "Password Cannot be Empty", Toast.LENGTH_SHORT).show();
                    }
                } else if (email.isEmpty()) {
                    login_email.setError("Email Cannot be Empty");
                    login_email.findFocus();
                    Toast.makeText(LoginActivity.this, "Email Cannot be Empty", Toast.LENGTH_SHORT).show();
                } else {
                    login_email.setError("Please Enter Valid Email");
                    login_email.findFocus();
                    Toast.makeText(LoginActivity.this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
                }
            }
        });


        LoginForgetPasswordBTN.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));
        SignUpRedirectBTN.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));
        googleICon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
                getSharedPreferences("userShared", MODE_PRIVATE)
                        .edit()
                        .putBoolean("isLogin", true)
                        .apply();

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                Toast.makeText(LoginActivity.this, "Google Signin Successful", Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this, "Google Sign In Failed", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        // You can handle the signed-in user here
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                });
    }
}