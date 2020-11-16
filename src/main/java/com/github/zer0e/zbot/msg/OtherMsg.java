package com.github.zer0e.zbot.msg;

public class OtherMsg extends Msg{
    private String sender_id;
    private String text;
    private String type;

    public OtherMsg(String sender_id, String text, String type) {
        this.sender_id = sender_id;
        this.text = text;
        this.type = type;
    }
}
