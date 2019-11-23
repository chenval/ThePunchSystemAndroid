package com.example.thepunchsystemandroid;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutAppActivity extends BaseActivity {
    private RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboout_app);
        initViews();
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.mipmap.icon)//图片
                .setDescription("本APP主要起到督促打卡的作用")//介绍
                .addGroup("主要起到一个打卡计时的作用，如果全放在放作弊上未免本末倒置")
                .addGroup("作弊方法是层出不穷的，就好比乐健APP跑步打卡")
                .addGroup("不是不能增加手机序列号识别，限制登录个数，联网识别定位等")
                .addGroup("可以但没必要，堵不如疏，愿意学的自然学，不愿意的强迫他坐在那里都不会学")
                .addGroup("这才是我的初心，打卡APP")
                .addGroup("该版本是测试版，可以不用连接指定wifi打卡")
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
