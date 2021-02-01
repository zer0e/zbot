package com.github.zer0e.zbot.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {
    public static String bytes_to_tmp_file(byte[] bytes){
        return bytes_to_tmp_file(bytes, "tmp000");
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
        if (os.indexOf("windows") != 0){
            tmp_dir.append("\\tmp\\");
        }else if (os.indexOf("linux") != 0){
            tmp_dir.append("/tmp/");
        }else {
            return "/tmp/";
        }
        return project_dir + tmp_dir;
    }
}
