package com.ifixhubke.trendyhairstyles;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavAction;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class HomeFragment extends Fragment implements ItemClickListener{

    RecyclerView recyclerView;
    private ArrayList<Post> postList;
    PostsAdapter adapter;
    ProgressBar progressBar;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        FloatingActionButton createPost = view.findViewById(R.id.add_post_fab);
        progressBar = view.findViewById(R.id.recycler_progressbar);

        postList = new ArrayList<>();

        fetchPosts(view);

        createPost.setOnClickListener(v ->{
            NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_homeFragment_to_postFragment);
        });

        return view;
    }

    private void initializeRecycler(View view){
        recyclerView = view.findViewById(R.id.posts_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PostsAdapter(postList, getContext(),this);
        recyclerView.setAdapter(adapter);
    }

    private void fetchPosts(View view){
        progressBar.setVisibility(View.VISIBLE);
         databaseReference = FirebaseDatabase.getInstance().getReference("posts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    postList.clear();
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

    @Override
    public void likePost(Post post, int position) {

        int rt = post.getLikes()+1;
        post.setLikes(rt);
        Toast.makeText(getContext(), "like post "+rt, Toast.LENGTH_SHORT).show();

        Query query = databaseReference.orderByChild("style_name").equalTo(post.getStyle_name());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot mSnap : snapshot.getChildren()){
                    mSnap.child("likes").getRef().setValue(rt);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Toast.makeText(getContext(), "like post "+ post.getLikes(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void dislikePost(Post post, int position) {
        int rt = post.getLikes()-1;
        post.setLikes(rt);
        Toast.makeText(getContext(), "disliked post "+rt, Toast.LENGTH_SHORT).show();

        Query query = databaseReference.orderByChild("style_name").equalTo(post.getStyle_name());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot mSnap : snapshot.getChildren()){
                    mSnap.child("likes").getRef().setValue(rt);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void savePost(Post post, int position) {
        Toast.makeText(getContext(), "saved post "+post.getStyle_photo_url() +" "+ post.getStyle_name() +" "+
                post.getSalon_name() +" "+ post.getStyle_price(), Toast.LENGTH_LONG).show();
        databaseReference = FirebaseDatabase.getInstance().getReference("saved");
        Saved saved = new Saved(post.getStyle_photo_url(),post.getStyle_name(),post.getStyle_price(),post.getSalon_name(), true);
        String userID = FirebaseAuth.getInstance().getUid();
        databaseReference.child(userID).setValue(saved);
        Toast.makeText(getContext(), "Style Saved", Toast.LENGTH_SHORT).show();

    }
}