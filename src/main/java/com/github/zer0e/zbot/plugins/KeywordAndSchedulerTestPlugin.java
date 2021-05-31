package com.github.zer0e.zbot.plugins;

import com.github.zer0e.zbot.core.Api;
import com.github.zer0e.zbot.msg.FriendMsg;
import com.github.zer0e.zbot.msg.TempMsg;
import com.github.zer0e.zbot.plugins.base.FriendPlugin;
import com.github.zer0e.zbot.plugins.base.KeywordPlugin;
import com.github.zer0e.zbot.plugins.base.SchedulerPlugin;

/**
 * @ClassName KeywordAndSchedulerTestPlugin
 * @Description
 * @Author zer0e
 * @Date 2021-05-31 12:55
 **/
public class KeywordAndSchedulerTestPlugin extends KeywordPlugin implements FriendPlugin, SchedulerPlugin {
    private final Api api = Api.getApi();

    @Override
    public void execute() {
        api.send_plain_msg_to_friend("123456","scheduler msg");
    }

    @Override
    public void init() {
        this.schedulerTimeSet.add("0 0/2 * * * ?");
        this.friend_words_set.add("test");
        this.friend_ids_set.add("*");
    }

    @Override
    public int callback(FriendMsg msg) {
        api.send_plain_msg_to_friend(msg.getSender_id(), "收到test");
        return 0;
    }

    @Override
    public int callback(TempMsg msg) {
        return 0;
    }
}
