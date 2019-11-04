package com.example.thepunchsystemandroid;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityList {
    private static List<Activity> activities = new ArrayList<>();

    public static void Add(Activity activity){
        activities.add(activity);
    }

    public static void Remove(Activity activity){
        activities.remove(activity);
    }

    public static void finishAll(){
        for (Activity activity : activities){
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
        activities.clear();
    }
}
