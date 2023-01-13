package com.example.myfcai.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfcai.Adapters.CourseRegistrationArrayAdapter;
import com.example.myfcai.Class.Course;
import com.example.myfcai.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {
    //ListView lv1G,lv2G,lv3CS,lv3IS,lv4CS,lv4IS;
    //ArrayList<Course> courseArrayList;
    //CourseRegistrationArrayAdapter arrayAdapter;
    DatabaseReference databaseReference;
    ArrayList<String> departments;
    ListView lv1G;
    TextView tvHours;
    Button btnRegister;
    int hours, exHours = 0, basicHours;
    ArrayList<Course> studentCourses, courseArrayList, l1, l2, l3, l4, newCourses;
    CourseRegistrationArrayAdapter arrayAdapter;
    String dep, level,studentName;
    double gpa;
    ArrayList<String> old;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        dep = getIntent().getStringExtra("dep");
        level = getIntent().getStringExtra("level");
        studentName = getIntent().getStringExtra("name");
        gpa = getIntent().getDoubleExtra("gpa", 0);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        btnRegister = findViewById(R.id.btn_register_activity_registration);
        btnRegister.setEnabled(false);
        btnRegister.setBackgroundResource(R.color.colorPrimaryLight);

        old = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("User Info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    if (s.child("status").getValue(String.class).equals("1")) {
                        old.add(s.getKey());
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        newCourses = new ArrayList<>();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (String m : old) {
                    FirebaseDatabase.getInstance().getReference().child("User Info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("courses").child(m).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Registration").child(m).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                }

                for (Course c : newCourses) {

                    HashMap<String, Object> course = new HashMap<>();
                    //course.put("code", c.getCode());
                    //course.put("student name", c.getName());
                    course.put("course name", c.getName());
                    course.put("credit", c.getCredit());
                    course.put("type", c.getType());
                    course.put("status", "1");
                    course.put("mid", 0);
                    course.put("practical", 0);
                    course.put("final", 0);
                    course.put("grade", "");

                    HashMap<String, Object> reg = new HashMap<>();
                    //course.put("code", c.getCode());
                    reg.put("student name", studentName);
                    reg.put("mid", 0);
                    reg.put("practical", 0);
                    reg.put("final", 0);

                    FirebaseDatabase.getInstance().getReference().child("User Info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("courses").child(c.getCode()).setValue(course);
                    FirebaseDatabase.getInstance().getReference().child("Registration").child(c.getCode()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(reg);

                    Intent intent = new Intent(RegistrationActivity.this, SplashActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }

            }
        });
        tvHours = findViewById(R.id.tv_hours_activity_registration);
        if (exHours != 0) {
            hours = exHours;
            basicHours = exHours;
        } else if (gpa >= 2) {
            hours = 18;
            basicHours = 18;
        } else {
            hours = 12;
            basicHours = 12;
        }
        tvHours.setText("You have " + hours + " hours");
        studentCourses = new ArrayList<>();
        databaseReference.child("User Info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentCourses.clear();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    studentCourses.add(new Course(snap.getKey(),snap.child("grade").getValue(String.class)));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        lv1G = findViewById(R.id.lv_1g_activity_registration);
        l1 = new ArrayList<>();
        l2 = new ArrayList<>();
        l3 = new ArrayList<>();
        l4 = new ArrayList<>();
        courseArrayList = new ArrayList<>();
        arrayAdapter = new CourseRegistrationArrayAdapter(RegistrationActivity.this, courseArrayList);
        lv1G.setAdapter(arrayAdapter);
        departments = new ArrayList<>();
        //departments.add("G");
        departments.add(dep);

        for (int i = 0; i < departments.size(); i++) {
            if (i==0){
                courseArrayList.clear();
                l1.clear();
                l2.clear();
                l3.clear();
                l4.clear();
            }


            final int finalI = i;

            databaseReference.child("Department").child(departments.get(i)).child("course").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String s = snapshot.child("status").getValue(String.class), n = snapshot.getKey(),pre=snapshot.child("pre").getValue(String.class);

                        if (s.equals("1") && !isExist(snapshot.getKey(), studentCourses) && (isExist(pre, studentCourses) || pre.equals(""))) {
                            switch (n.substring(2, 3)) {
                                case "1":
                                    l1.add(new Course(snapshot.getKey(), snapshot.child("name").getValue(String.class), snapshot.child("credit").getValue(Integer.class), snapshot.child("pre").getValue(String.class), snapshot.child("type").getValue(String.class), snapshot.child("status").getValue(String.class), false));
                                    break;
                                case "2":
                                    l2.add(new Course(snapshot.getKey(), snapshot.child("name").getValue(String.class), snapshot.child("credit").getValue(Integer.class), snapshot.child("pre").getValue(String.class), snapshot.child("type").getValue(String.class), snapshot.child("status").getValue(String.class), false));
                                    break;
                                case "3":
                                    l3.add(new Course(snapshot.getKey(), snapshot.child("name").getValue(String.class), snapshot.child("credit").getValue(Integer.class), snapshot.child("pre").getValue(String.class), snapshot.child("type").getValue(String.class), snapshot.child("status").getValue(String.class), false));
                                    break;
                                case "4":
                                    l4.add(new Course(snapshot.getKey(), snapshot.child("name").getValue(String.class), snapshot.child("credit").getValue(Integer.class), snapshot.child("pre").getValue(String.class), snapshot.child("type").getValue(String.class), snapshot.child("status").getValue(String.class), false));
                                    break;
                            }
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                    if ((finalI+1) == departments.size()) {
                        courseArrayList.addAll(l1);
                        courseArrayList.addAll(l2);
                        courseArrayList.addAll(l3);
                        courseArrayList.addAll(l4);
                    }
                    arrayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        lv1G.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course course = (Course) parent.getAdapter().getItem(position);
                if (!course.isSelect() && hours >= course.getCredit() && isPassedAll(course.getPre(), studentCourses) && !isExist(course.getCode(), studentCourses)) {//pre-request isPassed(selected code, student codes)--->isExist;
                    course.setSelect(true);
                    btnRegister.setEnabled(true);
                    btnRegister.setBackgroundResource(R.color.colorPrimaryDark);
                    hours = hours - course.getCredit();
                    tvHours.setText("You have " + hours + " hours");
                    newCourses.add(course);
                } else if (!course.isSelect() && hours < course.getCredit()) {
                    Toast.makeText(RegistrationActivity.this, "You don't have enough hours", Toast.LENGTH_SHORT).show();
                } else if (!course.isSelect() && !course.getPre().equals("") && !isExist(course.getPre(), studentCourses)) {
                    Toast.makeText(RegistrationActivity.this, "You don't have its Prerequisites", Toast.LENGTH_SHORT).show();
                } else if (course.isSelect()) {
                    course.setSelect(false);
                    hours = hours + course.getCredit();
                    tvHours.setText("You have " + hours + " hours");
                    if (hours == basicHours) {
                        btnRegister.setEnabled(false);
                        btnRegister.setBackgroundResource(R.color.colorPrimaryLight);
                    }
                    newCourses.remove(course);
                }
                arrayAdapter.notifyDataSetChanged();
            }
        });

    }

    boolean isPassedAll(String preCode, ArrayList<Course> sCourses) {
        boolean r = true;
        String[] preCodes = new String[preCode.length() / 5];
        for (int i = 0; i < preCode.length() / 5; i++) {
            preCodes[i] = preCode.substring(((i + 1) * 5) - 5, (i + 1) * 5);
        }
        for (String s : preCodes) {
            if (!isExist(s, sCourses)) {
                r = false;
                break;
            }
        }
        return r;

    }

    boolean isExist(String preCode, ArrayList<Course> sCourses) {
        boolean r = false;
        for (Course s : sCourses) {
            if (s.getCode().equals(preCode) && (s.getGrade().equals("A") || s.getGrade().equals("B") || s.getGrade().equals("C") || s.getGrade().equals("D"))) {
                r = true;
                break;
            }
        }
        return r;
    }
}
