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

public class SignUpActivity extends AppCompatActivity {

    EditText mail,fullNames,aboutYou,password,reEnterPass;
    Button signUp;
    TextView logIn;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mail = findViewById(R.id.username_field);
        fullNames = findViewById(R.id.full_names_edtx);
        aboutYou = findViewById(R.id.about_you_edtx);
        password = findViewById(R.id.password_edtx);
        reEnterPass = findViewById(R.id.re_password_edtx);
        signUp = findViewById(R.id.login_button);
        logIn = findViewById(R.id.create_account);

        mAuth = FirebaseAuth.getInstance();


        if (mAuth.getCurrentUser() !=null){
            startActivity(new Intent(SignUpActivity.this,MainActivity.class));
            finish();
        }

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eMail = mail.getText().toString().trim();
                String passWord = password.getText().toString().trim();
                String rePass = reEnterPass.getText().toString().trim();

                if (TextUtils.isEmpty(eMail)){ mail.setError("Email required!");return; }
                if (TextUtils.isEmpty(passWord)){ password.setError("Password required!");return; }
                if (passWord.length() < 6 ){ password.setError("Password should be 6 or more characters");return; }
                if(TextUtils.isEmpty(rePass)){ reEnterPass.setError("Re-Enter Password");return; }
                if (!passWord.equals(rePass)){ reEnterPass.setError("Not Equal to Password");return; }

                logIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
                    }
                });

            mAuth.createUserWithEmailAndPassword(eMail,passWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(SignUpActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
                        mAuth.signOut();
                        finish();
                    }
                    else {
                        Toast.makeText(SignUpActivity.this, "An Error Occurred While Creating Your Account", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            }
        });
    }
}
