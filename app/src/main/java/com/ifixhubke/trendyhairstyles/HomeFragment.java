package com.ifixhubke.trendyhairstyles;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;


public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        FloatingActionButton createPost = view.findViewById(R.id.add_post_fab);
        //Button logout = view.findViewById(R.id.button_logout);

       /* logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getActivity(),SignInActivity.class));
            getActivity().finish();
        });*/

        createPost.setOnClickListener(v -> NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_homeFragment_to_postFragment2));
        return view;
    }
}