package com.ifixhubke.trendyhairstyles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


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
    TextView tv_name,tv_about;
    private static final String TAG = "MainActivity";

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

        //Switch for Dark Mode
        mNavigationView.getMenu().findItem(R.id.nav_dark_mode).setActionView(new Switch(this));

        // To set switch is on/off:
        ((Switch) mNavigationView.getMenu().findItem(R.id.nav_dark_mode).getActionView()).setChecked(false);

        ((Switch) mNavigationView.getMenu().findItem(R.id.nav_dark_mode).getActionView())
                .setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if(isChecked) {
                        Toast.makeText(MainActivity.this, "Dark Mode Enabled", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Dark Mode Disabled", Toast.LENGTH_SHORT).show();
                    }
                });

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        //Setup Bottom Navigation View
        NavigationUI.setupWithNavController(mBottomNavigationView, navController);

       //Setup Top Back post_style_button
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).setOpenableLayout(mDrawer).build();
        NavigationUI.setupActionBarWithNavController(this,navController,mDrawer);

        //Setup Navigation Drawer
        NavigationUI.setupWithNavController(mNavigationView,navController);

        fetchUserProfile();

        //Logout user
        MenuItem logout = mNavigationView.getMenu().findItem(R.id.nav_logout);
        logout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Logged out succesfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,SignInActivity.class));
                finish();
                return true;
            }
        });
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