package com.example.thepunchsystemandroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.example.thepunchsystemandroid.tool.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class updateActivity extends BaseActivity {

    private EditText oldPassword;
    private EditText password;
    private EditText passwordCheck;
    private RadioGroup radgroup;
    private Button button;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityList.Add(this);
        setContentView(R.layout.activity_update);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        oldPassword=findViewById(R.id.oldPassword);
        password=findViewById(R.id.password);
        radgroup=findViewById(R.id.radioGroup);
        button=findViewById(R.id.updateOk);
        passwordCheck=findViewById(R.id.passwordCheck);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldp=oldPassword.getText().toString();
                String pass=password.getText().toString();
                String passc=passwordCheck.getText().toString();
                RadioButton Sex=findViewById(radgroup.getCheckedRadioButtonId());
                String sex=Sex.getText().toString();
                int sex1;
                if(sex.equals("男")){
                    sex1=1;
                }else {
                    sex1=0;
                }
                SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                System.out.println(sharedPreferences.getString("password",null)+"22222222222");
                System.out.println(oldp+"55555555");
                System.out.println(sex+"55555222222222222555");
                if (oldp.equals(sharedPreferences.getString("password",null))){
                    if(pass.length()>=6){
                        if(pass.equals(passc)){
                            SharedPreferences share = getSharedPreferences("Session",MODE_PRIVATE);
                            String session=share.getString("sessionid",null);
                            JSONObject object=new JSONObject();
                            try {
                                object.put("studentID",sharedPreferences.getString("username",null));
                                object.put("sex",sex1);
                                object.put("password",oldp);
                                System.out.println(object.toString());
                                String url="http://47.102.114.0:8080/updateStudentInfo";
                              //  String url="http://47.102.114.0:8083/updateStudentInfo";
                                HttpUtil.sendOkHttpRequest(url,object.toString(),session,new okhttp3.Callback(){
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(updateActivity.this);
                                                builder.setTitle("网络异常，修改失败");
                                                builder.setMessage("修改失败请稍后重试");
                                                builder.setNegativeButton("再试试", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                });
                                                builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        finish();
                                                    }
                                                });
                                                builder.show();
                                            }
                                        });

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        final String responseText=response.body().string();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(responseText);
                                                    String status = jsonObject.getString("status");
                                                    if (status.equals("success")) {
                                                        Toast.makeText(updateActivity.this,"修改成功，重新登录",Toast.LENGTH_LONG).show();
                                                        SharedPreferences sharedPreferences=getSharedPreferences("login", Context.MODE_PRIVATE);
                                                        sharedPreferences.edit().putBoolean("info",false).apply();
                                                        SharedPreferences a=getSharedPreferences("Session", Context.MODE_PRIVATE);
                                                        sharedPreferences.edit().
                                                                clear().apply();
                                                        ActivityList.finishAll();
                                                        goToActivity(LoginActivity.class,null);

                                                        Intent intent = new Intent(updateActivity.this, LoginActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                    if (status.equals("fail")) {
                                                        Toast.makeText(updateActivity.this,"遇到未知因素，修改失败了555",Toast.LENGTH_LONG).show();
                                                    }

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                });
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            oldPassword.setText("");
                            passwordCheck.setText("");
                            Toast.makeText(updateActivity.this,"两次密码输入不一致",Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(updateActivity.this,"密码过短！",Toast.LENGTH_LONG).show();
                    }

                }else {
                    oldPassword.setText("");
                    Toast.makeText(updateActivity.this,"密码验证错误！请重新输入旧密码",Toast.LENGTH_LONG).show();
                }

            }
        });


    }
    public void goToActivity(Class Activity,Bundle bundle){
        Intent intent = new Intent(this,Activity);
        if (bundle!=null&&bundle.size()!=0){
            intent.putExtra("data",bundle);
        }
        startActivity(intent);
    }


}
