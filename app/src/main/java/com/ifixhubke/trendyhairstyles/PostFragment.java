package com.ifixhubke.trendyhairstyles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

@SuppressWarnings("ConstantConditions")
public class PostFragment extends Fragment {

    private static final String TAG = "PostFragment";
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageURI;
    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseReference;
    private ProgressBar progressBar;
    private EditText capt;
    private EditText styleName;
    private EditText price;
    private EditText salon;
    private String sharedProfileName, sharedProfilePicture;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_post, container, false);

        SharedPreferences sharedPreferences = this.getActivity()
                .getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);

        sharedProfileName = sharedPreferences.getString("USERNAME", "");
        sharedProfilePicture = sharedPreferences.getString("PROFILE_URL", "");

        Button post_btn = view.findViewById(R.id.post_style_button);
        salon = view.findViewById(R.id.salon_name);
        price = view.findViewById(R.id.style_price);
        styleName = view.findViewById(R.id.style_name);
        capt = view.findViewById(R.id.emoji_entered);
        progressBar = view.findViewById(R.id.post_progressBar);
        ImageView chooseImage = view.findViewById(R.id.camera_to_choose_photo);

        mStorageReference = FirebaseStorage.getInstance().getReference("posts_images");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("posts");

        chooseImage.setOnClickListener(v -> openFileChooser());

        post_btn.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            uploadImage();
        });

        return view;
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
        }
    }

    private void uploadImage() {

        if (imageURI != null) {
            try {

                //all this code is for trying to compress the image
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), imageURI);
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
                        String downloadURL = downloadUri.toString();
                        String date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

                        Post post = new Post(sharedProfileName,
                                sharedProfilePicture,
                                styleName.getText().toString(), price.getText().toString(),
                                salon.getText().toString(), capt.getText().toString(), downloadURL, date, 0);

                        mDatabaseReference.child(UUID.randomUUID().toString()).setValue(post)
                                .addOnSuccessListener(unused -> {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getContext(), "Posted Successfully", Toast.LENGTH_SHORT).show();
                                    NavHostFragment.findNavController(PostFragment.this).navigate(R.id.action_postFragment_to_homeFragment);
                                }).addOnFailureListener(e -> {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getContext(), "Post not Successful", Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();

            }
        } else {
            Toast.makeText(getActivity(), "No Picture Selected", Toast.LENGTH_SHORT).show();
        }
    }
}