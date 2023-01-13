package com.example.myfcai.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myfcai.Class.User;
import com.example.myfcai.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TeacherCoursesArrayAdapter extends ArrayAdapter<User> {
    EditText etMid, etPractical, etFinal;
    TextView tvName;
    LinearLayout linearLayout;
    String status, code;
    User user;
    int pos;
    ArrayList<User> mid, pra, fin;

    public TeacherCoursesArrayAdapter(@NonNull Context context, ArrayList<User> users) {
        super(context, 0, users);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.teacher_course, parent, false);
        }

        user = (User) getItem(position);
        //this.pos=position;
        mid = new ArrayList<>();
        pra = new ArrayList<>();
        fin = new ArrayList<>();


        linearLayout = convertView.findViewById(R.id.ll_back_teacher_course);
        tvName = convertView.findViewById(R.id.tv_name_teacher_course);
        etMid = convertView.findViewById(R.id.tv_mid_teacher_course);
        etPractical = convertView.findViewById(R.id.tv_practical_teacher_course);
        etFinal = convertView.findViewById(R.id.tv_final_teacher_course);

        tvName.setText(user.getName());


        etMid.setText(String.valueOf(user.getMid()));
        etMid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()!=0){
                    User user=getItem(position);
                    mid.add(new User(user.getId(), Double.parseDouble(s.toString())));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        etPractical.setText(String.valueOf(user.getPractical()));
        etPractical.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length()!=0){
                    User user=getItem(position);
                    pra.add(new User(user.getId(), Double.parseDouble(s.toString())));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        etFinal.setText(String.valueOf(user.getFinalG()));
        etFinal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()!=0){
                    User user=getItem(position);
                    fin.add(new User(user.getId(), Double.parseDouble(s.toString())));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return convertView;
    }

    public void setGrades(String code) {
        //String test=etMid.getText().toString(),name=getItem(pos).getName();
        if (mid.size()!=0){
            for (User user : mid) {
                String stdID = user.getId();
                Double grade = user.getGrade();
                FirebaseDatabase.getInstance().getReference().child("User Info").child(stdID).child("courses").child(code).child("mid").setValue(grade);
                FirebaseDatabase.getInstance().getReference().child("Registration").child(code).child(stdID).child("mid").setValue(grade);
            }
        }

        if (pra.size()!=0) {
            for (User user : pra) {
                String stdID = user.getId();
                Double grade = user.getGrade();
                FirebaseDatabase.getInstance().getReference().child("User Info").child(stdID).child("courses").child(code).child("practical").setValue(grade);
                FirebaseDatabase.getInstance().getReference().child("Registration").child(code).child(stdID).child("practical").setValue(grade);
            }
        }

        if (fin.size()!=0) {
            for (User user : fin) {
                String stdID = user.getId();
                Double grade = user.getGrade();
                FirebaseDatabase.getInstance().getReference().child("User Info").child(stdID).child("courses").child(code).child("final").setValue(grade);
                FirebaseDatabase.getInstance().getReference().child("Registration").child(code).child(stdID).child("final").setValue(grade);
            }
        }



    }










        /*if (!etMid.getText().toString().equals("")){
            
            FirebaseDatabase.getInstance().getReference().child("User Info").child(stdID).child("courses").child(code).child("mid").setValue(Double.valueOf(etMid.getText().toString()));
            FirebaseDatabase.getInstance().getReference().child("Registration").child(code).child(stdID).child("mid").setValue(Double.valueOf(etMid.getText().toString()));
        }
        if (!etPractical.getText().toString().equals("")){
            FirebaseDatabase.getInstance().getReference().child("User Info").child(stdID).child("courses").child(code).child("practical").setValue(Double.valueOf(etPractical.getText().toString()));
            FirebaseDatabase.getInstance().getReference().child("Registration").child(code).child(stdID).child("practical").setValue(Double.valueOf(etPractical.getText().toString()));
        }
        if (!etFinal.getText().toString().equals("")){
            FirebaseDatabase.getInstance().getReference().child("User Info").child(stdID).child("courses").child(code).child("final").setValue(Double.valueOf(etFinal.getText().toString()));
            FirebaseDatabase.getInstance().getReference().child("Registration").child(code).child(stdID).child("final").setValue(Double.valueOf(etFinal.getText().toString()));
            //calculate GPA and set it in "grade" then set status of course with "0"
        }*/

}
    /*String getFinal(String id){
        return etFinal.getText().toString();
    }
    String getPra(String id){
        return etPractical.getText().toString();
    }*/

