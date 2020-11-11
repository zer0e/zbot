package com.github.zer0e.mbot.core;

import com.github.zer0e.mbot.model.Msg;
import com.github.zer0e.mbot.utils.ReflectionUtils;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

@Getter
public class Registry {
    private PluginLoader plugin_loader;
    private static Logger logger = LoggerFactory.getLogger(Registry.class);
    // 监听词与插件id对应
    private Map<String, Set<UUID>> listen_words = new HashMap<>();
    //监听群组与插件id对应
    private Map<String, Set<UUID>> listen_groups = new HashMap<>();
    private Map<UUID, Object> plugin_obj_map;
    public static volatile Set<String> keywords = new HashSet<>();


    public Registry(PluginLoader plugin_loader) {
        this.plugin_loader = plugin_loader;
        plugin_obj_map = plugin_loader.getPlugin_obj_map();
        init();
    }

    private void init(){
        for (UUID uuid : plugin_obj_map.keySet()){
            Object o = plugin_obj_map.get(uuid);
            Set<String> words = (Set<String>)ReflectionUtils.invoke(o,"getWords");
            Set<String> groups = (Set<String>)ReflectionUtils.invoke(o, "getGroups");
            for (String word : words){
                Set<UUID> uuids = listen_words.getOrDefault(word,new HashSet<>());
                uuids.add(uuid);
                listen_words.put(word, uuids);
                keywords.add(word);
            }

            for (String group : groups){
                Set<UUID> uuids = listen_groups.getOrDefault(group, new HashSet<>());
                uuids.add(uuid);
                listen_groups.put(group, uuids);
            }
        }
    }
    /*
    回调函数，把消息转发给插件处理
     */
    public int call(Msg msg, UUID uuid){
        if (!plugin_obj_map.containsKey(uuid)){
            return -1;
        }else{
            Object o = plugin_obj_map.get(uuid);
            return (int)ReflectionUtils.invoke(plugin_obj_map.get(uuid), "callback", msg);
        }
    }
}
