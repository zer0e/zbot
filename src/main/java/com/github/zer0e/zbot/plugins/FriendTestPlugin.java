package com.github.zer0e.zbot.plugins;

import com.github.zer0e.zbot.core.Api;
import com.github.zer0e.zbot.msg.FriendMsg;
import com.github.zer0e.zbot.msg.TempMsg;
import com.github.zer0e.zbot.plugins.base.FriendPlugin;
import com.github.zer0e.zbot.plugins.base.KeywordPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName FriendTestPlugin
 * @Description
 * @Author zer0e
 * @Date 2021-03-09 21:48
 **/
public class FriendTestPlugin extends KeywordPlugin implements FriendPlugin {
    // 插件需要回复消息，即用到api
    private final Api api = Api.getApi();
    // 如您需要使用logger，本项目使用的是slf4j，请注意引用
    private static Logger logger = LoggerFactory.getLogger(FriendTestPlugin.class);

    @Override
    public void init() {
        this.friend_words_set.add("test");
        this.friend_ids_set.add("*");
    }

    @Override
    public int callback(FriendMsg msg) {
        boolean is_send = api.send_plain_msg_to_friend(msg.getSender_id(), "收到test");
        return 0;
    }

    @Override
    public int callback(TempMsg msg) {
        boolean is_send = api.send_plain_msg_to_tmp_friend(msg.getSender_group(),msg.getSender_id(), "收到test");
        return 0;
    }
}
