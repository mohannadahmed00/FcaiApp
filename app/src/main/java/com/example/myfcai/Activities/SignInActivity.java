package com.example.myfcai.Activities;
//3D:99:DF:C2:84:79:DF:D2:79:B5:B7:09:0E:FA:27:7E:B4:6D:AC:F9

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfcai.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {
    TextView tvShow, tvForgotten, tvSignUp;
    EditText etEmail, etPassword;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            if (connected) {
                Intent intent = new Intent(this, SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }else {
                Toast.makeText(SignInActivity.this, "please check your connection", Toast.LENGTH_SHORT).show();
            }
        }

        tvShow = findViewById(R.id.tv_show_password_activity_sign_in);
        tvForgotten = findViewById(R.id.tv_forgotten_password_activity_sign_in);
        tvSignUp = findViewById(R.id.tv_sign_up_activity_sign_in);
        etEmail = findViewById(R.id.et_email_activity_sign_in);
        etPassword = findViewById(R.id.et_password_activity_sign_in);
        btnSignIn = findViewById(R.id.btn_sign_in_activity_sign_in);

        tvShow.setVisibility(View.GONE);
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etPassword.getText().length() > 0) {
                    tvShow.setVisibility(View.VISIBLE);
                } else {
                    tvShow.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        showPassword(etPassword, tvShow);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo nInfo = cm.getActiveNetworkInfo();
                boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
                if (connected){
                    signIn();
                }else {
                    Toast.makeText(SignInActivity.this, "please check your connection", Toast.LENGTH_SHORT).show();
                }
                
            }
        });

        tvForgotten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, ForgottenActivity.class));

            }
        });
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });

    }

    private void signIn() {
        String email, password;
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        if (email.equals("") || password.equals("")) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
        } else {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                public void onComplete(Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        checkIfEmailVerified();
                    } else {
                        Toast.makeText(SignInActivity.this, "Username or Password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void checkIfEmailVerified() {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void showPassword(EditText password, TextView show) {
        if (show.getText().equals("show")) {
            show.setText("hide");
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            show.setText("show");
            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        password.setSelection(password.length());
    }
}