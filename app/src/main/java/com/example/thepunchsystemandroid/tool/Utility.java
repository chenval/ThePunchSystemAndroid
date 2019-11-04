package com.example.thepunchsystemandroid.tool;


import com.example.thepunchsystemandroid.Entity.indexStudents;
import com.example.thepunchsystemandroid.Entity.student;
import com.example.thepunchsystemandroid.Entity.unfinishTime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Utility {
    public static boolean handleMyAndAllResponse(String response){
        try {
            JSONObject jsonObject_time = new JSONObject(response);
            String time = jsonObject_time.getString("unfinishTime");
            unfinishTime unfinishTime1=new unfinishTime();
            unfinishTime1.setUfinishTime(time);
            unfinishTime1.saveOrUpdate();
            JSONObject jsonObject=new JSONObject(response).getJSONObject("student");
            student stu=new student();
            stu.setId(jsonObject.getInt("id"));
            stu.setStudentID(jsonObject.getLong("studentID"));
            stu.setPassword(jsonObject.getString("password"));
            stu.setName(jsonObject.getString("name"));
            stu.setSex(jsonObject.getInt("sex"));
            stu.setGrade(jsonObject.getInt("grade"));
            stu.setPhoto(jsonObject.getString("photo"));
            stu.setAvatar(jsonObject.getString("avatar"));
            stu.setCreateTime(jsonObject.getString("createTime"));
            stu.setIspunch(jsonObject.getBoolean("punch"));
            stu.saveOrUpdate();

            JSONArray jsonArray=new JSONObject(response).getJSONArray("indexStudents");
            List<indexStudents> stu_data_2=new ArrayList<>();
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                indexStudents students=new indexStudents();
                students.setAvatar(jsonObject1.getString("avatar"));
                students.setName(jsonObject1.getString("name"));
                students.setGrade(jsonObject1.getInt("grade"));
                students.setWeekTime(jsonObject1.getDouble("weekTime"));
                students.setWeekLeftTime(jsonObject1.getDouble("weekLeftTime"));
                students.setTodayTime(jsonObject1.getDouble("todayTime"));
                students.setPunch(jsonObject1.getBoolean("punch"));
                stu_data_2.add(students);
                students.save();
            }
            return true;
        }catch (JSONException e){
            e.printStackTrace();
        }
        return false;
    }

}
