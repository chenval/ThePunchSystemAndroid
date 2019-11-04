package com.example.thepunchsystemandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thepunchsystemandroid.tool.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class registerActivity extends AppCompatActivity {
    private EditText name;
    private EditText ID;
    private EditText password;
    private EditText passwordCheck;
    private Button cancel;
    private Button register;
    private RadioGroup radgroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_register);

        name = findViewById(R.id.name);
        ID = findViewById(R.id.ID);
        password = findViewById(R.id.password);
        passwordCheck = findViewById(R.id.passwordCheck);
        radgroup = findViewById(R.id.radioGroup);

        cancel = findViewById(R.id.cancel);
        register = findViewById(R.id.register);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setText("");
                ID.setText("");
                passwordCheck.setText("");
                password.setText("");
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String studentName = name.getText().toString();
                String studentId = ID.getText().toString();
                if(studentName!=null&&studentId!=null){
                    if (password.getText().toString().equals(passwordCheck.getText().toString())&&password.getText().toString().length()>=6) {
                        RadioButton Sex = findViewById(radgroup.getCheckedRadioButtonId());
                        String sex = Sex.getText().toString();
                        int sex1;
                        if (sex.equals("男")) {
                            sex1 = 1;
                        } else {
                            sex1 = 0;
                        }
                        System.out.println(sex1+"aaaaaaaaaaaaaa");
                        String pw = password.getText().toString();
                        System.out.println(pw+"aaaaaaaaaaaaaa");
                        String pwC = passwordCheck.getText().toString();
                        System.out.println(pwC+"aaaaaaaaaaaaaa");
                        //  String url = "http://47.102.114.0:8080/register";
                       String url = "http://47.102.114.0:8083/register";
                        JSONObject object = new JSONObject();
                        try {
                            object.put("studentId", studentId);
                            object.put("name", studentName);
                            object.put("password", pw);
                            object.put("sex", sex1);
                            object.put("ispunch",0);
                            object.put("photo","http://118.24.95.11:7000/pig.jpg");
                            System.out.println(object.toString()+"aaaaaaaaaaaaaa");
                            HttpUtil.sendOkHttpRequest(url, object.toString(), null, new okhttp3.Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(registerActivity.this);
                                            builder.setTitle("网络异常，注册失败");
                                            builder.setMessage("稍后重试");
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
                                    final String responseText = response.body().string();
                                    System.out.println(responseText+"asdasdasdasdasdac efc");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                JSONObject jsonObject = new JSONObject(responseText);
                                                String status = jsonObject.getString("status");
                                                if (status.equals("success")) {
                                                    Toast.makeText(registerActivity.this, "注册成功。", Toast.LENGTH_LONG).show();
                                                    ActivityList.finishAll();
                                                    goToActivity(LoginActivity.class, null);

                                                    Intent intent = new Intent(registerActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                if (status.equals("fail")) {
                                                    Toast.makeText(registerActivity.this, "遇到未知因素，注册失败了555", Toast.LENGTH_LONG).show();
                                                }

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                }
                            });
                        } catch (JSONException E) {
                            E.printStackTrace();
                        }
                    } else {
                        Toast.makeText(registerActivity.this, "两次输入密码不一致或者密码过短", Toast.LENGTH_SHORT).show();
                        password.setText("");
                        passwordCheck.setText("");
                    }
                }else {
                    Toast.makeText(registerActivity.this, "帐号和用户名不能为空", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void goToActivity(Class Activity, Bundle bundle) {
        Intent intent = new Intent(this, Activity);
        if (bundle != null && bundle.size() != 0) {
            intent.putExtra("data", bundle);
        }
        startActivity(intent);
    }
}
