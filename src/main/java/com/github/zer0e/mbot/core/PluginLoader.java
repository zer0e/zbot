package com.github.zer0e.mbot.core;

import com.github.zer0e.mbot.config.Config;
import com.github.zer0e.mbot.plugins.BasePlugin;
import com.github.zer0e.mbot.utils.ConfigUtil;
import com.github.zer0e.mbot.utils.ReflectionUtils;
import lombok.Getter;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.Map;

@Getter
public class PluginLoader {
    private Config config = ConfigUtil.getConfig();
    // 从设置中读取的插件列表
    private Set<String> plugins = config.getPlugins();
    // 有效的插件列表
    private Set<String> real_plugins = new HashSet<>();
    // 有效的插件实例
    private Set<Object> object_set = new HashSet<>();
    // 插件ID与插件实例对应
    private Map<UUID,Object> plugin_obj_map = new HashMap<>();

    private String plugin_dir = "com.github.zer0e.mbot.plugins.";

    private static Logger logger = LoggerFactory.getLogger(PluginLoader.class);

    public PluginLoader() {
        load_plugins();
    }

    public void load_plugins(){
        for (String s : this.plugins){
            if (check_plugin(s)){
                real_plugins.add(s);
            }
        }
    }

    private boolean check_plugin(String s){
        String plugin_uri = this.plugin_dir + s ;
        try {
            Class c = Class.forName(plugin_uri);
            Object o = c.newInstance();
            Set<String> words = (Set<String>)ReflectionUtils.invoke(o,"getWords");
            Set<String> groups = (Set<String>)ReflectionUtils.invoke(o,"getWords");
            if (words.size() == 0 || groups.size() == 0){
                return false;
            }
            UUID uuid = UUID.randomUUID();
            object_set.add(o);
            plugin_obj_map.put(uuid, o);
            logger.info("注册插件：" + s + " 插件ID: " + uuid.toString());
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
