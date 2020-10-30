package com.ifixhubke.trendyhairstyles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity  {

    DrawerLayout mDrawer;
    NavigationView mNavigationView;
    BottomNavigationView mBottomNavigationView;
    AppBarConfiguration appBarConfiguration;
    NavController navController;
    private static final String TAG = "MainActivity";

    TextView tv_name,tv_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationView = findViewById(R.id.bottomNavigationView);
        mDrawer = findViewById(R.id.drawerLayout);
        mNavigationView = findViewById(R.id.navigationView);
        View headerView = mNavigationView.getHeaderView(0);
        tv_name = headerView.findViewById(R.id.profile_name);
        tv_about = headerView.findViewById(R.id.profile_about);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        //Setup Bottom Navigation View
        NavigationUI.setupWithNavController(mBottomNavigationView, navController);

       //Setup Top Back button
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).setOpenableLayout(mDrawer).build();
        NavigationUI.setupActionBarWithNavController(this,navController,mDrawer);

        //Setup Navigation Drawer
        NavigationUI.setupWithNavController(mNavigationView,navController);

        fetchUserProfile();

    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController,appBarConfiguration);
    }

    private void fetchUserProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userid = user.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = Objects.requireNonNull(dataSnapshot.child("full_names").getValue()).toString();
                String about = Objects.requireNonNull(dataSnapshot.child("about_you").getValue()).toString();
               tv_name.setText(name);
               tv_about.setText(about);
                Log.d(TAG, "onDataChange: "+name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}