package com.github.zer0e.zbot.core.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.zer0e.zbot.core.Registry;
import com.github.zer0e.zbot.msg.*;
import lombok.Data;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Data
public class MsgHandler {
    private Registry registry;
    private LinkedBlockingQueue<JSONObject> exchange;
    private volatile boolean stop = false;
    private static Logger logger = LoggerFactory.getLogger(MsgHandler.class);
    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public MsgHandler(@NonNull Registry registry, @NonNull LinkedBlockingQueue<JSONObject> exchange) {
        this.registry = registry;
        this.exchange = exchange;
    }

    public void handle_msg(JSONObject msg){
        logger.debug("接收消息：" + msg.toString());
        Msg _msg = parseMessage(msg);
        if (_msg == null){
            return ;
        }
        if (_msg.getType().equals("GroupMessage")){
            handle_msg((GroupMsg)_msg);
        }else if (_msg.getType().equals("FriendMessage")){
            handle_msg((FriendMsg)_msg);
        }else if (_msg.getType().equals("TempMessage")){
            handle_msg((TempMsg)_msg);
        }else{
            logger.error("未知类型的消息：" + _msg.toString());
        }
    }

    public void handle_msg(GroupMsg msg){
        logger.debug("处理群消息: " + msg.toString());
        boolean has_any_group = this.registry.getListen_group_ids().containsKey("*");
        if (!has_any_group && this.registry.getListen_group_ids().containsKey(msg.getSender_group())){
            logger.debug("sender group id : " + msg.getSender_group() + " 不在监听列表中");
            return;
        }
        Set<UUID> uuid_groups = null;
        if (has_any_group){
            uuid_groups = this.registry.getListen_group_ids().get("*");
        }else{
            uuid_groups = this.registry.getListen_group_ids().get(msg.getSender_group());
        }
        Set<UUID> uuid_words = new HashSet<>();
        for (String keyword : this.registry.getListen_group_words().keySet()){
            if (msg.getText().contains(keyword)){
                Set<UUID> uuid = this.registry.getListen_group_words().get(keyword);
                uuid_words.addAll(uuid);
            }
        }
        if (uuid_words.isEmpty()){
            logger.debug("不存在此关键字的插件");
            return;
        }
        Set<UUID> result = new HashSet<>(uuid_groups);
        result.retainAll(uuid_words);
        if (!result.isEmpty()){
            for (UUID uuid : result){
                logger.debug("call : " + uuid);
                this.registry.call(msg, uuid);
            }
        }else{
            logger.debug("监听词与监听者不匹配");
        }
    }

    public void handle_msg(FriendMsg msg){
        logger.debug("处理好友消息: " + msg.toString());
        Set<UUID> uuids = getResultUuids(msg.getSender_id(), msg.getText());
        for (UUID uuid : uuids){
            logger.debug("call: " + uuid);
            this.registry.call(msg, uuid);
        }
    }

    // 临时消息与好友消息处理一致
    public void handle_msg(TempMsg msg){
        logger.debug("处理临时消息: " + msg.toString());
        Set<UUID> uuids = getResultUuids(msg.getSender_id(), msg.getText());
        for (UUID uuid : uuids){
            logger.debug("call: " + uuid);
            this.registry.call(msg, uuid);
        }
    }
    // 从发送者和消息中获取需要调用的插件uuid
    private Set<UUID> getResultUuids(String sender_id, String text){
        Set<UUID> result = new HashSet<>();
        boolean has_any_friend = this.registry.getListen_friend_ids().containsKey("*");
        if (!has_any_friend && this.registry.getListen_group_ids().containsKey(sender_id)){
            logger.debug("sender id : " + sender_id + " 不在监听列表中");
            return result;
        }
        Set<UUID> uuid_friends = null;
        if (has_any_friend){
            uuid_friends = this.registry.getListen_friend_ids().get("*");
        }else{
            uuid_friends = this.registry.getListen_friend_ids().get(sender_id);
        }
        Set<UUID> uuid_words = new HashSet<>();
        for (String keyword : this.registry.getListen_friend_words().keySet()){
            if (text.contains(keyword)){
                Set<UUID> uuid = this.registry.getListen_friend_words().get(keyword);
                uuid_words.addAll(uuid);
            }
        }
        if (uuid_words.isEmpty()){
            logger.debug("不存在此关键字的插件");
            return result;
        }
        result.addAll(uuid_friends);
        result.retainAll(uuid_words);
        if (result.isEmpty()){
            logger.debug("监听词与监听者不匹配");
        }
        return result;
    }

    // 从json数据变成model
    private Msg parseMessage(JSONObject msg){
        // 在这初始化消息
        String type = null;
        String text = null;
        String sender_id = null;
        String sender_group_id = null;
        try {
            type = msg.getString("type");
            // 处理messageChain
            String messageChain = msg.getString("messageChain");
            StringBuilder sb = new StringBuilder();
            JSONArray jsonArray = JSON.parseArray(messageChain);
            for (Object array : jsonArray){
                JSONObject o = JSONObject.parseObject(array.toString());
                if (o.getString("type").equals("Plain")){
                    sb.append(o.get("text").toString());
                }
            }
            text = sb.toString();
            String sender_json = msg.getString("sender");
            JSONObject object = JSONObject.parseObject(sender_json);
            sender_id = object.getString("id");
            // 这里可能会出错
            JSONObject group_json = JSONObject.parseObject(object.getString("group"));
            sender_group_id = group_json.getString("id");
        }catch (Exception ignored){
        }
        Msg _msg = null;
        if (type == null){
            return null;
        }
        switch (type) {
            case "GroupMessage":
                _msg = new GroupMsg(sender_group_id, sender_id, text);
                break;
            case "FriendMessage":
                _msg = new FriendMsg(sender_id, text);
                break;
            case "TempMessage":
                _msg = new TempMsg(sender_group_id, sender_id, text);
                break;
            default:
                _msg = new OtherMsg(sender_id, text, type);
                break;
        }
        return _msg;
    }


    public void start(){
        logger.info("handler线程启动");
        while (!stop && !Thread.currentThread().isInterrupted()){
            try {
                JSONObject msg = this.exchange.poll(60*60*24, TimeUnit.SECONDS);
                if (msg != null){
                    executorService.execute(() -> {
                        handle_msg(msg);
                    });
                }
            }catch (InterruptedException ignored) {
            }
        }
        logger.info("退出handler");
    }

}