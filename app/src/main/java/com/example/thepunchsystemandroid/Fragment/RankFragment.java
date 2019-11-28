package com.example.thepunchsystemandroid.Fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.thepunchsystemandroid.Adapter.studentAdapter;
import com.example.thepunchsystemandroid.Entity.indexStudents;
import com.example.thepunchsystemandroid.Entity.student;
import com.example.thepunchsystemandroid.R;
import com.example.thepunchsystemandroid.duankou;
import com.example.thepunchsystemandroid.tool.HttpUtilPost;
import com.example.thepunchsystemandroid.tool.HttpUtilGet;
import com.example.thepunchsystemandroid.tool.Utility;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;


public class RankFragment extends Fragment  implements SwipeRefreshLayout.OnRefreshListener{
    private List<indexStudents> studentsList=new ArrayList<>();
    private RecyclerView recyclerView;
    private studentAdapter adapter;
    private List<indexStudents> list;
    private SwipeRefreshLayout refreshLayout;
    private Button grade;
    private boolean g=true;

    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    break;
                case 2:
                    Toast.makeText(getActivity(), "获取信息失败", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(getActivity(), "储存失败", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Map a=(Map)msg.obj;
                    student ac=(student)a.get(1);
                    List<indexStudents> b=(List<indexStudents>)a.get(2);
                    ac.save();
                    break;
                case 5:
                    String sessionid=(String)msg.obj;
                    SharedPreferences share = getActivity().getSharedPreferences("Session",MODE_PRIVATE);
                    SharedPreferences.Editor edit = share.edit();//编辑文件
                    edit.putString("sessionid",sessionid);
                    edit.apply();
                    break;
                case 6:
                    queryStudents_Re();
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View contactsLayout = inflater.inflate(R.layout.fragment_rank, container, false);
        grade=contactsLayout.findViewById(R.id.grade);
        grade.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) contactsLayout.findViewById(R.id.recycler_view);
        adapter = new studentAdapter(getContext(), studentsList);
        refreshLayout = (SwipeRefreshLayout)contactsLayout.findViewById(R.id.eRefresh_view);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark),
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorAccent));

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        return contactsLayout;
    }

    @Override
    public void onRefresh() {
        queryStudents_Re();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (g){
                    student a=LitePal.findFirst(student.class);
                    list=LitePal.where("grade=?",a.getGrade()+"").find(indexStudents.class);
                    studentsList.clear();
                    studentsList.addAll(list);
                    Collections.sort(studentsList);
                    adapter.notifyDataSetChanged();
                    grade.setText("全部");
                    g=false;
                }else {
                    list=LitePal.findAll(indexStudents.class);
                    studentsList.clear();
                    studentsList.addAll(list);
                    Collections.sort(studentsList);
                    adapter.notifyDataSetChanged();
                    grade.setText("同级");
                    g=true;
                }

            }
        });
        queryStudents();
    }
    private void queryStudents_Re(){

        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("login", MODE_PRIVATE);
        String url= duankou.getDuanKou()+"getStudentAndPunchInfo";
        JSONObject object=new JSONObject();
        list=LitePal.findAll(indexStudents.class);
        studentsList.clear();
        studentsList.addAll(list);
        Collections.sort(studentsList);
        adapter.notifyDataSetChanged();
        grade.setText("同级");
        g=true;
        try {
            object.put("studentID",sharedPreferences.getString("username",null));
            //  System.out.println(object.toString());
            SharedPreferences share = getActivity().getSharedPreferences("Session",MODE_PRIVATE);
            String cookie=share.getString("sessionid","");
            //  System.out.println(cookie);
            //  System.out.println(share.getString("sessionid",null));
            queryFromServer(url,object.toString(),cookie);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    private void queryStudents(){
        list=LitePal.findAll(indexStudents.class);
        if(list.size()>0){
            student s=LitePal.findFirst(student.class);
            SharedPreferences sharedPreferences=getActivity().getSharedPreferences("login", MODE_PRIVATE);
            sharedPreferences.edit().putLong("userId",s.getStudentID()).putString("avatar",s.getAvatar()).apply();
            studentsList.clear();
            System.out.println("这是在查找后清空的长度"+studentsList.size());
            for(indexStudents a:list){
                studentsList.add(a);
                System.out.println(a.toString());
            }
            System.out.println("这是在查找后清空的长度后加入数据的长度"+studentsList.size());
            Collections.sort(studentsList);
            adapter.notifyDataSetChanged();
        }else {
            SharedPreferences sharedPreferences=getActivity().getSharedPreferences("login", MODE_PRIVATE);
            String url=duankou.getDuanKou()+"getStudentAndPunchInfo";
            JSONObject object=new JSONObject();
            try {
                object.put("studentID",sharedPreferences.getString("username",null));
                //  System.out.println(object.toString());
                SharedPreferences share = getActivity().getSharedPreferences("Session",MODE_PRIVATE);
                String cookie=share.getString("sessionid","");
                System.out.println(cookie);
                System.out.println(share.getString("sessionid",null));

                queryFromServer(url,object.toString(),cookie);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
    private void queryFromServer(String url,String json,String session){
        HttpUtilGet.sendOkHttpRequestGet(url,json,session,new okhttp3.Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                        refreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String responseText=response.body().string();
                System.out.println("r2222"+responseText+"获取所有信息");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{

                            JSONObject jsonObject = new JSONObject(responseText);
                            String status = jsonObject.getString("status");
                            if (status.equals("success")) {
                                LitePal.deleteAll(indexStudents.class);
                                studentsList.clear();
                                //  System.out.println("此时的长度，额"+studentsList.size());
                                adapter.notifyDataSetChanged();
                                Utility.handleMyAndAllResponse(responseText);
                                refreshLayout.setRefreshing(false);
                                queryStudents();
                            }
                            if(status.equals("fail")){
                                System.out.println("asdadadz+ssssss5555"+status);
                                String address = duankou.getDuanKou()+"login" ;
                                JSONObject object=new JSONObject();
                                try {
                                    SharedPreferences sharedPreference = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
                                    SharedPreferences share = getActivity().getSharedPreferences("Session",MODE_PRIVATE);
                                    object.put("studentID", sharedPreference.getString("username",""));
                                    object.put("password", sharedPreference.getString("password",""));
                                    String data = object.toString();
                                    HttpUtilPost.sendOkHttpRequest(address, data,null,new okhttp3.Callback(){
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getContext(),"刷新session失败", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            Headers headers =response.headers();
                                            List cookies = headers.values("Set-Cookie");
                                            String session = (String)cookies.get(0);
                                            String sessionid = session.substring(0,session.indexOf(";"));
                                            SharedPreferences share = getActivity().getSharedPreferences("Session",MODE_PRIVATE);
                                            SharedPreferences.Editor edit = share.edit();//编辑文件
                                            edit.putString("sessionid",sessionid);
                                            edit.apply();
                                            Message message=new Message();
                                            message.what=6;
                                            handler.sendMessage(message);

                                        }
                                    });
                                }catch (JSONException E){
                                    E.printStackTrace();
                                }
                            }
                        }catch (JSONException E){
                            E.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
