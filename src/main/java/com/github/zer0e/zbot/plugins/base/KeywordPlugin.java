package com.github.zer0e.zbot.plugins.base;

import java.util.HashSet;
import java.util.Set;

public abstract class KeywordPlugin extends BasePlugin {
    public Set<String> friend_words_set = new HashSet<>();
    public Set<String> friend_ids_set = new HashSet<>();
    public Set<String> group_words_set = new HashSet<>();
    public Set<String> group_ids_set = new HashSet<>();
}
