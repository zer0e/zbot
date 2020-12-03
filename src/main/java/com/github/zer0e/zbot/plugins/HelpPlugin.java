package com.github.zer0e.zbot.plugins;

import com.github.zer0e.zbot.core.Api;
import com.github.zer0e.zbot.core.Registry;
import com.github.zer0e.zbot.msg.FriendMsg;
import com.github.zer0e.zbot.msg.GroupMsg;
import com.github.zer0e.zbot.msg.TempMsg;
import com.github.zer0e.zbot.plugins.base.FriendPlugin;
import com.github.zer0e.zbot.plugins.base.GroupPlugin;
import com.github.zer0e.zbot.plugins.base.KeywordPlugin;

public class HelpPlugin extends KeywordPlugin implements FriendPlugin, GroupPlugin {
    private final Api api = Api.getApi();
    public HelpPlugin() {
        init();
    }

    @Override
    protected void init() {
        this.group_words_set.add("help");
        this.group_ids_set.add("*");
        this.friend_words_set.add("help");
        this.friend_words_set.add("帮助");
        this.friend_ids_set.add("*");
    }

    @Override
    public int callback(FriendMsg msg) {
        api.send_plain_msg_to_friend(msg.getSender_id(),getReply(false));
        return 0;
    }

    @Override
    public int callback(TempMsg msg) {
        api.send_plain_msg_to_tmp_friend(msg.getSender_group_id(),msg.getSender_id(),getReply(false));
        return 0;
    }

    @Override
    public int callback(GroupMsg msg) {
        api.send_plain_msg_to_group(msg.getSender_group(),getReply(true));
        return 0;
    }

    private String getReply(boolean is_group){
        StringBuilder sb = new StringBuilder();
        sb.append("当前可使用命令如下：\n");
        if (is_group){
            for (String keyword : Registry.group_keywords){
                sb.append(keyword).append("\n");
            }
        }else{
            for (String keyword : Registry.friend_keywords){
                sb.append(keyword).append("\n");
            }
        }
        return sb.toString();

    }
}
