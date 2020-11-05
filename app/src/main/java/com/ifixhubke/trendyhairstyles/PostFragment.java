package com.ifixhubke.trendyhairstyles;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class PostFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageURI;
    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseReference;
    ProgressBar uploadProg;
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
        post_btn = view.findViewById(R.id.post_style_button);
        salon = view.findViewById(R.id.salon_name);
        price = view.findViewById(R.id.style_price);
        styleName = view.findViewById(R.id.style_name);
        capt = view.findViewById(R.id.emoji_entered);
        progressBar = view.findViewById(R.id.post_progressBar);
        chooseImage = view.findViewById(R.id.camera_to_choose_photo);
        uploadProg = view.findViewById(R.id.uploadProgressBar);

        mStorageReference = FirebaseStorage.getInstance().getReference("posts_photos");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("posts");

        chooseImage.setOnClickListener(v -> {
            openFileChooser();
        });


        post_btn.setOnClickListener(v -> {

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
            imageURI = data.getData();
        }
    }

    private void uploadImage() {

        if (imageURI != null) {
            progressBar.setVisibility(View.VISIBLE);
            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), imageURI);
            } catch (IOException e) {
                e.printStackTrace();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                byte[] data = baos.toByteArray();
                //uploading the image
                StorageReference fileStorageReference = mStorageReference.child(imageURI.getLastPathSegment());
                UploadTask uploadTask = fileStorageReference.putBytes(data);
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        // Continue with the task to get the download URL
                        return fileStorageReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            String downloadURL = downloadUri.toString();
                            Post post = new Post("Mercy Ketere",
                                    "https://9b16f79ca967fd0708d1-2713572fef44aa49ec323e813b06d2d9.ssl.cf2.rackcdn.com/1140x_a10-7_cTC/20161031arJaneHekima02-1-1568433305.jpg",
                                    styleName.getText().toString(), price.getText().toString(),
                                    salon.getText().toString(), capt.getText().toString(), downloadURL, "12/12/2020 12:56:10 EAT", 100);

                            FirebaseDatabase.getInstance().getReference().child("posts").child(UUID.randomUUID().toString()).setValue(post)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(getContext(), "Posted Successfully", Toast.LENGTH_SHORT).show();
                                            NavHostFragment.findNavController(PostFragment.this).navigate(R.id.action_postFragment_to_homeFragment);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getContext(), "Posted not Succesfull", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        }else {
            Toast.makeText(getActivity(), "no Image Selected", Toast.LENGTH_SHORT).show();
        }
    }
}