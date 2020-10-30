package com.ifixhubke.trendyhairstyles;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText mail,fullNames,aboutYou,password,reEnterPass;
    Button signUp;
    TextView back_ToLogin;
    FirebaseAuth mAuth;
    ProgressBar mProgressbar;
    String userID;


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
        back_ToLogin = findViewById(R.id.back_to_login);
        mProgressbar = findViewById(R.id.sign_up_progressBar);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() !=null){
            startActivity(new Intent(SignUpActivity.this,MainActivity.class));
            finish();
        }

        back_ToLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
            finish();
        });

        signUp.setOnClickListener(v -> {
            mProgressbar.setVisibility(View.VISIBLE);
            String eMail = mail.getText().toString().trim();
            String passWord = password.getText().toString().trim();
            String rePass = reEnterPass.getText().toString().trim();

            String fNames = fullNames.getText().toString().trim();
            String mAbout = aboutYou.getText().toString().trim();


            if (TextUtils.isEmpty(eMail)){ mail.setError("Email required!"); mProgressbar.setVisibility(View.INVISIBLE); return; }
            if (TextUtils.isEmpty(passWord)){ password.setError("Password required!"); mProgressbar.setVisibility(View.INVISIBLE); return; }
            if (passWord.length() < 6 ){ password.setError("Password should be 6 or more characters"); mProgressbar.setVisibility(View.INVISIBLE); return; }
            if(TextUtils.isEmpty(rePass)){ reEnterPass.setError("Re-Enter Password"); mProgressbar.setVisibility(View.INVISIBLE); return; }
            if (!passWord.equals(rePass)){ reEnterPass.setError("Not Equal to Password"); mProgressbar.setVisibility(View.INVISIBLE); return; }

        mAuth.createUserWithEmailAndPassword(eMail,passWord).addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                userID = mAuth.getUid();
                saveUserDetails(fNames,eMail,mAbout);

                mProgressbar.setVisibility(View.INVISIBLE);
                Toast.makeText(SignUpActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
                mAuth.signOut();
                finish();
            }
            else {
                mProgressbar.setVisibility(View.INVISIBLE);
                Toast.makeText(SignUpActivity.this, "An Error Occurred While Creating Your Account," +
                        " Check your Internet Connection", Toast.LENGTH_LONG).show();
            }
        });
        });
    }

    private void saveUserDetails(String name,String email, String about) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        User user = new User(name, email, about);
        databaseReference.child(userID).setValue(user);
    }
}
