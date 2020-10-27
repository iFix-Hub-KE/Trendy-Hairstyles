package com.ifixhubke.trendyhairstyles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout mDrawer;
    Toolbar mToolbar;
    NavigationView mNavigationView;
    ActionBarDrawerToggle mActionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // mToolbar = findViewById(R.id.my_toolbar);
        mDrawer = findViewById(R.id.drawerLayout);
        mNavigationView = findViewById(R.id.nav_view);
       // mToolbar.setTitle("Home");
       // setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Home");

        mActionBarDrawerToggle = new ActionBarDrawerToggle(this,mDrawer,R.string.open,R.string.close);
        mDrawer.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);
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
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.nav_report:{
                Toast.makeText(this, "Report", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.nav_settings:{
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
}