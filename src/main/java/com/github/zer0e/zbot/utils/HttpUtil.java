package com.github.zer0e.zbot.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;

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
            obj = JSON.parseObject(response.body().string());
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

    public static byte[] get_with_bytes(String url){
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = client.newCall(request);
        byte[] bytes = null;
        try {
            Response response = call.execute();
            bytes = response.body().bytes();
            error_time = 0;
        }catch (Exception e){
            e.printStackTrace();
            if (error_time < retry_time){
                logger.info("重试连接中");
                error_time++;
                return get_with_bytes(url);
            }
        }
        return bytes;
    }

    // 上传图片文件
    public static JSONObject post_img(String url, String filename,Map<String,String> args){
        return post_img(url,"img",filename,args);
    }

    public static JSONObject post_img(String url, String file_form_name,String filename, Map<String,String> args){
        File file = new File(filename);
        if (!file.exists()){
            logger.error("post img is not exists");
            return null;
        }
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MediaType.parse("multipart/form-data"));
        for (String key : args.keySet()){
            builder.addFormDataPart(key,args.get(key));
        }
        RequestBody FileBody = RequestBody.create(MediaType.parse("image/png"), file);
        builder.addFormDataPart(file_form_name,file.getName(),FileBody);

        MultipartBody requestBody = builder.build();
        JSONObject res = null;
        try{
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            Response response = client.newCall(request).execute();
            res = JSONObject.parseObject(response.body().string());
            if (res.getOrDefault("url","").equals("")){
                res = null;
            }
        }catch (Exception e){
            e.printStackTrace();
            res = null;
        }
        return res;
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
//            logger.debug(res);
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
