package com.ifixhubke.trendyhairstyles;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.contentcapture.DataShareWriteAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class SavedFragment extends Fragment {

    DatabaseReference databaseReference;
    ArrayList<Saved> savedArrayList;
    SavedAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_saved, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference("saved");

        savedArrayList = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot i : snapshot.getChildren()) {
                    Saved saved = i.getValue(Saved.class);
                    savedArrayList.add(saved);

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
        adapter = new SavedAdapter(savedArrayList);
        recyclerView.setAdapter(adapter);
    }
}