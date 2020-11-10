package com.ifixhubke.trendyhairstyles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity  {

    DrawerLayout mDrawer;
    NavigationView mNavigationView;
    BottomNavigationView mBottomNavigationView;
    AppBarConfiguration appBarConfiguration;
    NavController navController;
    Toolbar mToolbar;
    TextView tv_name,tv_about;
    String name;
    String profileURL;
    String about;
    CircleImageView profileImage;

    private static final String TAG = "MainActivity";
    private String sharedPrefFile = "sharedPreferences";
    private SharedPreferences sharedPreferences;

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
        profileImage = headerView.findViewById(R.id.poster_profile_image);

        sharedPreferences  = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);



        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

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
        logout.setOnMenuItemClickListener(item -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(MainActivity.this, "Logged out succesfully!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this,SignInActivity.class));
            finish();
            return true;
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
                name = Objects.requireNonNull(dataSnapshot.child("full_names").getValue()).toString();
                about = Objects.requireNonNull(dataSnapshot.child("about_you").getValue()).toString();
                profileURL = Objects.requireNonNull(dataSnapshot.child("profile_url").getValue()).toString();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("USERNAME",name);
                editor.putString("PROFILE_URL",profileURL);
                editor.putString("ABOUT",about);
                editor.apply();
                editor.commit();
                Toast.makeText(MainActivity.this, sharedPreferences.getString("USERNAME","default"), Toast.LENGTH_SHORT).show();

               tv_name.setText(sharedPreferences.getString("USERNAME","default"));
               tv_about.setText(sharedPreferences.getString("ABOUT","default"));
                Picasso.get()
                        .load(sharedPreferences.getString("PROFILE_URL","default"))
                        .placeholder(R.drawable.ic_profile_user)
                        .fit()
                        .centerCrop()
                        .into(profileImage);
                Log.d(TAG, "onDataChange: "+name+ " "+profileURL);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}

