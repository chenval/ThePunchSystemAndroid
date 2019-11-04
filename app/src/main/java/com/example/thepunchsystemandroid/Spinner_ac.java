package com.example.thepunchsystemandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class Spinner_ac extends Activity {
    private TextView result = null;
    private Spinner spinner = null;
    private ArrayAdapter<String> adapter = null;
    private static final String [] langurage ={"2018级","2019级","2020级"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_rank);
        spinner = (Spinner)findViewById(R.id.spinner);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,langurage);
        //设置下拉列表风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将适配器添加到spinner中去
        spinner.setAdapter(adapter);
        spinner.setVisibility(View.VISIBLE);//设置默认显示
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                result.setText("你的选择是："+((TextView)arg1).getText());


            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
