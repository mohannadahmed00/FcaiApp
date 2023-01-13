package com.example.myfcai.Class;

public class User {
    String imgUri,name,age,email,phone,type,id,courseCode;
    double mid,  practical,  finalG,grade;
    User(String imgUri, String name, String age, String email, String phone, String type){
        this.imgUri=imgUri;
        this.name=name;
        this.age=age;
        this.email=email;
        this.phone=phone;
        this.type=type;
    }
    public User(String id, String name,String courseCode, double mid, double practical, double finalG){
        this.id=id;
        this.name=name;
        this.courseCode=courseCode;
        this.mid=mid;
        this.practical=practical;
        this.finalG=finalG;
    }
    public User(String id,double grade){
        this.id=id;
        this.grade=grade;
    }

    public double getGrade() {
        return grade;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public double getMid() {
        return mid;
    }

    public double getPractical() {
        return practical;
    }

    public double getFinalG() {
        return finalG;
    }

    public String getImgUri() {
        return imgUri;
    }

    public String getUserName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getType() {
        return type;
    }

    public void setMid(double mid) {
        this.mid = mid;
    }

    public void setPractical(double practical) {
        this.practical = practical;
    }

    public void setFinalG(double finalG) {
        this.finalG = finalG;
    }
}
