package com.example.myfcai.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myfcai.Activities.CommentActivity;
import com.example.myfcai.Class.Post;
import com.example.myfcai.R;

import java.util.ArrayList;

public class StdAttArrayAdapter extends ArrayAdapter {
    public StdAttArrayAdapter(Context context, ArrayList<String> stdNames) {
        super(context, 0, stdNames);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.course, parent, false);
        }
        final String stdName = (String) getItem(position);
        TextView name = convertView.findViewById(R.id.tv_name_course);
        TextView code = convertView.findViewById(R.id.tv_code_course);
        TextView credit =convertView.findViewById(R.id.tv_credit_course);
        name.setText(stdName);
        code.setText("");
        credit.setText("");


        return convertView;
    }
}
