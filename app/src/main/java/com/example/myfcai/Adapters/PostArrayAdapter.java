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

public class PostArrayAdapter extends ArrayAdapter {
    public PostArrayAdapter(Context context, ArrayList<Post> posts) {
        super(context, 0, posts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.post, parent, false);
        }
        final Post posts = (Post) getItem(position);

        ImageView ivUserImg=convertView.findViewById(R.id.iv_user_img_post);
        TextView name = convertView.findViewById(R.id.tv_writer_name_post);
        TextView course = convertView.findViewById(R.id.tv_course_post);
        TextView time =convertView.findViewById(R.id.tv_time_post);
        TextView post = convertView.findViewById(R.id.tv_post);
        TextView comments=convertView.findViewById(R.id.tv_comment_post);
        Glide.with(getContext()).load(posts.getUserImg()).into(ivUserImg);
        name.setText(posts.getWriter());
        course.setText(". "+posts.getCourse());
        time.setText(posts.getTime());
        post.setText(posts.getPost());
        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CommentActivity.class);
                intent.putExtra("post id",posts.getPostID());
                getContext().startActivity(intent);
            }
        });


        return convertView;
    }
}
