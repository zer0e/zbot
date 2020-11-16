package com.github.zer0e.zbot;

import com.github.zer0e.zbot.core.Bot;

public class Run {
    public static void main(String[] args) {
        Bot bot = new Bot().build();
        bot.start();
    }
}
