package com.example.myfcai.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myfcai.Class.Course;
import com.example.myfcai.R;

import java.util.ArrayList;

public class StudentCoursesArrayAdapter extends ArrayAdapter<Course> {
    TextView tvName, tvMid, tvPractical, tvFinal, tvGrade;
    LinearLayout linearLayout;
    String status;

    public StudentCoursesArrayAdapter(@NonNull Context context, ArrayList<Course> courses) {
        super(context, 0, courses);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.student_course, parent, false);
        }

        Course course = (Course) getItem(position);

        linearLayout = convertView.findViewById(R.id.ll_back_student_course);
        tvName = convertView.findViewById(R.id.tv_name_student_course);
        tvMid = convertView.findViewById(R.id.tv_mid_student_course);
        tvPractical = convertView.findViewById(R.id.tv_practical_student_course);
        tvFinal = convertView.findViewById(R.id.tv_final_student_course);
        tvGrade = convertView.findViewById(R.id.tv_grade_student_course);

        tvName.setText(course.getName());
        if (course.getMid() != 0) {
            tvMid.setText(String.valueOf(course.getMid()));
        }
        if (course.getPractical() != 0) {
            tvPractical.setText(String.valueOf(course.getPractical()));
        }
        if (course.getFinalG() != 0) {
            tvFinal.setText(String.valueOf(course.getFinalG()));
        }
        if (!course.getGrade().equals("")) {
            tvGrade.setText(String.valueOf(course.getGrade()));
        }

        status = course.getStatus();
        if (status.equals("0")) {
            linearLayout.setBackgroundResource(R.drawable.button_background_gray);
        } else {

            linearLayout.setBackgroundResource(R.drawable.button_background_white_post);

        }


        return convertView;
    }
}
