package com.github.zer0e.zbot.msg;

import lombok.Data;

@Data
public class TempMsg extends Msg{
    private String sender_group_id;
    private String sender_id;
    private String text;
    private String type = "TempMessage";

    public TempMsg(String sender_group_id, String sender_id, String text) {
        this.sender_group_id = sender_group_id;
        this.sender_id = sender_id;
        this.text = text;
    }
}
