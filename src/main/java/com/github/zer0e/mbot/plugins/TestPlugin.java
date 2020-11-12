package com.github.zer0e.mbot.plugins;


import com.github.zer0e.mbot.core.Api;
import com.github.zer0e.mbot.model.GroupMsg;
import com.github.zer0e.mbot.model.Msg;

public class TestPlugin extends BasePlugin{
    public TestPlugin() {
        // 初始化
        init();
    }

    @Override
    protected void init() {
        // 初始化关键字与监听的群组
        this.description = "测试插件";
        this.words.add("test");
        this.groups.add("*");
    }


    @Override
    public int callback(Msg msg) {
        Api api = new Api();
        api.send_plain_msg_to_group(((GroupMsg) msg).getSender_group(), "收到test");
        return 1;
    }
}
