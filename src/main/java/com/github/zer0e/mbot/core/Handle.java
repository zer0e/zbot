package com.github.zer0e.mbot.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.zer0e.mbot.model.GroupMsg;
import com.github.zer0e.mbot.utils.ReflectionUtils;
import lombok.Data;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.*;
import java.util.concurrent.*;

@Data
public class Handle {
    private Registry registry;
    private LinkedBlockingQueue<JSONObject> exchange;
    private volatile boolean stop = false;
    private static Logger logger = LoggerFactory.getLogger(Handle.class);
    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public Handle(Registry registry, @NonNull LinkedBlockingQueue<JSONObject> exchange) {
        this.registry = registry;
        this.exchange = exchange;
    }

    public void handle_msg(JSONObject msg){
        logger.debug("接收消息：" + msg.toString());
        try {
            String type = (String) msg.get("type");
            if (type.equals("GroupMessage")){
                handle_group_msg(msg);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void handle_group_msg(JSONObject msg) throws Exception{
        // 先处理发送者
        String sender = msg.getString("sender").toString();
        JSONObject object = JSONObject.parseObject(sender);
        String sender_id = object.get("id").toString();
        JSONObject group_json = JSONObject.parseObject(object.get("group").toString());
        String group_id = group_json.get("id").toString();
        boolean has_any_group = this.registry.getListen_groups().containsKey("*");
        if (!has_any_group && !this.registry.getListen_groups().containsKey(group_id)){
            logger.debug("sender group id : " + group_id + " 不在监听列表中");
            return;
        }
        Set<UUID> uuid_groups = null;
        if (has_any_group){
            uuid_groups = this.registry.getListen_groups().get("*");
        }else{
            uuid_groups = this.registry.getListen_groups().get(group_id);
        }

        // 处理messageChain
        String messageChain = msg.get("messageChain").toString();
        StringBuilder sb = new StringBuilder();
        JSONArray jsonArray = JSON.parseArray(messageChain);
        for (Object array : jsonArray){
            JSONObject o = JSONObject.parseObject(array.toString());
            if (o.get("type").toString().equals("Plain")){
                sb.append(o.get("text").toString());
            }
        }
        logger.debug("消息内容：" + sb.toString());
        Set<UUID> uuid_words = new HashSet<>();
        for (String keyword : this.registry.getListen_words().keySet()){
            if (sb.toString().contains(keyword)){
                Set<UUID> uuid = this.registry.getListen_words().get(keyword);
                uuid_words.addAll(uuid);
            }
        }
        if (uuid_words.isEmpty()){
            logger.debug("不存在此关键字的插件");
            return;
        }
        GroupMsg groupMsg = new GroupMsg(group_id, sender_id, sb.toString());


        // 取交集 得到需要回调的UUID
        Set<UUID> result = new HashSet<>(uuid_groups);
        result.retainAll(uuid_words);
        if (!result.isEmpty()){
            for (UUID uuid : result){
                logger.debug("call : " + uuid);
                this.registry.call(groupMsg, uuid);
            }
        }

    }

    public void start() throws InterruptedException {
        while (!stop){
            JSONObject msg = this.exchange.poll(60*60*24, TimeUnit.SECONDS);
            if (msg != null){
                executorService.execute(() -> {
                    handle_msg(msg);
                });
            }
        }
    }

}
