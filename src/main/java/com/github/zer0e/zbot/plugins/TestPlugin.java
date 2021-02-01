package com.github.zer0e.zbot.plugins;


import com.github.zer0e.zbot.core.Api;
import com.github.zer0e.zbot.msg.FriendMsg;
import com.github.zer0e.zbot.msg.GroupMsg;
import com.github.zer0e.zbot.msg.TempMsg;
import com.github.zer0e.zbot.plugins.base.FriendPlugin;
import com.github.zer0e.zbot.plugins.base.GroupPlugin;
import com.github.zer0e.zbot.plugins.base.KeywordPlugin;

public class TestPlugin extends KeywordPlugin implements GroupPlugin, FriendPlugin {
    private final Api api = Api.getApi();

    @Override
    public void init() {
        // 初始化关键字与监听的群组
        this.group_words_set.add("test");
        this.group_ids_set.add("*");

        // 初始化私聊关键字和监听人id
        this.friend_words_set.add("test");
        this.friend_ids_set.add("*");
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
