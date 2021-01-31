package com.github.zer0e.zbot.plugins.base;

import java.util.HashSet;
import java.util.Set;

public abstract class KeywordPlugin extends BasePlugin {
    public Set<String> friend_words_set = new HashSet<>();
    public Set<String> friend_ids_set = new HashSet<>();
    public Set<String> group_words_set = new HashSet<>();
    public Set<String> group_ids_set = new HashSet<>();
    // 插件是否将关键词开放给其他插件
    // 即是否添加关键词到Registry中的所有关键词集合中
    public boolean keyword_open = true;
}
