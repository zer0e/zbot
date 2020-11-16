package com.github.zer0e.zbot.config;

import java.util.Set;

public class Config {

    private Mirai_http_api mirai_http_api;
    private Set<String> plugins;

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

}

