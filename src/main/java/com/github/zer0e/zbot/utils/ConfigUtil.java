package com.github.zer0e.zbot.utils;

import com.github.zer0e.zbot.config.Config;
import com.github.zer0e.zbot.config.Mirai_http_api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.*;

/**
 * @Author zer0e
 * @Description
 * @Date 2021-03-07 22:41
 **/
public class ConfigUtil {
    private static final Logger logger = LoggerFactory.getLogger(ConfigUtil.class);
    // FileUtil保证路径存在
    private static final String config_path = FileUtil.get_config_dir() + "/" + FileUtil.CONFIG_FILE_NAME;
    private static Config config;
    public static Config loadConfig(){
        Yaml yaml = new Yaml();
        try {
            config =  yaml.loadAs(new FileInputStream(config_path), Config.class);
        }catch (IOException e) {
            logger.error("配置文件错误或不存在，将自动生成或关闭程序手动生成！");
            config = new Config();
            auto_generate_config_file(config);
        }
        return config;
    }

    public static Config getConfig() {
        if (config != null)
            return config;
        else{
            loadConfig();
        }
        return config;
    }

    private static void auto_generate_config_file(Config config){
        Mirai_http_api mirai_http_api = new Mirai_http_api();
        mirai_http_api.setAuthKey("INITKEY5SjjHI3m");
        mirai_http_api.setBot_qq("123456");
        mirai_http_api.setHost("localhost");
        mirai_http_api.setPort("8080");

        config.setMirai_http_api(mirai_http_api);
        try{
            new Yaml().dump(config.toMap(),new FileWriter(config_path));
            logger.info("成功生成默认配置文件");
        }catch (IOException e) {
            logger.error("写入配置文件失败");
        }

    }
}
