package com.example.thepunchsystemandroid.tool;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtilPost {
   public static void sendOkHttpRequest(String address,String json ,String session,okhttp3.Callback callback ){
       OkHttpClient client=new OkHttpClient();
       MediaType JSON = MediaType.parse("application/json; charset=utf-8");
       RequestBody body=RequestBody.create(JSON,json);
       if(session!=null){
           Request request=new Request.Builder()
                   .url(address)
                   .post(body)
                   .header("Cookie",session)
                   .build();
           client.newCall(request).enqueue(callback);
       }else {
           Request request=new Request.Builder()
                   .url(address)
                   .post(body)
                   .build();
           client.newCall(request).enqueue(callback);
       }


   }
}
