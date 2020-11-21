package com.ifixhubke.trendyhairstyles;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class SavedFragment extends Fragment {

    ArrayList<Saved> savedList;
    SavedAdapter savedAdapter;
    DatabaseReference databaseReference;
    ProgressBar progressBar;
    private String userID = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_saved, container, false);

        progressBar = view.findViewById(R.id.saved_recycler_progressbar);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userID).child("saved");

        savedList = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot i : snapshot.getChildren()) {
                        Saved saved = i.getValue(Saved.class);
                        savedList.add(saved);

                        initializeRecycler(view);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
                else {
                    TextView no_saved_text = view.findViewById(R.id.no_saved_text);
                    ImageView no_saved = view.findViewById(R.id.no_saved);
                    progressBar.setVisibility(View.INVISIBLE);
                    no_saved.setVisibility(View.VISIBLE);
                    no_saved_text.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    private void initializeRecycler(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.saved_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        savedAdapter = new SavedAdapter(savedList);
        recyclerView.setAdapter(savedAdapter);
    }

}