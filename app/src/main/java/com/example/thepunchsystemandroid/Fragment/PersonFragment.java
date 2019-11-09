package com.example.thepunchsystemandroid.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.thepunchsystemandroid.Entity.indexStudents;
import com.example.thepunchsystemandroid.Entity.student;
import com.example.thepunchsystemandroid.Entity.unfinishTime;
import com.example.thepunchsystemandroid.R;
import com.example.thepunchsystemandroid.duankou;
import com.example.thepunchsystemandroid.tool.FastBlurUtil;
import com.example.thepunchsystemandroid.tool.HttpUtilPost;
import com.example.thepunchsystemandroid.tool.HttpUtilGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import okhttp3.Call;
import okhttp3.Response;

public class PersonFragment extends DialogFragment {
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(getActivity(), "与服务器连接失败！", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    b.setPunch(true);
                    a.setIspunch(true);
                    a.updateAll("name=?",a.getName());
                    b.updateAll("name = ?",b.getName());
                    time.setUfinishTime((String)msg.obj);
                    time.saveOrUpdate();
                    break;
                case 3:
                    b.setPunch(false);
                    a.setIspunch(false);
                    a.updateAll("name=?",a.getName());
                    b.updateAll("name = ?",b.getName());
                    break;
                case 4:
                    indexStudents c=(indexStudents)msg.obj;
                    c.updateAll("name = ?",c.getName());
                    break;
            }
        }
    };
    private ImageView imageView_back;
    private ImageView avatva;
    private TextView textView_name;
    private TextView textView_Id;
    private ImageView user_lin;
    private student a;
    private TextView TodatTime;
    private TextView punch;
    private Button start;
    private Button stop;
    private indexStudents b = new indexStudents();
    private TextView StartTime;
    private TextView weekTime;
    private TextView weekLeftTime;
    private unfinishTime time;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        time=LitePal.findFirst(unfinishTime.class);
        a = LitePal.findFirst(student.class);
        String name = a.getName();
        b = LitePal.where("name = ?", name).findFirst(indexStudents.class);
        View personLayout = inflater.inflate(R.layout.fragment_person, container, false);
        imageView_back = personLayout.findViewById(R.id.h_back);
        avatva = personLayout.findViewById(R.id.h_head);
        textView_name = personLayout.findViewById(R.id.user_name);
        textView_Id = personLayout.findViewById(R.id.user_Id);
        user_lin = personLayout.findViewById(R.id.user_line);
        TodatTime = personLayout.findViewById(R.id.Today_time);
        start=personLayout.findViewById(R.id.start);
        stop=personLayout.findViewById(R.id.end);
        StartTime=personLayout.findViewById(R.id.startTime);
        punch = personLayout.findViewById(R.id.punch);
        weekLeftTime=personLayout.findViewById(R.id.weekLeftTime);
        weekTime=personLayout.findViewById(R.id.weektime);
        weekTime.setText(b.getWeekTime()+"h");
        weekLeftTime.setText(b.getWeekLeftTime()+"h");
        TodatTime.setText(b.getTodayTime()+"h");
        if(a.getIspunch()){
            punch.setText("打卡中");
            punch.setTextColor(getResources().getColor(R.color.colorRed));
            StartTime.setText(time.getUfinishTime());
        }else {
            punch.setText("未打卡");
            punch.setTextColor(getResources().getColor(R.color.colorGreen));
            StartTime.setText("还没有开始打卡！");
        }
        textView_name.setText(a.getName());
        textView_Id.setText(Long.toString(a.getStudentID()));
        return personLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("测试开始按钮");
                String url = duankou.getDuanKou()+"startPunch";
                SharedPreferences card = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
                System.out.println(card.getAll().toString());
                JSONObject object = new JSONObject();
                try {
                    object.put("studentID", card.getString("username", null));
                    SharedPreferences session = getActivity().getSharedPreferences("Session", Context.MODE_PRIVATE);
                    String cookie = session.getString("sessionid", "");
                    System.out.println(url+"   "+object.toString()+"     "+cookie);
                    HttpUtilPost.sendOkHttpRequest(url, object.toString(), cookie, new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "与服务器连接失败！", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                           final String responseText = response.body().string();
                            System.out.println(responseText);
                            try {
                                URL urlTime = new URL("http://www.baidu.com");
                                URLConnection uc = urlTime.openConnection();//生成连接对象
                                uc.connect(); //发出连接
                                long ld = uc.getDate(); //取得网站日期时间
                                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTimeInMillis(ld);
                                final String format = formatter.format(calendar.getTime());
                                JSONObject jsonObject = new JSONObject(responseText);
                                String status = jsonObject.getString("status");
                                if (status.equals("success")) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getActivity(), "打卡开始！", Toast.LENGTH_SHORT).show();
                                            punch.setText("打卡中");
                                            punch.setTextColor(getResources().getColor(R.color.colorRed));
                                            StartTime.setText(format);
                                            Message ms = new Message();
                                            ms.what = 2;
                                            ms.obj=format;
                                            handler.sendMessage(ms);
                                        }
                                    });
                                }
                                if (status.equals("fail")) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                JSONObject jsonObject1 = new JSONObject(responseText);
                                                final  String msg;
                                                if(jsonObject1.getString("msg").isEmpty()){
                                                    msg=null;
                                                }else{
                                                    msg = jsonObject1.getString("msg");
                                                }
                                                if(msg.equals("已经在打卡或者没有登录。")) {
                                                    Toast.makeText(getActivity(), "已经在打卡或者没有登录！", Toast.LENGTH_SHORT).show();
                                                }else if(msg.equals("请尝试连接LC2，并且拔出网线重试打卡")){
                                                    Toast.makeText(getActivity(), "未在LC2打卡", Toast.LENGTH_SHORT).show();
                                                }else {
                                                    Toast.makeText(getActivity(), "登陆过期！", Toast.LENGTH_SHORT).show();
                                                }
                                            }catch (JSONException E){
                                                E.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException E) {
                    E.printStackTrace();
                }
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   String url = "http://47.102.114.0:8083/endPunch";
                 String url = duankou.getDuanKou()+"endPunch";
                SharedPreferences card = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
                JSONObject object = new JSONObject();
                try {
                    object.put("studentID", card.getString("username", null));
                    SharedPreferences session = getActivity().getSharedPreferences("Session", Context.MODE_PRIVATE);
                    String cookie = session.getString("sessionid", "");
                    HttpUtilPost.sendOkHttpRequest(url, object.toString(), cookie, new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "与服务器连接失败！", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                           final String responseText = response.body().string();
                            System.out.println("结束打卡"+responseText);
                            System.out.println(responseText);
                            try {
                                JSONObject jsonObject = new JSONObject(responseText);
                                String status = jsonObject.getString("status");
                                final String ms=jsonObject.getString("msg");
                                if (status.equals("success")) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getActivity(), "停止打卡！", Toast.LENGTH_SHORT).show();
                                            punch.setText("未打卡");
                                            punch.setTextColor(getResources().getColor(R.color.colorGreen));
                                            StartTime.setText("还没有开始打卡!");
                                            Message ms = new Message();
                                            ms.what = 3;
                                            handler.sendMessage(ms);
                                        }
                                    });
                                }
                                if (status.equals("fail")) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            if ( ms.equals("请尝试连接LC2，并且拔出网线重试打卡")) {
                                                Toast.makeText(getActivity(), "请尝试连接LC2，并且拔出网线重试打卡", Toast.LENGTH_SHORT).show();
                                            }else if( ms.equals("当前打卡时间过短小于半小时或者识别为隔天打卡记录，请稍后重新打卡。")){
                                                Toast.makeText(getActivity(), "前打卡时间过短小于半小时或者识别为隔天打卡记录，请稍后重新打卡！", Toast.LENGTH_SHORT).show();
                                                punch.setText("未打卡");
                                                punch.setTextColor(getResources().getColor(R.color.colorGreen));
                                                StartTime.setText("还没有开始打卡!");
                                                Message ms = new Message();
                                                ms.what = 3;
                                                handler.sendMessage(ms);
                                            }else if(ms.equals("没有登录或者没有开始打卡。")){
                                                Toast.makeText(getActivity(), "未打卡！", Toast.LENGTH_SHORT).show();
                                            }else {
                                                Toast.makeText(getActivity(), "登陆过期！", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    String X=duankou.getDuanKou()+"getStudentAndPunchInfo";
                    HttpUtilGet.sendOkHttpRequestGet(X,object.toString(),cookie,new okhttp3.Callback(){
                        @Override
                        public void onFailure(Call call, IOException e) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(),"更新今日时间出现异常",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseText=response.body().string();
                            System.out.println(responseText);
                            try {
                                JSONObject jsonObject=new JSONObject(responseText).getJSONObject("student");
                                String name=jsonObject.getString("name");
                                JSONArray jsonArray=new JSONObject(responseText).getJSONArray("indexStudents");
                                final indexStudents stua=new indexStudents();
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    if(jsonObject1.getString("name").equals(name)){
                                        stua.setAvatar(jsonObject1.getString("avatar"));
                                        stua.setName(jsonObject1.getString("name"));
                                        stua.setGrade(jsonObject1.getInt("grade"));
                                        stua.setWeekTime(jsonObject1.getDouble("weekTime"));
                                        stua.setWeekLeftTime(jsonObject1.getDouble("weekLeftTime"));
                                        stua.setTodayTime(jsonObject1.getDouble("todayTime"));
                                        stua.setPunch(jsonObject1.getBoolean("punch"));
                                    }
                                }
                                Message message=new Message();
                                message.obj=stua;
                                message.what=4;
                                handler.sendMessage(message);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        TodatTime.setText(stua.getTodayTime()+"h");
                                        weekLeftTime.setText(stua.getWeekLeftTime()+"h");
                                        weekTime.setText(stua.getWeekTime()+"h");
                                    }
                                });

                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException E) {
                    E.printStackTrace();
                }
            }
        });
        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        Glide.with(getContext()).load(sharedPreferences.getString("avatar", "")).apply(RequestOptions.circleCropTransform()).into(avatva);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //                        下面的这个方法必须在子线程中执行
                final Bitmap blurBitmap2 = FastBlurUtil.GetUrlBitmap(sharedPreferences.getString("avatar", ""), 5);

                //                        刷新ui必须在主线程中执行
                getActivity().runOnUiThread(new Runnable() {//这个是我自己封装的在主线程中刷新ui的方法。
                    @Override
                    public void run() {
                        imageView_back.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imageView_back.setImageBitmap(blurBitmap2);
                    }
                });
            }
        }).start();
    }
}

