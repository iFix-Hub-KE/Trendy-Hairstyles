package com.ifixhubke.trendyhairstyles;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.MediaStore;
import android.util.Log;
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
import java.util.Objects;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class PostFragment extends Fragment {

    private static final String TAG = "PostFragment";

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageURI;
    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseReference;
    ImageView chooseImage;
    ProgressBar progressBar;
    EditText capt;
    EditText styleName;
    EditText price;
    EditText salon;
    Button post_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        Log.d(TAG, "onCreateView: view created");
        post_btn = view.findViewById(R.id.post_style_button);
        salon = view.findViewById(R.id.salon_name);
        price = view.findViewById(R.id.style_price);
        styleName = view.findViewById(R.id.style_name);
        capt = view.findViewById(R.id.emoji_entered);
        progressBar = view.findViewById(R.id.post_progressBar);
        chooseImage = view.findViewById(R.id.camera_to_choose_photo);

        mStorageReference = FirebaseStorage.getInstance().getReference("posts_images");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("posts");

        chooseImage.setOnClickListener(v -> openFileChooser());


        post_btn.setOnClickListener(v -> {
            Log.d(TAG, "onCreateView: post button clicked");
            progressBar.setVisibility(View.VISIBLE);
            uploadImage();
        });
        return view;
    }

    public void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Log.d(TAG, "onActivityResult: the is data that has been picked");
            imageURI = data.getData();
        }
    }

    private void uploadImage() {

        if (imageURI != null) {
            Log.d(TAG, "uploadImage: the image uri is not empty");
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), imageURI);
                Log.d(TAG, "uploadImage: converting the image to a bitmap"+ bmp);
                //here you can choose quality factor in third parameter(ex. i chosen 25)
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
                byte[] fileInBytes = byteArrayOutputStream.toByteArray();
                Log.d(TAG, "uploadImage: the image has been compressed ");

                //uploading the image
                StorageReference fileStorageReference = mStorageReference.child(imageURI.getLastPathSegment());
                UploadTask uploadTask = fileStorageReference.putBytes(fileInBytes);
                uploadTask.continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }
                    // Continue with the task to get the download URL
                    Log.d(TAG, "then: upload task was successful" + fileStorageReference.getDownloadUrl());
                    return fileStorageReference.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        assert downloadUri != null;
                        String downloadURL = downloadUri.toString();
                        Post post = new Post("Mercy Ketere",
                                "https://9b16f79ca967fd0708d1-2713572fef44aa49ec323e813b06d2d9.ssl.cf2.rackcdn.com/1140x_a10-7_cTC/20161031arJaneHekima02-1-1568433305.jpg",
                                styleName.getText().toString(), price.getText().toString(),
                                salon.getText().toString(), capt.getText().toString(), downloadURL, "12/12/2020 12:56:10 EAT", "100");

                        mDatabaseReference.child(UUID.randomUUID().toString()).setValue(post)
                                .addOnSuccessListener(unused -> {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Log.d(TAG, "onSuccess: the post was successful");
                                    Toast.makeText(getContext(), "Posted Successfully", Toast.LENGTH_SHORT).show();
                                    NavHostFragment.findNavController(PostFragment.this).navigate(R.id.action_postFragment_to_homeFragment);
                                }).addOnFailureListener(e -> {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getContext(), "Posted not Successful", Toast.LENGTH_SHORT).show();
                                });
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();

            }
        }else {
            Log.d(TAG, "uploadImage: no image was selected");
            Toast.makeText(getActivity(), "no Image Selected", Toast.LENGTH_SHORT).show();
        }
    }
}