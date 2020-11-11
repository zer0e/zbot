package com.github.zer0e.mbot.plugins;

import com.github.zer0e.mbot.model.Msg;
import lombok.Getter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
public abstract class BasePlugin {
    protected Set<String> words = new HashSet<>();
    protected Set<String> groups = new HashSet<>();
    protected String description;
    protected abstract void init();
    public abstract int callback(Msg msg);
}
