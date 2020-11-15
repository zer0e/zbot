package com.github.zer0e.mbot.plugins;

import com.github.zer0e.mbot.msg.GroupMsg;

import java.util.HashSet;
import java.util.Set;

public interface GroupPlugin {
    Set<String> group_words_set = new HashSet<>();
    Set<String> group_ids_set = new HashSet<>();
    void init();
    int callback(GroupMsg msg);
}
