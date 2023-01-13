package com.example.myfcai.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myfcai.Activities.CommentActivity;
import com.example.myfcai.Class.Post;
import com.example.myfcai.R;

import java.util.ArrayList;

public class TaskArrayAdapter extends ArrayAdapter {
    String type;
    public TaskArrayAdapter(Context context, ArrayList<Post> posts,String type) {
        super(context, 0, posts);
        this.type=type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task, parent, false);
        }
        final Post posts = (Post) getItem(position);
        View view=convertView.findViewById(R.id.v_task);
        ImageView ivUserImg=convertView.findViewById(R.id.iv_user_img_task);
        TextView name = convertView.findViewById(R.id.tv_writer_name_task);
        TextView course = convertView.findViewById(R.id.tv_course_task);
        TextView time =convertView.findViewById(R.id.tv_time_task);
        TextView post = convertView.findViewById(R.id.tv_task);
        TextView comments=convertView.findViewById(R.id.tv_comment_task);
        Glide.with(getContext()).load(posts.getUserImg()).into(ivUserImg);
        name.setText(posts.getWriter());
        course.setText(". "+posts.getCourse());
        time.setText(posts.getTime());
        post.setText(posts.getPost());
        if (type.equals("t")){
            comments.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        }else {
            comments.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);

        }

        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //upload task
                Toast.makeText(getContext(), "uploaded", Toast.LENGTH_SHORT).show();
            }
        });


        return convertView;
    }
}
