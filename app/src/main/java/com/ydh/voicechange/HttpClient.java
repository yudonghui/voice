package com.ydh.voicechange;

import com.ydh.voicechange.interfaces.YdhInterface;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ydh on 2021/11/11
 */
public class HttpClient {
    private static HttpClient instance;

    public static HttpClient getInstantce() {
        if (instance == null) {
            synchronized (HttpClient.class) {
                if (instance == null) {
                    instance = new HttpClient();
                }
            }
        }
        return instance;
    }

    public void post(String url, FormBody body, final YdhInterface mListener) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (mListener != null) {
                    mListener.onSuccess(call.toString());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    if (mListener != null) {
                        mListener.onSuccess(result);
                    }
                    //处理UI需要切换到UI线程处理
                }
            }
        });
    }

    public FormBody getFormBody(HashMap<String, String> map) {
        FormBody.Builder builder = new FormBody.Builder();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }
}
