package com.example.thepunchsystemandroid.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.thepunchsystemandroid.ActivityList;
import com.example.thepunchsystemandroid.LoginActivity;
import com.example.thepunchsystemandroid.R;
import com.example.thepunchsystemandroid.updateActivity;

public class SettingFragment extends DialogFragment {
    private TextView log_off;
    private TextView log_out;
    private TextView update;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View settings = inflater.inflate(R.layout.fragment_setting, container,
                false);
        update=settings.findViewById(R.id.update);
        log_off=(TextView) settings.findViewById(R.id.sign_out);
        log_out=(TextView) settings.findViewById(R.id.sign_out_app);
        return settings;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "更新暂未开放", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), updateActivity.class);
                startActivity(intent);
            }
        });
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityList.finishAll();
            }
        });
        log_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("退出当前帐号");
                builder.setMessage("退出后会返回登录界面,你是否确认退出当前账号?");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("确认退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
                        sharedPreferences.edit().putBoolean("info",false).apply();
                        SharedPreferences a=getActivity().getSharedPreferences("Session", Context.MODE_PRIVATE);
                        sharedPreferences.edit().
                                clear().apply();
                        ActivityList.finishAll();
                        goToActivity(LoginActivity.class,null);
                    }
                });
                builder.show();
            }
        });
    }
    public void goToActivity(Class Activity,Bundle bundle){
        Intent intent = new Intent(getActivity(),Activity);
        if (bundle!=null&&bundle.size()!=0){
            intent.putExtra("data",bundle);
        }
        startActivity(intent);
    }


}
