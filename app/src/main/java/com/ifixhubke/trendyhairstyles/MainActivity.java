package com.ifixhubke.trendyhairstyles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    DrawerLayout mDrawer;
    NavigationView mNavigationView;
    ActionBarDrawerToggle mActionBarDrawerToggle;
    BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawer = findViewById(R.id.drawerLayout);
        mNavigationView = findViewById(R.id.nav_view);
        mBottomNavigationView = findViewById(R.id.bottomNavigationView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Home");

        mActionBarDrawerToggle = new ActionBarDrawerToggle(this,mDrawer,R.string.open,R.string.close);
        mDrawer.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

       mNavigationView.setNavigationItemSelectedListener(this);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //setting the fragment that will be loaded when the app starts
        Fragment fragment = new HomeFragment();
        addFragment(fragment);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mActionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displaySelectedNavMenu(int itemID){
        switch (itemID){
            case R.id.nav_switch:{
                Toast.makeText(this, "Dark mode switch", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.nav_about:{
                Fragment fragment = new AboutFragment();
                addFragment(fragment);
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.nav_report:{
                Fragment fragment = new ReportFragment();
                addFragment(fragment);
                Toast.makeText(this, "Report", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.nav_settings:{
                Fragment fragment = new SettingsFragment();
                addFragment(fragment);
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.nav_logout:{
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                break;
            }

        }
        mDrawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        displaySelectedNavMenu(menuItem.getItemId());
        return true;
    }

    @Override
    public void onBackPressed() {
        if(mDrawer.isDrawerOpen(GravityCompat.START)){
            mDrawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected( MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.bottom_nav_home:{
                            Fragment fragment = new HomeFragment();
                            addFragment(fragment);
                            Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        case R.id.bottom_nav_notifications:{
                            Fragment fragment = new NotificationsFragment();
                            addFragment(fragment);
                            Toast.makeText(MainActivity.this, "Notifications", Toast.LENGTH_SHORT).show();
                            return true;

                        }
                        case R.id.bottom_nav_saved:{
                            Fragment fragment = new SavedFragment();
                            addFragment(fragment);
                            Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }
                    return false;
                }
            };

    private void addFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("")
                .replace(R.id.fragment_container,fragment)
                .commit();
    }

}