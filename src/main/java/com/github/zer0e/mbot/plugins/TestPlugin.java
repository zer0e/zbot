package com.github.zer0e.mbot.plugins;


import com.github.zer0e.mbot.core.Api;
import com.github.zer0e.mbot.msg.FriendMsg;
import com.github.zer0e.mbot.msg.GroupMsg;
import com.github.zer0e.mbot.msg.TempMsg;

public class TestPlugin extends KeywordPlugin implements GroupPlugin,FriendPlugin{
    private Api api;
    public TestPlugin() {
        // 初始化
        init();
    }

    @Override
    public void init() {
        // 初始化关键字与监听的群组
        this.group_words_set.add("test");
        this.group_ids_set.add("*");

        // 初始化私聊关键字和监听人id
        this.friend_words_set.add("test");
        this.friend_ids_set.add("*");

        // 初始化API避免大量申请session
        api = new Api();
    }

    @Override
    public int callback(FriendMsg msg) {
        boolean is_ok = api.send_plain_msg_to_friend(msg.getSender_id(), "收到test");
        return is_ok ? 1 : 0;
    }

    @Override
    public int callback(TempMsg msg) {
        boolean is_ok = api.send_plain_msg_to_tmp_friend(msg.getSender_group_id(),msg.getSender_id(), "收到test");
        return is_ok ? 1 : 0;
    }

    @Override
    public int callback(GroupMsg msg) {
        boolean is_ok = api.send_plain_msg_to_group(msg.getSender_group(), "收到test");
        return is_ok ? 1 : 0;
    }
}
