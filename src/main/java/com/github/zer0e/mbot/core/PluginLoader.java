package com.github.zer0e.mbot.core;

import com.github.zer0e.mbot.config.Config;
import com.github.zer0e.mbot.plugins.FriendPlugin;
import com.github.zer0e.mbot.plugins.GroupPlugin;
import com.github.zer0e.mbot.utils.ConfigUtil;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Getter
public class PluginLoader {
    private static final Logger logger = LoggerFactory.getLogger(PluginLoader.class);

    private final String plugin_dir = "com.github.zer0e.mbot.plugins.";
    private final Config config = ConfigUtil.getConfig();
    // 从设置中读取的插件列表
    private final Set<String> plugins_from_settings = config.getPlugins();
    // 有效的群插件列表 暂时无用处先保留
    private Set<String> group_plugins = new HashSet<>();
    // 有效的好友插件列表
    private Set<String> friend_plugins = new HashSet<>();
    // 插件ID与插件实例对应
    private Map<UUID,Object> group_plugin_obj_map = new HashMap<>();
    private Map<UUID,Object> friend_plugins_obj_map = new HashMap<>();

    public PluginLoader() {
        load_plugins();
    }

    public void load_plugins(){
        for (String s : this.plugins_from_settings){
            check_plugins(s);
        }
    }

    private void check_plugins(String plugin_name){
        String plugin_uri = this.plugin_dir + plugin_name ;
        try {
            Class c = Class.forName(plugin_uri);
            Object o = c.newInstance();
            if (o instanceof GroupPlugin){
                if (check_group_plugins((GroupPlugin) o)){
                    group_plugins.add(plugin_name);
                }
            }
            if (o instanceof FriendPlugin){
                if (check_friend_plugin((FriendPlugin) o)){
                    friend_plugins.add(plugin_name);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("类不存在");
        }
    }

    private boolean check_group_plugins(GroupPlugin o){
        if (o.group_ids_set.isEmpty() || o.group_words_set.isEmpty()){
            logger.error(o.getClass().getName() + " 插件关键词不合法");
            return false;
        }
        UUID uuid = UUID.randomUUID();
        group_plugin_obj_map.put(uuid, o);
        logger.info("注册群插件: " + o.getClass().getName() + " 插件id：" + uuid);
        return true;
    }
    private boolean check_friend_plugin(FriendPlugin o){
        if (o.friend_words_set.isEmpty() || o.friend_ids_set.isEmpty()){
            logger.error(o.getClass().getName() + " 插件关键词不合法");
            return false;
        }
        UUID uuid = UUID.randomUUID();
        friend_plugins_obj_map.put(uuid, o);
        logger.info("注册好友插件: " + o.getClass().getName() + " 插件id：" + uuid);
        return true;
    }

}
