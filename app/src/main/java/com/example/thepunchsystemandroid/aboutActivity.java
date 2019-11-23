package com.example.thepunchsystemandroid;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toolbar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class aboutActivity extends BaseActivity {
    private RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initViews();
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.fuck)//图片
                .setDescription("我一个学后端的，却写安卓鬼知道我经历了什么")//介绍
                .addItem(new Element().setTitle("Version 3.1"))
                .addGroup("为什么会变成这样呢……")
                .addGroup("第一次小组里全是大佬。组里后端大佬又很多。")
                .addGroup("两件快乐事情重合在一起。而这两份快乐，又给我带来更多的快乐。")
                .addGroup("得到的，本该是像摸鱼一般幸福的时间……")
                .addGroup("但是，为什么让我一个半路出家的来学写安卓")
                .create();
        relativeLayout.addView(aboutPage);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:

        }
        return true;
    }
    private void initViews(){
        relativeLayout= (RelativeLayout) findViewById(R.id.relativeLayout);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
