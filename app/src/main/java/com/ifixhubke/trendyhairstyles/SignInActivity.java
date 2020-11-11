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

public class SignInActivity extends AppCompatActivity {

    private EditText mEmail, mPassword;
    private FirebaseAuth mAuth;
    private ProgressBar mProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mEmail = findViewById(R.id.username_field);
        mPassword = findViewById(R.id.password_field);
        Button signIn = findViewById(R.id.login_button);
        TextView createAcc = findViewById(R.id.create_account);
        TextView forgotPass = findViewById(R.id.forgot_password);
        mProgressbar = findViewById(R.id.sign_in_progressbar);
        mAuth = FirebaseAuth.getInstance();

        signIn.setOnClickListener(v -> {

            mProgressbar.setVisibility(View.VISIBLE);

            String eMail = mEmail.getText().toString().trim();
            String passWord = mPassword.getText().toString().trim();

            if (TextUtils.isEmpty(eMail)) {
                mEmail.setError("Email required!");
                mProgressbar.setVisibility(View.INVISIBLE);
                return;
            }
            if (TextUtils.isEmpty(passWord)) {
                mPassword.setError("Password required!");
                mProgressbar.setVisibility(View.INVISIBLE);
                return;
            }
            if (passWord.length() < 6) {
                mPassword.setError("Password should be 6 or more characters");
                mProgressbar.setVisibility(View.INVISIBLE);
                return;
            }

            mAuth.signInWithEmailAndPassword(eMail, passWord).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    mProgressbar.setVisibility(View.INVISIBLE);
                    Toast.makeText(SignInActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                    finish();
                } else {
                    mProgressbar.setVisibility(View.INVISIBLE);
                    Toast.makeText(SignInActivity.this, "An Error Occurred," +
                            "Check your Details Again", Toast.LENGTH_LONG).show();
                }
            });

        });

        createAcc.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            finish();
        });

        forgotPass.setOnClickListener(v -> {
            PasswordResetDialog dialog = new PasswordResetDialog();
            dialog.show(getSupportFragmentManager(), "dialog_password_reset");
        });
    }
}