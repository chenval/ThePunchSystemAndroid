package com.example.thepunchsystemandroid.Entity;

import org.litepal.crud.LitePalSupport;

public class student extends LitePalSupport {
    private int Id;
    private long studentID;
    private String name;
    private Integer sex;
    private String password;
    private int grade;
    private String photo;
    private String avatar;
    private String createTime;
    private Boolean  ispunch;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public long getStudentID() {
        return studentID;
    }

    public void setStudentID(long studentID) {
        this.studentID = studentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Boolean getIspunch() {
        return ispunch;
    }

    public void setIspunch(Boolean ispunch) {
        this.ispunch = ispunch;
    }

    @Override
    public String toString() {
        return "student{" +
                "Id=" + Id +
                ", studentID=" + studentID +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", password='" + password + '\'' +
                ", grade=" + grade +
                ", photo='" + photo + '\'' +
                ", avatar='" + avatar + '\'' +
                ", createTime=" + createTime +
                ", ispunch=" + ispunch +
                '}';
    }
}
