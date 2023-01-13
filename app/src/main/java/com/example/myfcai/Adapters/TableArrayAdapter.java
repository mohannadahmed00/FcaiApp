package com.example.myfcai.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myfcai.Activities.AttendanceActivity;
import com.example.myfcai.Activities.ScanActivity;
import com.example.myfcai.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TableArrayAdapter extends ArrayAdapter {
    String courseName, userID, code, type, lastAttID;
    Date date;
    TextView tvCourse;
    Button btnAtt;
    DatabaseReference databaseReference;

    public TableArrayAdapter(Context context, ArrayList<String> tables, String type) {
        super(context, 0, tables);
        this.type = type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.table, parent, false);
        courseName = (String) getItem(position);
        date = Calendar.getInstance().getTime();
        code = userID + courseName;
        tvCourse = convertView.findViewById(R.id.tv_course_table);
        btnAtt = convertView.findViewById(R.id.btn_attendance_table);
        if (type.equals("t")) {
            btnAtt.setVisibility(View.VISIBLE);

        } else {
            btnAtt.setVisibility(View.GONE);

        }
        tvCourse.setText(courseName);


        btnAtt.setOnClickListener(e -> {
            databaseReference.child("Attendance").child(getItem(position).toString()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    ArrayList<String> dates = new ArrayList<>();
                    boolean isNew = false;
                    if (snapshot.getValue() == null) {
                        isNew = true;
                    }
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        if (snap.getKey().equals("last")) {
                            lastAttID = snap.getValue(String.class);
                        } else {
                            dates.add(snap.getKey().substring(0, 10));
                        }
                    }
                    String currentDate = date.toString().substring(0, 10);
                    boolean isCurrentDate = true;
                    for (String date : dates) {
                        if (!date.equals(currentDate)) {
                            isCurrentDate = false;
                            break;
                        }
                    }
                    if (isNew || (lastAttID.equals("") && !isCurrentDate)) {

                        String code = Calendar.getInstance().getTime() + userID + getItem(position).toString();
                        databaseReference.child("Attendance").child(getItem(position).toString()).child("last").setValue(code);
                        databaseReference.child("Attendance").child(getItem(position).toString()).child(code).setValue("");
                        Intent intent = new Intent(getContext(), AttendanceActivity.class);
                        intent.putExtra("code", code);
                        intent.putExtra("courseName", getItem(position).toString());
                        getContext().startActivity(intent);

                    } else if (!lastAttID.equals("")) {
                        Intent intent = new Intent(getContext(), AttendanceActivity.class);
                        intent.putExtra("code", lastAttID);
                        intent.putExtra("courseName", getItem(position).toString());
                        getContext().startActivity(intent);
                    } else {
                        //Toast.makeText(getContext(), "The attendance have already token today, be fair man!! ", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });


        });
        convertView.setOnClickListener(e -> {
            if (type.equals("s")) {
                Intent intent = new Intent(getContext(), ScanActivity.class);
                intent.putExtra("courseName", getItem(position).toString());
                intent.putExtra("date", Calendar.getInstance().getTime().toString().substring(0, 10));
                getContext().startActivity(intent);
            }
        });


        return convertView;
        //.child(code+Calendar.getInstance().getTime())
    }
}