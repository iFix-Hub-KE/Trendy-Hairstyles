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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class HomeFragment extends Fragment implements ItemClickListener {

    private ArrayList<Post> postList;
    private PostsAdapter adapter;
    private ProgressBar progressBar;
    private DatabaseReference databaseReference;
    private String userID = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        FloatingActionButton createPost = view.findViewById(R.id.add_post_fab);
        progressBar = view.findViewById(R.id.recycler_progressbar);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        postList = new ArrayList<>();
        fetchPosts(view);

        createPost.setOnClickListener(v -> NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_homeFragment_to_postFragment));

        return view;
    }

    private void initializeRecycler(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.posts_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PostsAdapter(postList,this);
        recyclerView.setAdapter(adapter);
    }

    private void fetchPosts(View view) {
        progressBar.setVisibility(View.VISIBLE);
        databaseReference = FirebaseDatabase.getInstance().getReference("posts");
        databaseReference.orderByChild("date_time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    postList.clear();
                    for (DataSnapshot i : snapshot.getChildren()) {
                        Post post = i.getValue(Post.class);
                        postList.add(post);

                        //to reverse the list coz firebase sorts data in ascending order
                        Collections.reverse(postList);
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
        int rt = post.getLikes() + 1;
        post.setLikes(rt);

        Query query = databaseReference.orderByChild("style_name").equalTo(post.getStyle_name());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot mSnap : snapshot.getChildren()) {
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
    public void dislikePost(Post post, int position) {
        int rt = post.getLikes() - 1;
        post.setLikes(rt);
        Toast.makeText(getContext(), "disliked post " + rt, Toast.LENGTH_SHORT).show();

        Query query = databaseReference.orderByChild("style_name").equalTo(post.getStyle_name());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot mSnap : snapshot.getChildren()) {
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
        Toast.makeText(getContext(), "saved post " + post.getStyle_photo_url() + " " + post.getStyle_name() + " " +
                post.getSalon_name() + " " + post.getStyle_price(), Toast.LENGTH_LONG).show();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userID).child("saved");
        Saved saved = new Saved(post.getStyle_photo_url(), post.getStyle_name(), post.getStyle_price(), post.getSalon_name(), true);
        String userID = FirebaseAuth.getInstance().getUid();
        assert userID != null;
        databaseReference.push().setValue(saved);
        Toast.makeText(getContext(), "Style Saved", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void unSavePost(Post post, int position) {

    }

}