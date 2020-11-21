package com.ifixhubke.trendyhairstyles;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private String userID = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_saved, container, false);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userID).child("saved");

        savedList = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot i : snapshot.getChildren()) {
                    Saved saved = i.getValue(Saved.class);
                    savedList.add(saved);

                    initializeRecycler(view);
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