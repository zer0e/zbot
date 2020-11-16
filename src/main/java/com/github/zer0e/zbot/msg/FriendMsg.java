package com.github.zer0e.zbot.msg;

import lombok.Data;

@Data
public class FriendMsg extends Msg{
    private String sender_id;
    private String text;
    private String type = "FriendMessage";

    public FriendMsg(String sender_id, String text) {
        this.sender_id = sender_id;
        this.text = text;
    }
}
