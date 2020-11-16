package com.github.zer0e.zbot.msg;

import lombok.Data;

@Data
public abstract class Msg {
    protected String text;
    protected String sender_id;
    protected String type;
}
