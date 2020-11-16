package com.github.zer0e.zbot.msg;

import lombok.Data;

@Data
public class GroupMsg extends Msg{
    private String sender_group;
    private String sender_id;
    private String text;
    private String type = "GroupMessage";

    public GroupMsg(String sender_group, String sender_id, String text) {
        this.sender_group = sender_group;
        this.sender_id = sender_id;
        this.text = text;
    }
}
