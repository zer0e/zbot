package com.github.zer0e.zbot.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
    public static final String TMP_DIR = "tmp";
    public static final String CONFIG_FILE_NAME = "config.yaml";
    public static final String CONFIG_DIR = "config";
    public static final String PLUGIN_DIR = "plugin";
    public static String bytes_to_tmp_file(byte[] bytes){
        String filename = UUID.randomUUID().toString();
        return bytes_to_tmp_file(bytes, filename);
    }
    public static String bytes_to_tmp_file(byte[] bytes, String filename){
        String tmp_dir = get_tmp_dir();
        String full_path = tmp_dir + filename;
        File file = new File(full_path);
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        FileOutputStream fos = null;
        try {
            file.createNewFile();
            fos = new FileOutputStream(file);
            fos.write(bytes);
            logger.info("文件存放于： " + full_path);
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }finally {
            if (fos != null){
                try {
                    fos.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return full_path;
    }
    public static boolean delete_tmp_dir(){
        String tmp_dir = get_tmp_dir();
        File tmp_file_dir = new File(tmp_dir);
        for (File file : tmp_file_dir.listFiles()){
            if (file != null){
                delete_tmp_file(file);
            }
        }
        return tmp_file_dir.delete();
    }
    public static void delete_tmp_file(File file){
        if (file.isFile()){
            file.delete();
        }else{
            for (File tmp_file : file.listFiles()){
                delete_tmp_file(tmp_file);
            }
        }
    }

    public static String get_project_dir(){
        return System.getProperty("user.dir");
    }

    public static String get_os(){
        return System.getProperty("os.name").toLowerCase();
    }



    public static String get_dir(String dir_name) {
        String os = get_os();
        String project_dir = get_project_dir();
        StringBuilder dir = new StringBuilder();
        if (os.contains("windows")){
            dir.append("\\" + dir_name + "\\");
        }else {
            dir.append("/" + dir_name + "/");
        }
        // 自动创建文件夹
        File file = new File(project_dir + dir);
        if (!file.exists()){
            file.mkdirs();
        }
        return project_dir + dir;
    }
    /**
     * @Author zer0e
     * @Description 返回临时文件夹的绝对路径，如不存在文件夹则会创建，下同
     * @Date 2021-03-07 21:23
     * @Param
     * @Return java.lang.String
     * @Throws
     **/
    public static String get_tmp_dir(){
        return get_dir(TMP_DIR);
    }

    public static String get_config_dir(){
        return get_dir(CONFIG_DIR);
    }

    public static String get_plugin_dir(){
        return get_dir(PLUGIN_DIR);
    }

    /**
     * @Author zer0e
     * @Description 创建bot所需的文件夹，bot初始化时调用
     * @Date 2021-03-07 21:22
     * @Param
     * @Return boolean
     * @Throws
     **/
    public static void create_base_dir(){
        String tmp_dir = get_tmp_dir();
        String config_dir = get_config_dir();
        String plugin_dir = get_plugin_dir();
        List<String> dirs = new ArrayList<>();
        dirs.add(tmp_dir);
        dirs.add(config_dir);
        dirs.add(plugin_dir);
        for (String dir : dirs){
            File file = new File(dir);
            if (!file.exists()){
                if (!file.mkdirs()){
                    logger.error("创建文件夹：" + dir + " 时失败");
                }else{
                    logger.info("创建文件夹："+ dir);
                }
            }
        }
    }

    public static boolean exists_config_file(){
        String config_file_path = get_config_dir() + "/" + CONFIG_FILE_NAME;
        File file = new File(config_file_path);
        return file.exists();
    }
}
