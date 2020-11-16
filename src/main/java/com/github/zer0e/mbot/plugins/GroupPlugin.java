package com.github.zer0e.mbot.plugins;

import com.github.zer0e.mbot.msg.GroupMsg;

import java.util.HashSet;
import java.util.Set;

public interface GroupPlugin {
    int callback(GroupMsg msg);
}
