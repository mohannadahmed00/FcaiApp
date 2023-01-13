package com.example.myfcai.Fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myfcai.Adapters.TaskArrayAdapter;
import com.example.myfcai.Class.Course;
import com.example.myfcai.Class.Post;
import com.example.myfcai.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class TaskFragment extends Fragment {
    Button btnPost;//45936428705
    ImageView ivAddPost;
    EditText etPost;
    ArrayList<Course> courses;
    ArrayList<String> coursesNames;

    ArrayList<Post> postsList;
    TaskArrayAdapter taskArrayAdapter;
    String type;
    Dialog dialog;
    View view;
    DatabaseReference databaseReference;
    Spinner sPost, sCourse;
    ArrayAdapter adapter;
    ListView lvDiagnosis;
    String userID, time, writerName, writerImg, dep, courseCode, courseName;
    TextView tvNewYear;

    Button btnAddTask;


    public TaskFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        type = getArguments().getString("type");
        dep = getArguments().getString("dep");
        view = inflater.inflate(R.layout.fragment_post, container, false);

        ivAddPost = view.findViewById(R.id.btn_add_post_fragment_post);


        sCourse = view.findViewById(R.id.s_post_fragment_post);
        lvDiagnosis = view.findViewById(R.id.lv_post_fragment_post);
        tvNewYear = view.findViewById(R.id.tv_new_year_fragment_post);
        postsList = new ArrayList<>();
        taskArrayAdapter = new TaskArrayAdapter(getContext(), postsList,type);
        lvDiagnosis.setAdapter(taskArrayAdapter);

        courses = new ArrayList<>();
        coursesNames = new ArrayList<>();
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, coursesNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.write_post);
        etPost = dialog.findViewById(R.id.et_write_post);
        sPost = dialog.findViewById(R.id.s_course_write_post);
        btnPost = dialog.findViewById(R.id.btn_write_post);


        sCourse.setAdapter(adapter);
        sCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                courseName = coursesNames.get(position);



                for (int i = 0; i < courses.size(); i++) {
                    if (courses.get(i).getName().equals(courseName)) {
                        courseCode = courses.get(i).getCode();
                        if (!type.equals("s")) {
                            sPost.setSelection(i);
                        }
                        break;
                    }
                }


                databaseReference.child("Task").child(courseCode).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        postsList.clear();
                        for (DataSnapshot s : dataSnapshot.getChildren()) {
                            postsList.add(new Post(s.getKey(), s.child("writer image").getValue(String.class), s.child("writer name").getValue(String.class), s.child("course name").getValue(String.class), s.child("time").getValue(String.class), s.child("post").getValue(String.class)));
                            taskArrayAdapter.notifyDataSetChanged();
                        }

                        for (int i = 0; i < courses.size(); i++) {
                            if (courses.get(i).getCode().equals(dataSnapshot.getKey())) {
                                sCourse.setSelection(i);
                                break;
                            }
                        }
                        if (postsList.size() != 0) {
                            tvNewYear.setVisibility(View.GONE);

                        } else {
                            tvNewYear.setVisibility(View.VISIBLE);
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
        });
        if (type.equals("s")) {
            //student
            ivAddPost.setVisibility(View.GONE);
            databaseReference.child("User Info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("courses").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    courses.clear();
                    coursesNames.clear();
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        courses.add(new Course(s.getKey(), s.child("course name").getValue(String.class), 0));
                        coursesNames.add(s.child("course name").getValue(String.class));
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            //teacher
            databaseReference.child("User Info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("courses").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    courses.clear();
                    coursesNames.clear();
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


            sPost.setAdapter(adapter);
            sPost.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    courseName = coursesNames.get(position);
                    /*for (Course x : courses) {
                        if (x.getName().equals(courseName)) {
                            courseCode = x.getCode();
                            break;
                        }
                    }*/
                    for (int i = 0; i < courses.size(); i++) {
                        if (courses.get(i).getName().equals(courseName)) {
                            courseCode = courses.get(i).getCode();
                            sCourse.setSelection(i);
                            break;
                        }
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });




            ivAddPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    databaseReference.child("User Info").child(userID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            writerImg = dataSnapshot.child("user image").getValue(String.class);
                            writerName = dataSnapshot.child("first name").getValue(String.class) + " " + dataSnapshot.child("last name").getValue(String.class);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date date = new Date();
                    time = formatter.format(date);
                    btnPost.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HashMap<String, Object> post = new HashMap<>();
                            post.put("writer image", writerImg);
                            post.put("writer name", writerName);
                            post.put("course name", courseName);
                            post.put("time", time);
                            post.put("post", etPost.getText().toString().trim());
                            databaseReference.child("Task").child(courseCode).push().setValue(post);
                            Toast.makeText(getActivity(), "finally !!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();


                        }
                    });
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            });

        }

        return view;
    }
}