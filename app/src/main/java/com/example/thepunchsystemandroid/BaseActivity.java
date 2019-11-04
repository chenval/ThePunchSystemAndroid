package com.example.thepunchsystemandroid;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ActivityList.Add(this);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        ActivityList.Remove(this);
    }
}
