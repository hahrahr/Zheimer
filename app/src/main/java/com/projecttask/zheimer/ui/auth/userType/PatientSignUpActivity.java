package com.projecttask.zheimer.ui.auth.userType;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class PatientSignUpActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST = 100;
    private static final int STORAGE_PERMISSION_REQUEST = 101;
    private ImageView profileImage;
    private Button uploadImageButton, takePhotoButton, signUpButton;
    private EditText nameEditText, emailEditText, phoneEditText, relationshipEditText, birthdateEditText, passwordEditText, confirmPasswordEditText;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private StorageReference storageReference;
    private Switch genderSwitch;
    private String encodedImage;

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

        uploadImageButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(PatientSignUpActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(PatientSignUpActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST);
            } else {
                openFileChooser();
            }
        });

        takePhotoButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(PatientSignUpActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(PatientSignUpActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(PatientSignUpActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        CAMERA_PERMISSION_REQUEST);
            } else {
                openCamera();
            }
        });

        signUpButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();
            String age = birthdateEditText.getText().toString().trim();
            String relationship = relationshipEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();
            String gender = genderSwitch.isChecked() ? "Male" : "Female";

            if (isSignUpDetailsValid(name, email, phone, age, password, confirmPassword)) {
                register(email, password, name, phone, age, gender, relationship);
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pickImage.launch(intent);
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
                Uri photoUri = FileProvider.getUriForFile(this, "com.projecttask.zheimer.fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                pickImage.launch(intent);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    private String encodedImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        profileImage.setImageBitmap(bitmap);
                        encodedImage = encodedImage(bitmap);
                        if (encodedImage != null) {
                            Toast.makeText(this, "Image encoded successfully", Toast.LENGTH_SHORT).show();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

    private boolean isSignUpDetailsValid(String name, String email, String phone, String birthdate, String password, String confirmPassword) {
        if (encodedImage == null) {
            showToast("Select profile image");
            return false;
        } else if (name.isEmpty()) {
            showToast("Enter name");
            return false;
        } else if (email.isEmpty()) {
            showToast("Enter email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Enter valid email");
            return false;
        } else if (phone.isEmpty()) {
            showToast("Enter phone number");
            return false;
        } else if (phone.length() < 11) {
            showToast("Phone number should be 11 digits");
            return false;
        } else if (birthdate.isEmpty()) {
            showToast("Enter birthdate");
            return false;
        } else if (password.isEmpty()) {
            showToast("Enter password");
            return false;
        } else if (password.length() < 6) {
            showToast("Password should be at least 6 characters");
            return false;
        } else if (confirmPassword.isEmpty()) {
            showToast("Confirm your password");
            return false;
        } else if (!password.equals(confirmPassword)) {
            showToast("Password and confirm password do not match");
            return false;
        } else {
            return true;
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void register(String email, String password, String name, String phone, String birthdate, String gender, String relationship) {
        progressBar.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        sendEmailVerification();
                        String userId = auth.getCurrentUser().getUid();
                        StorageReference imageReference = storageReference.child("patients/" + userId + "/profile.jpg");

                        imageReference.putBytes(Base64.decode(encodedImage, Base64.DEFAULT))
                                .addOnSuccessListener(taskSnapshot -> imageReference.getDownloadUrl()
                                        .addOnSuccessListener(uri -> {
                                            HashMap<String, Object> user = new HashMap<>();
                                            user.put("userId", userId);
                                            user.put("name", name);
                                            user.put("email", email);
                                            user.put("phone", phone);
                                            user.put("birthdate", birthdate);
                                            user.put("gender", gender);
                                            user.put("relationship", relationship);
                                            user.put("profileImage", uri.toString());

                                            firestore.collection("patients").document(userId)
                                                    .set(user)
                                                    .addOnSuccessListener(aVoid -> {
                                                        Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                        finish();
                                                    })
                                                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to save user data", Toast.LENGTH_SHORT).show());
                                        }))
                                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to upload profile image", Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(getApplicationContext(), "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendEmailVerification() {
        if (auth.getCurrentUser() != null) {
            auth.getCurrentUser().sendEmailVerification()
                    .addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(), "Verification email sent", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to send verification email", Toast.LENGTH_SHORT).show());
        }
    }
}
