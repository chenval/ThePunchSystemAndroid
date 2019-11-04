package com.example.thepunchsystemandroid.Entity;

import org.litepal.crud.LitePalSupport;

public class indexStudents  extends LitePalSupport implements Comparable{
    private String name;
    private String avatar;
    private int grade;
    private double weekTime;
    private double todayTime;
    private double weekLeftTime;
    private Boolean punch;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public double getWeekTime() {
        return weekTime;
    }

    public void setWeekTime(double weekTime) {
        this.weekTime = weekTime;
    }

    public double getTodayTime() {
        return todayTime;
    }

    public void setTodayTime(double todayTime) {
        this.todayTime = todayTime;
    }

    public double getWeekLeftTime() {
        return weekLeftTime;
    }

    public void setWeekLeftTime(double weekLeftTime) {
        this.weekLeftTime = weekLeftTime;
    }

    public Boolean getPunch() {
        return punch;
    }

    public void setPunch(Boolean punch) {
        this.punch = punch;
    }

    @Override
    public int compareTo(Object o) {
        return (int)(((indexStudents) o).weekTime-this.weekTime);
    }

    @Override
    public String toString() {
        return "indexStudents{" +
                "name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", grade=" + grade +
                ", weekTime=" + weekTime +
                ", todayTime=" + todayTime +
                ", weekLeftTime=" + weekLeftTime +
                ", punch=" + punch +
                '}';
    }
}
