package com.github.zer0e.mbot.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpUtil {
    private static final OkHttpClient client = new OkHttpClient();
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    private static int error_time = 0;
    private static int retry_time = 1;

    // 同步get请求
    public static JSONObject get(String url){
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = client.newCall(request);
        JSONObject obj = null;
        try {
            Response response = call.execute();
            obj = JSON.parseObject(response.body().toString());
            error_time = 0;
        }catch (Exception e){
            e.printStackTrace();
            if (error_time < retry_time){
                logger.info("重试连接中");
                error_time++;
                return get(url);
            }
        }
        return obj;
    }

    // 同步post请求
    public static JSONObject post(String url, String data){
        MediaType mediaType = MediaType.parse("text/html; charset=utf-8");
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(data, mediaType))
                .build();

        JSONObject obj = null;
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            String res = response.body().string();
            obj = JSON.parseObject(res);
            error_time = 0;
        }catch (Exception e){
            e.printStackTrace();
            if (error_time < retry_time){
                logger.info("重试连接中");
                error_time++;
                return post(url, data);
            }
        }

        return obj;

    }

    public static String get_with_string(String url) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            error_time = 0;
            return response.body().string();
        }catch (Exception e){
            e.printStackTrace();
            if (error_time < retry_time){
                logger.info("重试连接中");
                error_time++;
                return get_with_string(url);
            }
        }
        return null;
    }
}
