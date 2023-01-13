package com.example.myfcai.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myfcai.Adapters.StudentCoursesArrayAdapter;
import com.example.myfcai.Adapters.TeacherCoursesArrayAdapter;
import com.example.myfcai.Class.Course;
import com.example.myfcai.Class.User;
import com.example.myfcai.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class CourseFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    Spinner sCourse;
    Button btnSubmit;
    String type, courseName, courseCode;
    DatabaseReference databaseReference;
    ArrayList<Course> courses;
    StudentCoursesArrayAdapter studentCoursesArrayAdapter;
    TeacherCoursesArrayAdapter teacherCoursesArrayAdapter;
    ListView lvCourses;
    ArrayList<String> coursesNames;
    ArrayAdapter adapter;
    ArrayList<User> students;
    boolean check = false;


    public CourseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        type = getArguments().getString("type");
        View view = inflater.inflate(R.layout.fragment_course, container, false);
        sCourse = view.findViewById(R.id.s_course_fragment_course);
        btnSubmit = view.findViewById(R.id.btn_activity_course);
        lvCourses = view.findViewById(R.id.lv_course_fragment_course);
        if (type.equals("s")) {
            //student
            btnSubmit.setVisibility(View.GONE);
            sCourse.setVisibility(View.GONE);


            courses = new ArrayList<>();
            studentCoursesArrayAdapter = new StudentCoursesArrayAdapter(getContext(), courses);
            lvCourses.setAdapter(studentCoursesArrayAdapter);

            databaseReference.child("User Info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("courses").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    courses.clear();
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        courses.add(new Course(snap.child("course name").getValue(String.class), snap.child("mid").getValue(Double.class), snap.child("practical").getValue(Double.class), snap.child("final").getValue(Double.class), snap.child("grade").getValue(String.class), snap.child("status").getValue(String.class)));
                        studentCoursesArrayAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } else {
            //teacher
            btnSubmit.setVisibility(View.VISIBLE);

            sCourse.setOnItemSelectedListener(this);
            courses = new ArrayList<>();
            coursesNames = new ArrayList<>();
            adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, coursesNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sCourse.setAdapter(adapter);

            databaseReference.child("User Info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("courses").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    courses.clear();
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        courses.add(new Course(s.getKey(), s.getValue(String.class), 0));
                        coursesNames.add(s.getValue(String.class));
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            students = new ArrayList<>();
            teacherCoursesArrayAdapter = new TeacherCoursesArrayAdapter(getContext(), students);
            lvCourses.setAdapter(teacherCoursesArrayAdapter);
            btnSubmit.setOnClickListener(e -> {

                teacherCoursesArrayAdapter.setGrades(courseCode);
                teacherCoursesArrayAdapter.notifyDataSetChanged();

                /*for (User student : students) {
                    HashMap<String, Object> course = new HashMap<>();

                    String stdID =student.getId();

                    course.put("status", "1");//final course.put("final", 0);
                    course.put("mid", 0);
                    course.put("practical", 0);
                    course.put("final", 0);
                    FirebaseDatabase.getInstance().getReference().child("User Info").child(stdID).child("courses").child(courseCode).child("mid").setValue(course);
                    FirebaseDatabase.getInstance().getReference().child("User Info").child(stdID).child("courses").child(courseCode).child("practical").setValue(course);
                    FirebaseDatabase.getInstance().getReference().child("User Info").child(stdID).child("courses").child(courseCode).child("final").setValue();


                    FirebaseDatabase.getInstance().getReference().child("User Info").child(stdID).child("courses").child(courseCode).child("grade").setValue(course);


                    HashMap<String, Object> reg = new HashMap<>();
                    //course.put("code", c.getCode());
                    reg.put("student name", studentName);
                    reg.put("mid", 0);
                    reg.put("practical", 0);
                    reg.put("final", 0);

                    FirebaseDatabase.getInstance().getReference().child("Registration").child(c.getCode()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(reg);

                }*/
            });

        }

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        courseName = coursesNames.get(position);
        for (Course x : courses) {
            if (x.getName().equals(courseName)) {
                courseCode = x.getCode();
                break;
            }
        }


        databaseReference.child("Registration").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                students.clear();
                for (DataSnapshot s : dataSnapshot.child(courseCode).getChildren()) {
                    students.add(new User(s.getKey(), s.child("student name").getValue(String.class), courseCode, s.child("mid").getValue(Double.class), s.child("practical").getValue(Double.class), s.child("final").getValue(Double.class)));
                    teacherCoursesArrayAdapter.notifyDataSetChanged();


                }
                if (students.size() == 0) {
                    students.clear();
                    teacherCoursesArrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}










        /*if (courseCode.substring(2, 3).equals("1") || courseCode.substring(2, 3).equals("2")) {
            databaseReference.child("User Info").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    students.clear();

                    databaseReference.child("Department").child("G").child("course").child(courseCode).child("students").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot s : dataSnapshot.getChildren()) {
                                final String id = s.getKey();
                                databaseReference.child("User Info").child(s.getKey()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String name = dataSnapshot.child("first name").getValue(String.class) + " " + dataSnapshot.child("last name").getValue(String.class);

                                        students.add(new User(id, name, courseCode, dataSnapshot.child("courses").child(courseCode).child("mid").getValue(Integer.class), dataSnapshot.child("courses").child(courseCode).child("practical").getValue(Integer.class), dataSnapshot.child("courses").child(courseCode).child("final").getValue(Integer.class)));
                                        teacherCoursesArrayAdapter.notifyDataSetChanged();
                                        //adapter
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } else {

            databaseReference.child("User Info").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    students.clear();



                    databaseReference.child("Department").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot s : dataSnapshot.getChildren()) {
                                if (!s.getKey().equals("G")) {
                                    for (DataSnapshot snap : s.getChildren()) {
                                        if (snap.getKey().equals(courseCode)) {
                                            check = true;
                                            break;//may
                                        }
                                    }

                                    if (check) {

                                        databaseReference.child("Department").child(s.getKey()).child("course").child(courseCode).child("students").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                for (DataSnapshot s : dataSnapshot.getChildren()) {
                                                    final String id = s.getKey();
                                                    databaseReference.child("User Info").child(s.getKey()).addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                            String name = dataSnapshot.child("first name").getValue(String.class) + " " + dataSnapshot.child("last name").getValue(String.class);
                                                            students.add(new User(id, name, courseCode, dataSnapshot.child("courses").child(courseCode).child("mid").getValue(Integer.class), dataSnapshot.child("courses").child(courseCode).child("practical").getValue(Integer.class), dataSnapshot.child("courses").child(courseCode).child("final").getValue(Integer.class)));
                                                            teacherCoursesArrayAdapter.notifyDataSetChanged();
                                                            //adapter
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });







                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }*/
