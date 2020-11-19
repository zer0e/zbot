package com.github.zer0e.zbot.core;

import com.github.zer0e.zbot.msg.FriendMsg;
import com.github.zer0e.zbot.msg.GroupMsg;
import com.github.zer0e.zbot.msg.TempMsg;
import com.github.zer0e.zbot.utils.ReflectionUtils;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Getter
public class Registry {
    private static final Logger logger = LoggerFactory.getLogger(Registry.class);
    // 用来记录群组插件的所有关键字
    public static volatile Set<String> group_keywords = new HashSet<>();
    // 用来记录好友插件的所有关键字
    public static volatile Set<String> friend_keywords = new HashSet<>();

    private PluginLoader plugin_loader;
    // UUID是给handle使用的，用来表示插件
    // 监听群组关键词与插件id对应
    private Map<String, Set<UUID>> listen_group_words = new HashMap<>();
    // 监听群组id与插件id对应
    private Map<String, Set<UUID>> listen_group_ids = new HashMap<>();
    // 监听好友关键词与插件id对应
    private Map<String, Set<UUID>> listen_friend_words = new HashMap<>();
    // 监听好友id与插件id对应
    private Map<String, Set<UUID>> listen_friend_ids = new HashMap<>();

    private Map<UUID, Object> group_plugin_obj_map;
    private Map<UUID, Object> friend_plugin_obj_map;

    private Set<Object> scheduler_obj;

    public Registry(PluginLoader plugin_loader) {
        this.plugin_loader = plugin_loader;
        this.group_plugin_obj_map = plugin_loader.getGroup_plugin_obj_map();
        this.friend_plugin_obj_map = plugin_loader.getFriend_plugins_obj_map();
        this.scheduler_obj = plugin_loader.getScheduler_obj();
        init();
    }

    // 初始化插件的所有关键词和监听人(群)
    private void init() {
        // 先初始化群组关键词和群组id
        for (UUID uuid : group_plugin_obj_map.keySet()){
            Object o = group_plugin_obj_map.get(uuid);
            Set<String> words = (Set<String>) ReflectionUtils.getField(o, "group_words_set");
            Set<String> groups = (Set<String>) ReflectionUtils.getField(o, "group_ids_set");

            for (String word : words){
                Set<UUID> uuids = listen_group_words.getOrDefault(word,new HashSet<>());
                uuids.add(uuid);
                listen_group_words.put(word, uuids);
                group_keywords.add(word);
            }
            for (String group : groups){
                Set<UUID> uuids = listen_group_ids.getOrDefault(group, new HashSet<>());
                uuids.add(uuid);
                listen_group_ids.put(group, uuids);
            }
        }

        // 初始化好友关键字及好友id
        for (UUID uuid : friend_plugin_obj_map.keySet()){
            Object o = friend_plugin_obj_map.get(uuid);
            Set<String> words = (Set<String>) ReflectionUtils.getField(o, "friend_words_set");
            Set<String> groups = (Set<String>) ReflectionUtils.getField(o, "friend_ids_set");
            for (String word : words){
                Set<UUID> uuids = listen_friend_words.getOrDefault(word,new HashSet<>());
                uuids.add(uuid);
                listen_friend_words.put(word, uuids);
                friend_keywords.add(word);
            }
            for (String group : groups){
                Set<UUID> uuids = listen_group_ids.getOrDefault(group, new HashSet<>());
                uuids.add(uuid);
                listen_friend_ids.put(group, uuids);
            }
        }
    }

    /*
    三个回调函数，把不同类型的消息转发给插件处理
     */
    public int call(GroupMsg msg, UUID uuid){
        if (!group_plugin_obj_map.containsKey(uuid)){
            return -1;
        }
        Object o = group_plugin_obj_map.get(uuid);
        return (int)ReflectionUtils.invoke(o, "callback", msg);
    }

    public int call(FriendMsg msg, UUID uuid){
        if (!friend_plugin_obj_map.containsKey(uuid)){
            return -1;
        }
        Object o = friend_plugin_obj_map.get(uuid);
        return (int)ReflectionUtils.invoke(o, "callback", msg);
    }

    public int call(TempMsg msg, UUID uuid){
        if (!friend_plugin_obj_map.containsKey(uuid)){
            return -1;
        }
        Object o = friend_plugin_obj_map.get(uuid);
        return (int)ReflectionUtils.invoke(o, "callback", msg);
    }

}
