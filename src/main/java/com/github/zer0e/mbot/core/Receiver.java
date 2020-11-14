package com.github.zer0e.mbot.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.zer0e.mbot.utils.ConfigUtil;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Queue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class Receiver extends WebSocketClient {
    private Queue<JSONObject> exchange;
    private static Logger logger = LoggerFactory.getLogger(Receiver.class);
    public Receiver(URI serverUri, Queue<JSONObject> exchange) {
        super(serverUri);
        this.exchange = exchange;
    }

    @Override
    public void onMessage(String s) {
        logger.info(s);
        try {
            JSONObject obj = JSON.parseObject(s);
            this.exchange.add(obj);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onClose(int i, String s, boolean b) {
        logger.info("关闭连接");
    }

    public void onError(Exception e) {
        logger.error("连接错误");
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        logger.info("开始连接");
    }

    public boolean start(){
        this.connect();
        return true;
    }
    public boolean stop(){
        this.close();
        return true;
    }

}
