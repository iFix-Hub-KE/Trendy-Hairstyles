package com.ifixhubke.trendyhairstyles;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetDialog extends DialogFragment {
    private EditText mEmail;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.forgot_password, container, false);
        mEmail = view.findViewById(R.id.reset_mail);
        Button mConfirm = view.findViewById(R.id.txt_confirm);
        mContext = getActivity();

        mConfirm.setOnClickListener(v -> {

            if (!(TextUtils.isEmpty(mEmail.getText().toString()))){
                sendPasswordResetEmail(mEmail.getText().toString());
            }
        });
        return view;
    }

    public void sendPasswordResetEmail(String email) {

        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(mContext, "Password Reset Link Sent to Email", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "No User is Associated with that Email", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
