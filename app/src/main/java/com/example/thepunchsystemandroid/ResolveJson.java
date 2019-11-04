package com.example.thepunchsystemandroid;

import org.json.JSONArray;
import org.json.JSONObject;

public class ResolveJson {
    public static void Resolve(String jsonRequest){
        try {
            JSONObject jsonObject=new JSONObject(jsonRequest).getJSONObject("student");
            JSONArray jsonArray=new JSONObject(jsonRequest).getJSONArray("indexStudents");
        } catch (Exception e){
            e.printStackTrace();
        }


    }
}
