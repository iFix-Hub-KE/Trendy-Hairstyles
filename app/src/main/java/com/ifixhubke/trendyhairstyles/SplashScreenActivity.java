package com.ifixhubke.trendyhairstyles;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = "SplashScreenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if(!isConnected()){
            showCustomDialog();
        }

        else {
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
                if (FirebaseAuth.getInstance().getCurrentUser() !=null){
                    startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
                }
                else{
                    startActivity(new Intent(SplashScreenActivity.this,SignInActivity.class));
                }
                finish();
            },4000);
        }

    }

    private void showCustomDialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashScreenActivity.this);
        alertDialogBuilder.setMessage("It seems you are not connect to the Internet, please turn on WIFI or Mobile Data");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Ok", (dialog, which) -> finish());
        alertDialogBuilder.show();
    }

    public boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifiNetworkInfo != null && wifiNetworkInfo.isConnected()) || (mobileNetworkInfo != null && mobileNetworkInfo.isConnected())){
            Log.d(TAG, "isConnected: connected");
            return true;
        }
        else{
            return false;
        }
    }
}