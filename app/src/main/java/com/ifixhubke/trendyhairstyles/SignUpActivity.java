package com.ifixhubke.trendyhairstyles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {

    private EditText mail, fullNames, aboutYou, password, reEnterPass;
    private FirebaseAuth mAuth;
    private ProgressBar mProgressbar;
    private String userID;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageURI;
    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseReference;
    private String imageURL;
    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mail = findViewById(R.id.username_field);
        fullNames = findViewById(R.id.full_names_edtx);
        aboutYou = findViewById(R.id.about_you_edtx);
        password = findViewById(R.id.password_edtx);
        reEnterPass = findViewById(R.id.re_password_edtx);
        Button signUp = findViewById(R.id.login_button);
        TextView back_ToLogin = findViewById(R.id.back_to_login);
        mProgressbar = findViewById(R.id.sign_up_progressBar);
        CircleImageView chooseImage = findViewById(R.id.camera_to_choose_photo);
        CircleImageView displayImage = findViewById(R.id.user_profile_image);

        mStorageReference = FirebaseStorage.getInstance().getReference("profile_images");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            finish();
        }

        chooseImage.setOnClickListener(v -> openFileChooser());

        back_ToLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            finish();
        });

        signUp.setOnClickListener(v -> {
            String eMail = mail.getText().toString().trim();
            String passWord = password.getText().toString().trim();
            String rePass = reEnterPass.getText().toString().trim();
            String fNames = fullNames.getText().toString().trim();
            String mAbout = aboutYou.getText().toString().trim();

            if (TextUtils.isEmpty(eMail)) {
                mail.setError("Email required!");
                return;
            }
            if (TextUtils.isEmpty(passWord)) {
                password.setError("Password required!");
                return;
            }
            if (passWord.length() < 6) {
                password.setError("Password should be 6 or more characters");
                return;
            }
            if (TextUtils.isEmpty(rePass)) {
                reEnterPass.setError("Re-Enter Password");
                return;
            }
            if (!passWord.equals(rePass)) {
                reEnterPass.setError("Not Equal to Password");
                return;
            }
            if (imageURI==null){
                Snackbar.make(findViewById(R.id.coordinator),"Please add a profile pic",
                        Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(eMail, passWord).addOnCompleteListener(task -> {
                mProgressbar.setVisibility(View.VISIBLE);
                if (task.isSuccessful()) {
                    userID = mAuth.getUid();
                    saveUserDetails(fNames, eMail, mAbout, imageURL);
                    startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                    mAuth.signOut();
                    finish();
                } else {
                    mProgressbar.setVisibility(View.INVISIBLE);
                    Toast.makeText(SignUpActivity.this, "An Error Occurred While Creating Your Account," +
                            " Check your details", Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void saveUserDetails(String name, String email, String about, String imageUrl) {
        User user = new User(name, email, about, imageUrl);
        mDatabaseReference.child(userID).setValue(user);
        Toast.makeText(this, "Created Successfully", Toast.LENGTH_SHORT).show();
    }

    public void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageURI = data.getData();
            uploadImage();
        }
    }

    private void uploadImage() {
        if (imageURI != null) {
            try {
                //compressing the image
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), imageURI);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
                byte[] fileInBytes = byteArrayOutputStream.toByteArray();

                //uploading the image
                StorageReference fileStorageReference = mStorageReference.child(imageURI.getLastPathSegment());
                UploadTask uploadTask = fileStorageReference.putBytes(fileInBytes);

                uploadTask.continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());

                    }
                    return fileStorageReference.getDownloadUrl();

                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        assert downloadUri != null;
                        imageURL = downloadUri.toString();
                    }

                }).addOnFailureListener(e -> Toast.makeText(this, "Failed to upload", Toast.LENGTH_SHORT).show());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "No Photo Selected", Toast.LENGTH_SHORT).show();
        }
    }
}
