package com.github.zer0e.mbot.core;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.LinkedBlockingQueue;

@Data
public class Bot {
    private Handle handle;
    private PluginLoader plugin_loader;
    private Receiver receiver;
    private Registry registry;
    private LinkedBlockingQueue<JSONObject> queue;
    private Api api;
    private Thread handle_thread;
    private Thread receiver_thread;

    private final static Logger logger = LoggerFactory.getLogger(Bot.class);

    public Bot() {
        api = new Api();
    }

    public Bot build(){
        if (queue == null){
            queue = new LinkedBlockingQueue<JSONObject>();
        }
        if (plugin_loader == null){
            plugin_loader = new PluginLoader();
        }
        if (registry == null){
            registry = new Registry(plugin_loader);
        }
        if (handle == null){
            handle = new Handle(registry, queue);
        }
        if (receiver == null){
            String session = api.get_session();
            String ws_uri = api.getWs_base_url() + api.getWs_get_msg_url() + session;
            try {
                URI serverUri = new URI(ws_uri);
                receiver = new Receiver(serverUri,queue);
            }catch (URISyntaxException e) {
                e.printStackTrace();
                throw new RuntimeException("ws监听地址错误");
            }
        }
        return this;
    }

    public boolean start(){
        if (handle == null || receiver == null || registry == null){
            logger.error("启动失败，请先创建bot");
            return false;
        }
        try {
            handle_thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        handle.start();
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        logger.info("处理线程退出");
                    }
                }
            });
            receiver_thread = new Thread(new Runnable() {
                public void run() {
                    receiver.start();
                }
            });
            handle_thread.start();
            receiver_thread.start();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean stop(){
        if (handle_thread == null || receiver_thread == null){
            logger.info("无需停止");
            return true;
        }
        handle.setStop(true);
        receiver.stop();
        return true;
    }

    public static void main(String[] args) {
        Bot bot = new Bot().build();
        bot.start();
    }

}
