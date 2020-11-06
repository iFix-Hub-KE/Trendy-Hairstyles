package com.ifixhubke.trendyhairstyles;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    private List<Post> postList;
    RecyclerView.Adapter adapter;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        FloatingActionButton createPost = view.findViewById(R.id.add_post_fab);
        progressBar = view.findViewById(R.id.recycler_progressbar);

        postList = new ArrayList<>();

        fetchPosts(view);

        createPost.setOnClickListener(v -> NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_homeFragment_to_postFragment2));
        return view;
    }

    private void initializeRecycler(View view){
        recyclerView = view.findViewById(R.id.posts_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PostsAdapter(postList, getContext());
        recyclerView.setAdapter(adapter);
    }

    private void fetchPosts(View view){
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("posts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot i: snapshot.getChildren()){
                        Post post = i.getValue(Post.class);
                        postList.add(post);
                        progressBar.setVisibility(View.INVISIBLE);
                        initializeRecycler(view);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}