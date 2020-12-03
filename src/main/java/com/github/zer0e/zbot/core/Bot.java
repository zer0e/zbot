package com.github.zer0e.zbot.core;

import com.alibaba.fastjson.JSONObject;
import com.github.zer0e.zbot.core.handler.MsgHandler;
import com.github.zer0e.zbot.core.handler.ScheduleHandler;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.LinkedBlockingQueue;

@Data
public class Bot {
    private MsgHandler msgHandler;
    private ScheduleHandler scheduleHandler;
    private PluginLoader plugin_loader;
    private Receiver receiver;
    private Registry registry;
    private LinkedBlockingQueue<JSONObject> queue;
    private Api api;
    private Thread msg_handler_thread;
    private Thread receiver_thread;
    private Thread health_thread;
    private Thread scheduler_thread;

    private final static Logger logger = LoggerFactory.getLogger(Bot.class);

    public Bot() {
        api = Api.getApi();
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
        if (msgHandler == null){
            msgHandler = new MsgHandler(registry, queue);
        }
        if (scheduleHandler == null){
            scheduleHandler = new ScheduleHandler(registry);
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
        if (msgHandler == null || receiver == null || registry == null || scheduleHandler == null){
            logger.error("启动失败，请先创建bot");
            return false;
        }
        try {
            msg_handler_thread = new Thread(new Runnable() {
                public void run() {
                    msgHandler.start();
                }
            });
            receiver_thread = new Thread(new Runnable() {
                public void run() {
                    receiver.start();
                }
            });
            health_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    health();
                }
            });
            scheduler_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    scheduleHandler.start();
                }
            });

            msg_handler_thread.start();
            receiver_thread.start();
            scheduler_thread.start();
            health_thread.start();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean stop(){
        if (msg_handler_thread == null || receiver_thread == null || scheduler_thread == null){
            logger.info("无需停止");
            return true;
        }
        try {
            msgHandler.setStop(true);
            msg_handler_thread.interrupt();
            receiver.stop();
            scheduleHandler.stop();
            msg_handler_thread.join();
            receiver_thread.join();
            scheduler_thread.join();
        }catch (InterruptedException ignored){

        }
        logger.info("停止所有线程");
        return true;
    }

    // 负责当receiver退出时，将所有线程结束
    public void health(){
        logger.info("健康线程启动");

        try {
            while (true){
                if (receiver_thread == null || msg_handler_thread == null || scheduler_thread == null){
                    continue;
                }
                if (receiver.is_close){
                    this.stop();
                    break;
                }
                Thread.sleep(1);
            }
        }catch (InterruptedException ignored){

        }finally {
            logger.info("健康线程结束");
        }


    }


    public static void main(String[] args) {
        Bot bot = new Bot().build();
        bot.start();
    }

}
