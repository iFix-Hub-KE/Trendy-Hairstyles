package com.ifixhubke.trendyhairstyles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    EditText mEmail,mPassword;
    Button signIn;
    TextView createAcc,forgotPass;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mEmail = findViewById(R.id.username_field);
        mPassword = findViewById(R.id.password_field);
        signIn = findViewById(R.id.login_button);
        createAcc = findViewById(R.id.create_account);
        forgotPass = findViewById(R.id.forgot_password);
        mAuth = FirebaseAuth.getInstance();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String eMail = mEmail.getText().toString().trim();
                String passWord = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(eMail)){
                    mEmail.setError("Email required!");
                    return;
                }
                if (TextUtils.isEmpty(passWord)){
                    mPassword.setError("Password required!");
                    return;
                }
                if (passWord.length() < 6 ){
                    mPassword.setError("Password should be 6 or more characters");
                    return;
                }

                mAuth.signInWithEmailAndPassword(eMail,passWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(SignInActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignInActivity.this,MainActivity.class));
                            finish();
                        }
                        else {
                            Toast.makeText(SignInActivity.this, "An Error Occurred", Toast.LENGTH_SHORT).show();


                        }
                    }
                });

            }
        });

        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
            }
        });

    }
}