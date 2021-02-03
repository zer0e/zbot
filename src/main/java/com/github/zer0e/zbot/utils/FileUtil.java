package com.github.zer0e.zbot.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
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
        return System.getProperty("os.name");
    }


    public static String get_tmp_dir(){
        String os = get_os();
        String project_dir = get_project_dir();
        StringBuilder tmp_dir = new StringBuilder();
        if (os.contains("windows")){
            tmp_dir.append("\\tmp\\");
        }else {
            tmp_dir.append("/tmp/");
        }
        return project_dir + tmp_dir;
    }
}
