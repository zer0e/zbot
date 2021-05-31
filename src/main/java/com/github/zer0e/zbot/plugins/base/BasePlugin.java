package com.github.zer0e.zbot.plugins.base;

import java.util.HashSet;
import java.util.Set;

public abstract class BasePlugin {
    public abstract void init();
    public Set<String> schedulerTimeSet = new HashSet<>();
}
