package com.github.zer0e.zbot.plugins.base;

import com.github.zer0e.zbot.msg.FriendMsg;
import com.github.zer0e.zbot.msg.TempMsg;

/*
* 好友插件必须实现好友私聊和临时会话私聊两种方式
* 不想处理某种类型的消息就任意返回
* */
public interface FriendPlugin {
    int callback(FriendMsg msg);
    int callback(TempMsg msg);
}
