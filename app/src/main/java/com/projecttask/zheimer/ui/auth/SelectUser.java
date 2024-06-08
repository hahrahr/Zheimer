package com.projecttask.zheimer.ui.auth;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.projecttask.zheimer.R;
import com.projecttask.zheimer.ui.auth.userType.DoctorSignUpActivity;
import com.projecttask.zheimer.ui.auth.userType.PatientSignUpActivity;
public class SelectUser extends AppCompatActivity {
    private CardView cardDoctor, cardPatient;
    private ImageView BackArrow;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);
        cardDoctor = findViewById(R.id.card_doctor);
        cardPatient = findViewById(R.id.card_patient);
        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> finish());
        cardDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectUser.this, DoctorSignUpActivity.class);
                startActivity(intent);
            }
        });
        cardPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectUser.this, PatientSignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
