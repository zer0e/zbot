package com.github.zer0e.mbot.model;

import lombok.Data;

@Data
public class GroupMsg extends Msg{
    private String sender_group;
    private String sender;
    private String text;
    private String type = "GroupMessage";

    public GroupMsg(String sender_group, String sender, String text) {
        this.sender_group = sender_group;
        this.sender = sender;
        this.text = text;
    }
}
