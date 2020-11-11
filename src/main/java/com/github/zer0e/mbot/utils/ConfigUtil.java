package com.github.zer0e.mbot.utils;

import com.github.zer0e.mbot.config.Config;
import org.yaml.snakeyaml.Yaml;



public class ConfigUtil {
    private static final String config_path = "/config.yaml";
    private static final Config config = loadConfig();
    public static Config loadConfig(){
        Yaml yaml = new Yaml();
        return yaml.loadAs(ConfigUtil.class.getResourceAsStream(config_path), Config.class);
    }

    public static Config getConfig() {
        return config;
    }
}
