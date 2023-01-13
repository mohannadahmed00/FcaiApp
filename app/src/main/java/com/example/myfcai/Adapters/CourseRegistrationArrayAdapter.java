package com.example.myfcai.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.myfcai.Class.Course;
import com.example.myfcai.R;

import java.util.ArrayList;

public class CourseRegistrationArrayAdapter extends ArrayAdapter<Course> {

    public CourseRegistrationArrayAdapter(Context context, ArrayList<Course> courses) {
        super(context, 0, courses);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.course, parent, false);
        }
        final Course course = (Course) getItem(position);

        final TextView tvCode=convertView.findViewById(R.id.tv_code_course);
        final TextView tvName=convertView.findViewById(R.id.tv_name_course);
        final TextView tvCredit=convertView.findViewById(R.id.tv_credit_course);




        tvCode.setText(course.getCode());
        tvName.setText(course.getName());
        tvCredit.setText(Integer.toString(course.getCredit()));

        if (course.isSelect()){
            tvCode.setBackgroundResource(R.color.green);
            tvName.setBackgroundResource(R.color.green);
            tvCredit.setBackgroundResource(R.color.green);
        }else {
            tvCode.setBackgroundResource(R.color.white);
            tvName.setBackgroundResource(R.color.white);
            tvCredit.setBackgroundResource(R.color.white);
        }
        return convertView;
    }

}
