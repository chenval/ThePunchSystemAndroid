package com.example.thepunchsystemandroid.tool;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtilGet {
    public static void sendOkHttpRequestGet(String address,String json ,String session,okhttp3.Callback callback ){
        OkHttpClient client=new OkHttpClient();
        if(session!=null){
            Request request=new Request.Builder()
                    .url(address)
                    .get()
                    .header("Cookie",session)
                    .build();
            client.newCall(request).enqueue(callback);
        }else {
            Request request=new Request.Builder()
                    .url(address)
                    .get()
                    .build();
            client.newCall(request).enqueue(callback);
        }


    }
}
