package com.example.myfcai.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myfcai.Adapters.StdAttArrayAdapter;
import com.example.myfcai.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class AttendanceActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    String userID, courseName, code;
    ImageView ivQR;
    Button btnEnd;
    ListView lvStd;
    StdAttArrayAdapter stdAttArrayAdapter;
    ArrayList<String> stdNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        courseName = getIntent().getStringExtra("courseName");
        code = getIntent().getStringExtra("code");
        ivQR = findViewById(R.id.iv_activity_att);
        lvStd = findViewById(R.id.lv_activity_att);
        btnEnd = findViewById(R.id.btn_activity_att);
        if (!code.isEmpty()) {
            //create QR
            QRGEncoder qrgEncoder = new QRGEncoder(code, null, QRGContents.Type.TEXT, 500);
            Bitmap qrBitmap = qrgEncoder.getBitmap();
            ivQR.setImageBitmap(qrBitmap);
        }
        stdNames = new ArrayList<>();
        stdAttArrayAdapter = new StdAttArrayAdapter(getBaseContext(), stdNames);
        databaseReference.child("Attendance").child(courseName).child(code).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                stdNames.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String x=snap.getValue(String.class);
                    stdNames.add(x);
                    stdAttArrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        lvStd.setAdapter(stdAttArrayAdapter);

        btnEnd.setOnClickListener(e -> {

            databaseReference.child("Attendance").child(courseName).child("last").setValue("");
            Intent intent = new Intent(AttendanceActivity.this, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);


        });


    }
}