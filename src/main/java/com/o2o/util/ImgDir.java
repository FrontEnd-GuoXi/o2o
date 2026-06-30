package com.o2o.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ImgDir {

    public static String imgStorePath;

    public static String onlineImgPrefix;

    public static String userAvatar;



    @Value("${imgStorePath}")
    public void  setImgStore(String imgStorePath) {
        ImgDir.imgStorePath = imgStorePath;
        Path base = Paths.get(imgStorePath);
        Path fragment = Paths.get("userAvatar");
        Path full = base.resolve(fragment);
        ImgDir.userAvatar = full.toString();
    }

    @Value("${onlineImgPrefix}")
    public void setOnlineImgPrefix(String onlineImgPrefix) {
        ImgDir.onlineImgPrefix = onlineImgPrefix;
    }


    private static String getString(String fullPath, String basePath) {
        Path base = Paths.get(basePath);
        Path full = Paths.get(fullPath);
        Path relative = base.relativize(full);
        Path onlineFull = Paths.get("\\").resolve(ImgDir.onlineImgPrefix).resolve(relative);
        return onlineFull.toString();
    }

    public static String getOnlinePath (String fullPath) {
        String basePath = ImgDir.imgStorePath;
        return getString(fullPath, basePath);
    }


    public static String getOnlinePath (String fullPath, String basePath) {
        return getString(fullPath, basePath);
    }
}
