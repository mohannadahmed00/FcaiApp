package com.example.myfcai.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfcai.Class.Loading;
import com.example.myfcai.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgottenActivity extends AppCompatActivity {
    EditText etEmail;
    Button btnReset;
    TextView tvRemember;
    Loading loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten);

        loading=new Loading(ForgottenActivity.this);

        etEmail = findViewById(R.id.et_email_activity_forgotten_password);
        btnReset = findViewById(R.id.btn_reset_activity_forgotten_password);
        tvRemember = findViewById(R.id.tv_remember_activity_forgotten_password);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResetPasswordEmail();
            }
        });
        tvRemember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void sendResetPasswordEmail() {
        String email = etEmail.getText().toString();
        if (!email.equals("")) {
            loading.startLoading();
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                public void onComplete(Task<Void> task) {
                    if (task.isSuccessful()) {
                        loading.dismissLoading();
                        Toast.makeText(ForgottenActivity.this, "Password reset email is sent check your mail", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ForgottenActivity.this, SignInActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        loading.dismissLoading();
                        Toast.makeText(ForgottenActivity.this, "Email is incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(ForgottenActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
        }
    }
}