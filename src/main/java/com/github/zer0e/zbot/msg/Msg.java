package com.github.zer0e.mbot.msg;

import lombok.Data;

@Data
public abstract class Msg {
    protected String text;
    protected String sender_id;
    protected String type;
}
