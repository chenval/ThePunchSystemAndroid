package com.example.thepunchsystemandroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.thepunchsystemandroid.tool.HttpUtilGet;
import com.example.thepunchsystemandroid.tool.HttpUtilPost;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Response;


public class LoginActivity extends BaseActivity {
    private EditText accountEdit;
    private EditText passwordEdit;
    private Button login;
    private Button registe;

    private ProgressDialog progressDialog;
    private long exitTime = 0;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(LoginActivity.this, "网络连接失败！", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", accountEdit.getText().toString());
                    editor.putString("password", passwordEdit.getText().toString());
                    editor.putBoolean("info", true);
                    editor.apply();
                    break;
                case 4:
                    String sessionid=(String)msg.obj;
                    SharedPreferences share = getSharedPreferences("Session",MODE_PRIVATE);
                    SharedPreferences.Editor edit = share.edit();//编辑文件
                    edit.putString("sessionid",sessionid);
                    edit.apply();
                    break;
                case 5:
                    Toast.makeText(LoginActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
    public void showProgressDialog(Context mContext, String text) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        progressDialog.setMessage(text);	//设置内容
        progressDialog.setCancelable(false);//点击屏幕和按返回键都不能取消加载框
        progressDialog.show();

        //设置超时自动消失
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //取消加载框
                if(dismissProgressDialog()){
    //超时处理
                }
            }
        }, 60000);//超时时间60秒
    }
    public Boolean dismissProgressDialog() {
        if (progressDialog != null){
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                return true;//取消成功
            }
        }
        return false;//已经取消过了，不需要取消
    }




    public void clickHandler_reg(View view) {
        Toast.makeText(LoginActivity.this, "鸽了鸽了，联系你的队长查后台", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_login);
        accountEdit = (EditText) findViewById(R.id.account);
        passwordEdit = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        registe = (Button) findViewById(R.id.signIn);
        SharedPreferences sharedPreference = getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences share = getSharedPreferences("Session",MODE_PRIVATE);
        String session=share.getString("sessionid",null);
        if(session!=null&&sharedPreference.getBoolean("info", false)){
            System.out.println(session+"    232323");
            showProgressDialog(this,"登录中");
            JSONObject object=new JSONObject();
            try{
                object.put("studentID",sharedPreference.getString("username",null));
                 String url=duankou.getDuanKou()+"getStudentAndPunchInfo";
                HttpUtilGet.sendOkHttpRequestGet(url,object.toString(),session,new okhttp3.Callback(){
                    @Override
                    public void onFailure(Call call, IOException e) {
                        dismissProgressDialog();
                        Message ms=new Message();
                        ms.what=5;
                        handler.sendMessage(ms);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        showResponse_session(response.body().string());
                    }
                });
            }catch (Exception E){
                E.printStackTrace();
            }
        }
        registe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "注册需要后台管理员申请过关才能成功", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, registerActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                String address=duankou.getDuanKou()+"login";
                if(account.length()!=0&&password.length()!=0){
                    JSONObject object=new JSONObject();
                    showProgressDialog(LoginActivity.this,"登录中。。。");
                    try {
                        object.put("studentID",account);
                        object.put("password",password);
                        String data=object.toString();
//                    System.out.println(object.toString());
                        HttpUtilPost.sendOkHttpRequest(address, data,null,new okhttp3.Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                System.out.println("!!!!!!!!!!!!!!!!!!!!!");
                                dismissProgressDialog();
                                Message message = new Message();
                                message.what = 2;
                                handler.sendMessage(message);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Headers headers =response.headers();
                                List cookies = headers.values("Set-Cookie");
                                String session = (String)cookies.get(0);
                                String sessionid = session.substring(0,session.indexOf(";"));
                                Message message = new Message();
                                message.what = 4;
                                message.obj=sessionid;
                                handler.sendMessage(message);
                                showResponse(response.body().string());

                            }
                        });
                    }catch (JSONException E){
                        E.printStackTrace();
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "账号密码不能为空", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    System.out.println(status);
                    dismissProgressDialog();
                    if (status.equals("success")) {

                        message.what = 3;
                        Toast.makeText(LoginActivity.this, "登陆成功！！！", Toast.LENGTH_SHORT).show();
                        handler.sendMessage(message);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    if (status.equals("fail")) {
                        Toast.makeText(LoginActivity.this, "帐号或者密码错误", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void showResponse_session(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println(response+"自动登录");
                Message message = new Message();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        dismissProgressDialog();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    if (status.equals("fail")) {
                       String address = duankou.getDuanKou()+"login";
                        JSONObject object=new JSONObject();
                        try {
                            SharedPreferences sharedPreference = getSharedPreferences("login", Context.MODE_PRIVATE);
                            SharedPreferences share = getSharedPreferences("Session",MODE_PRIVATE);
                            object.put("studentID", sharedPreference.getString("username",""));
                            object.put("password", sharedPreference.getString("password",""));
                            String data = object.toString();
                            HttpUtilPost.sendOkHttpRequest(address, data,null,new okhttp3.Callback(){
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    dismissProgressDialog();
                                    Message ms=new Message();
                                    ms.what=5;
                                    handler.sendMessage(ms);

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    Headers headers =response.headers();
                                    List cookies = headers.values("Set-Cookie");
                                    String session = (String)cookies.get(0);
                                    String sessionid = session.substring(0,session.indexOf(";"));
                                    Message message = new Message();
                                    message.what = 4;
                                    message.obj=sessionid;
                                    handler.sendMessage(message);
                                    dismissProgressDialog();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }catch (JSONException E){
                            E.printStackTrace();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }




}