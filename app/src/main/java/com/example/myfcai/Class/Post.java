package com.example.myfcai.Class;

public class Post {
    String postID,userImg,writer,course,time,post;



    public Post(String postID, String userImg, String writer, String course, String time, String post){
        this.postID = postID;
        this.userImg=userImg;
        this.writer=writer;
        this.course=course;
        this.time=time;
        this.post=post;
    }

    public String getPostID() {
        return postID;
    }

    public String getTime() {
        return time;
    }

    public String getWriter() {
        return writer;
    }

    public String getCourse() {
        return course;
    }

    public String getPost() {
        return post;
    }

    public String getUserImg() {
        return userImg;
    }
}
