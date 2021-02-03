package com.github.zer0e.zbot.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.github.zer0e.zbot.utils.interceptor.RetryInterceptor;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

// 请求异常交由请求的方法来处理
public class HttpUtil {
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .retryOnConnectionFailure(false)
            .addInterceptor(new RetryInterceptor())
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20,TimeUnit.SECONDS)
            .writeTimeout(20,TimeUnit.SECONDS)
            .connectionPool(new ConnectionPool(32,2,TimeUnit.MINUTES))
            .build();
    private static final HttpUtil httpUtil = new HttpUtil();
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public static HttpUtil getInstance(){
        return httpUtil;
    }

    private HttpUtil() {
    }

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
        }catch (Exception e){
            e.printStackTrace();
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
        }catch (Exception e){
            e.printStackTrace();
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
        String res = null;
        try {
            Response response = call.execute();
            res = response.body().string();
//            logger.debug(res);
            obj = JSON.parseObject(res);
        }catch (Exception e){
            e.printStackTrace();
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
            return response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
