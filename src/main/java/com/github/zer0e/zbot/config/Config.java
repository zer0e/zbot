package com.github.zer0e.zbot.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Config {

    private Mirai_http_api mirai_http_api = new Mirai_http_api();
    private Set<String> plugins = new HashSet<>();

    public Set<String> getPlugins() {
        return plugins;
    }

    public void setPlugins(Set<String> plugins) {
        this.plugins = plugins;
    }

    public void setMirai_http_api(Mirai_http_api mirai_http_api) {
        this.mirai_http_api = mirai_http_api;
    }

    public Mirai_http_api getMirai_http_api() {
        return mirai_http_api;
    }

    public Object toMap() {
        Map map = new HashMap<String,Object>();
        Map mirai_http_api_map = new HashMap<String,String>();
        mirai_http_api_map.put("host",this.getMirai_http_api().getHost());
        mirai_http_api_map.put("port",this.getMirai_http_api().getPort());
        mirai_http_api_map.put("authKey",this.getMirai_http_api().getAuthKey());
        mirai_http_api_map.put("bot_qq",this.getMirai_http_api().getBot_qq());
        map.put("mirai_http_api",mirai_http_api_map);
        Set<String> plugins = new HashSet<>();
        plugins.add("HelpPlugin");
        map.put("plugins",plugins);
        return map;
    }
}

