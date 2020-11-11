package com.github.zer0e.mbot;

import com.github.zer0e.mbot.core.Bot;

public class Run {
    public static void main(String[] args) {
        Bot bot = new Bot().build();
        bot.start();
    }
}
