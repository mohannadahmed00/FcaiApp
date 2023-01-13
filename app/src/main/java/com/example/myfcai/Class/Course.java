package com.example.myfcai.Class;

public class Course {
    String code, name,pre,status,grade,type;
    int credit;
    boolean select;
    double mid,practical,finalG;


    public double getMid() {
        return mid;
    }

    public double getPractical() {
        return practical;
    }

    public double getFinalG() {
        return finalG;
    }

    public Course(String code, String grade){
        this.code=code;
        this.grade=grade;
    }

    public Course(String code, String name,int x){
        this.code=code;
        this.name=name;
    }

    public Course(String name, double mid, double practical, double finalG, String grade,String status){
        this.name=name;
        this.mid=mid;
        this.practical=practical;
        this.finalG=finalG;
        this.grade=grade;
        this.status=status;
    }
    public Course(String code, String name, int credit, String pre, String type,String status,boolean select){
        this.code=code;
        this.name =name;
        this.credit=credit;
        this.pre=pre;
        this.status=status;
        this.type=type;
        this.select=select;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getPre() {
        return pre;
    }

    public String getStatus() {
        return status;
    }

    public int getCredit() {
        return credit;
    }

    public String getGrade() {
        return grade;
    }
}
