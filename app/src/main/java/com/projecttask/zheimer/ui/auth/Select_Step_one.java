package com.projecttask.zheimer.ui.auth;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.projecttask.zheimer.R;
public class Select_Step_one extends AppCompatActivity {
    private CardView cardSignUp, cardLogIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_step_one);
        cardSignUp = findViewById(R.id.card_sign_up);
        cardLogIn = findViewById(R.id.card_log_in);
        cardSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(Select_Step_one.this, SelectUser.class);
                startActivity(signUpIntent);
            }
        });
        cardLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logInIntent = new Intent(Select_Step_one.this, LoginActivity.class);
                startActivity(logInIntent);
            }
        });
    }
}
