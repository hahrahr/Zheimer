package com.projecttask.zheimer.ui.auth.userType;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.projecttask.zheimer.MainActivity;
import com.projecttask.zheimer.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class PatientSignUpActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int CAMERA_PERMISSION_REQUEST = 100;
    private static final int STORAGE_PERMISSION_REQUEST = 101;

    private ImageView profileImage, BackArrow;
    private Button uploadImageButton, takePhotoButton, signUpButton;
    private EditText nameEditText, emailEditText, phoneEditText, relationshipEditText, birthdateEditText, passwordEditText, confirmPasswordEditText;
    private ProgressBar progressBar;

    private TextView genderKnow;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private StorageReference storageReference;

    private Switch genderSwitch;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_sign_up);

        profileImage = findViewById(R.id.profile_image);
        uploadImageButton = findViewById(R.id.upload_image_button);
        takePhotoButton = findViewById(R.id.take_photo_button);
        signUpButton = findViewById(R.id.patient_sign_up_button);
        nameEditText = findViewById(R.id.patient_name);
        emailEditText = findViewById(R.id.patient_email);
        phoneEditText = findViewById(R.id.patient_phone);
        relationshipEditText = findViewById(R.id.patient_relationship);
        birthdateEditText = findViewById(R.id.patient_birthdate);
        passwordEditText = findViewById(R.id.patient_password);
        confirmPasswordEditText = findViewById(R.id.patient_confirm_password);
        progressBar = findViewById(R.id.register_progress);
        genderSwitch = findViewById(R.id.toggleSwitch);

        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> finish());

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();


        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(PatientSignUpActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PatientSignUpActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST);
                } else {
                    openFileChooser();
                }
            }
        });

        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(PatientSignUpActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(PatientSignUpActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PatientSignUpActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_REQUEST);
                } else {
                    openCamera();
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();
                String age = birthdateEditText.getText().toString().trim();
                String relationship = relationshipEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();
                String gender = genderSwitch.isChecked() ? "Male" : "Female";


                if (name.isEmpty()) {
                    Toast.makeText(PatientSignUpActivity.this, "Name Cannot be Empty", Toast.LENGTH_SHORT).show();
                    nameEditText.requestFocus();
                    return;
                }
                if (email.isEmpty()) {
                    Toast.makeText(PatientSignUpActivity.this, "Email Cannot be Empty", Toast.LENGTH_SHORT).show();
                    emailEditText.requestFocus();
                    return;
                }
                if (phone.isEmpty()) {
                    Toast.makeText(PatientSignUpActivity.this, "Phone Cannot be Empty", Toast.LENGTH_SHORT).show();
                    phoneEditText.requestFocus();
                    return;
                }
                if (phone.length() < 11) {
                    Toast.makeText(PatientSignUpActivity.this, "Phone Should be 11 characters", Toast.LENGTH_SHORT).show();
                    phoneEditText.requestFocus();
                    return;
                }
                if (age.isEmpty()) {
                    Toast.makeText(PatientSignUpActivity.this, "BirthDate Cannot be Empty", Toast.LENGTH_SHORT).show();
                    birthdateEditText.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    Toast.makeText(PatientSignUpActivity.this, "Password Cannot be Empty", Toast.LENGTH_SHORT).show();
                    passwordEditText.requestFocus();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(PatientSignUpActivity.this, "Password Should be at least 6 characters", Toast.LENGTH_SHORT).show();
                    passwordEditText.requestFocus();
                    return;
                }
                if (confirmPassword.isEmpty()) {
                    Toast.makeText(PatientSignUpActivity.this, "Confirm Password Cannot be Empty", Toast.LENGTH_SHORT).show();
                    confirmPasswordEditText.requestFocus();
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(PatientSignUpActivity.this, "Confirm Password Should be Equal to Your Password", Toast.LENGTH_SHORT).show();
                    confirmPasswordEditText.requestFocus();
                    return;
                }


                register(email, password, name, phone, age, gender, relationship);
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, "com.projecttask.zheimer.fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera and Storage permissions are required to take a photo", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFileChooser();
            } else {
                Toast.makeText(this, "Storage permission is required to upload a photo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImage.setImageBitmap(bitmap);
                uploadImageToFirebase(imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
                profileImage.setImageBitmap(bitmap);
                uploadImageToFirebase(photoUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        StorageReference fileReference = storageReference.child("profile_images/" + System.currentTimeMillis() + ".jpg");

        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    Toast.makeText(PatientSignUpActivity.this, "Image Upload successful", Toast.LENGTH_SHORT).show();
                    storeImageUrlInRealtimeDatabase(imageUrl);
                }))
                .addOnFailureListener(e -> Toast.makeText(PatientSignUpActivity.this, "Image Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void storeImageUrlInRealtimeDatabase(String imageUrl) {
        String userId = auth.getCurrentUser().getUid();
        if (userId != null) {
            HashMap<String, Object> imageData = new HashMap<>();
            imageData.put("profileImageUrl", imageUrl);

            // You can store the image data in a custom path for each user in the database
            FirebaseFirestore.getInstance().collection("users").document(userId).set(imageData)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(PatientSignUpActivity.this, "Image URL saved to database", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PatientSignUpActivity.this, "Failed to save Image URL to database", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void register(String email, String password, String name, String phone, String birthdate, String gender, String relationship) {
        progressBar.setVisibility(View.VISIBLE);



        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        sendEmailVerification();

                        String userId = auth.getCurrentUser().getUid();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String timestamp = dateFormat.format(new Date());
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("name", name);
                        userMap.put("email", email);
                        userMap.put("password", password);
                        userMap.put("phone", phone);
                        userMap.put("age", birthdate);
                        userMap.put("gender", gender);
                        userMap.put("relationship to Petient", relationship);
                        userMap.put("timestamp", timestamp);
                        userMap.put("isDeleted", false);
                        userMap.put("userType", "User"+"( "+relationship+" )");

                        firestore.collection("users").document(userId).set(userMap).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Toast.makeText(PatientSignUpActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(PatientSignUpActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(PatientSignUpActivity.this, "Registration failed: " + task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(PatientSignUpActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void sendEmailVerification() {
        auth.getCurrentUser().sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(PatientSignUpActivity.this, "Verification email sent", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PatientSignUpActivity.this, "Failed to send verification email", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}