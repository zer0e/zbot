package com.github.zer0e.mbot.plugins;

import com.github.zer0e.mbot.msg.FriendMsg;
import com.github.zer0e.mbot.msg.TempMsg;

import java.util.HashSet;
import java.util.Set;

/*
* 好友插件必须实现好友私聊和临时会话私聊两种方式
* 不想处理某种类型的消息就任意返回
* */
public interface FriendPlugin {
    Set<String> friend_words_set = new HashSet<>();
    Set<String> friend_ids_set = new HashSet<>();
    void init();
    int callback(FriendMsg msg);
    int callback(TempMsg msg);
}