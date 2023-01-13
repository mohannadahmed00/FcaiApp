package com.example.myfcai.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.example.myfcai.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    TextView tvWelcome;
    String imgURI, name,type, age, phone, email, dep, level;
    double gpa;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        type="";

        databaseReference = FirebaseDatabase.getInstance().getReference().child("User Info");
        tvWelcome = findViewById(R.id.tv_welcome_activity_splash);

        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                while (type.equals("")){
                    type=dataSnapshot.child("type").getValue(String.class);
                }

                name = dataSnapshot.child("first name").getValue(String.class) + " " + dataSnapshot.child("last name").getValue(String.class);
                if (type.equals("student")){
                    tvWelcome.setText("Study hard, " + name);
                    dep= dataSnapshot.child("dep").getValue(String.class);
                    level=dataSnapshot.child("level").getValue(String.class);
                    gpa=dataSnapshot.child("gpa").getValue(Double.class);

                }else {
                    tvWelcome.setText("Work hard, " + name);
                }

                imgURI = dataSnapshot.child("user image").getValue(String.class);
                age = dataSnapshot.child("age").getValue(String.class);
                phone = dataSnapshot.child("phone").getValue(String.class);
                email = dataSnapshot.child("email").getValue(String.class);


            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (type.equals("student")){
                    Intent intent = new Intent(SplashActivity.this, StudentActivity.class);
                    intent.putExtra("imgURI", imgURI);
                    intent.putExtra("name", name);
                    intent.putExtra("age", age);
                    intent.putExtra("phone", phone);
                    intent.putExtra("email", email);
                    intent.putExtra("dep", dep);
                    intent.putExtra("level", level);
                    intent.putExtra("gpa", gpa);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else/* if (type.equals("teacher"))*/{
                    Intent intent = new Intent(SplashActivity.this, TeacherActivity.class);
                    intent.putExtra("imgURI", imgURI);
                    intent.putExtra("name", name);
                    intent.putExtra("age", age);
                    intent.putExtra("phone", phone);
                    intent.putExtra("email", email);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

            }
        }, 3000);
    }
}