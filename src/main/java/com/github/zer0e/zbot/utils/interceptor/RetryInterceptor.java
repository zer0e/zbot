package com.github.zer0e.zbot.utils.interceptor;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ConnectException;

public class RetryInterceptor implements Interceptor {
    // okhttp的拦截器不是线程安全的 重试操作需要分开计算
    // 用来存放count 即当前请求次数
    // TODO 也许有更好的解决方式
    private volatile ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
    // 最多重试次数 请求总数为max_retry_time + 1
    private final int max_retry_time = 1;

    private Logger logger = LoggerFactory.getLogger(RetryInterceptor.class);

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        return retry(chain);
    }

    private Response retry(Chain chain) throws IOException{
        Response response = null;
        Request request = chain.request();
        int count = threadLocal.get() == null ? 0 : threadLocal.get();
        try {
            response = chain.proceed(request);
            while (!response.isSuccessful() && count < max_retry_time){
                threadLocal.set(++count);
                logger.info("url: " + request.url() + " 正在第 " + count + " 次重试");
                response.close();
                response = chain.proceed(request);
            }
        }catch (ConnectException e){
            if (count < max_retry_time){
                threadLocal.set(++count);
                logger.info("url: " + request.url() + " 正在第 " + count + " 次重试");
                response = retry(chain);
            }

        }
        threadLocal.set(0);
        if (response == null)
            throw new ConnectException("尝试重连后依旧失败 url: " + request.url());
        return response;
    }

}
