package top.selzt.mycloud.Util;


import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendRequest {
    public static void Post(String url, RequestBody requestBody,Callback callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                client.newCall(request).enqueue(callback);
            }
        }).start();
    }
}
