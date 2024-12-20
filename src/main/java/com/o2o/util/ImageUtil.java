package com.o2o.util;


import com.o2o.exceptions.ShopOperationException;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class ImageUtil {
    private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);
    private static  String resourcesPath = getResourcesPath();
    private static String temporaryImgDir;


    public static File MultipartFileToFile(CommonsMultipartFile multiFile) {



        try {

            String saveBasePath = "D:/javaImages/temporary/" + genImgName();
            InputStream inputStream =  multiFile.getInputStream();
            OutputStream outputStream = new FileOutputStream(new File(""));
            byte[] buffer = new byte[1024];
            int byteNum;



            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public static String getResourcesPath () {
        if (resourcesPath == null) {
            resourcesPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            if (resourcesPath.startsWith("/")) {
                resourcesPath = resourcesPath.substring(1);
            }
        }
        return resourcesPath;
    }

    public static String genImgName (File img) {
        String fileName = img.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String curDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        int randomNum = new Random(666).nextInt(10000) + 1;
        return "o2o_" + curDate + "_" + randomNum + "." + suffix;
    }

    public static String genImgAddr (String imgName, Long shopId) {
        try{
            String saveBasePath = "D:/javaImages/process/" + shopId;
            File processDir = new File(saveBasePath);
            if (!processDir.exists()) {
                if (!processDir.mkdir()) {
                    throw new ShopOperationException("图片存储路径创建失败");
                }
            }
            return saveBasePath + "/" + imgName;
        } catch (RuntimeException e) {
            throw new ShopOperationException("图片存储路径创建失败");
        }

    }

    public static String genImgAndSave (File rawFile, Long shopId) {
        String dest = "";
        String logoDir = resourcesPath + "logo.png";
        try {

            dest = genImgAddr(genImgName(rawFile), shopId);
            Thumbnails.of(rawFile)
                    .size(500, 500)
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(logoDir)), 0.8f)
                    .outputQuality(0.8)
                    .toFile(new File(dest));
            logger.debug("resourcesPath----" + resourcesPath);
            logger.debug("dest----" + dest);
            logger.debug("logoDir----" + logoDir);
        } catch (IOException e) {
            logger.error(e.toString() + "path:" + dest);
            logger.debug("resourcesPath----" + resourcesPath);
            logger.debug("dest----" + dest);
            throw new ShopOperationException("图片读取或生成失败");
        } catch (RuntimeException e) {
            logger.error(e.toString() + "path:" + dest);
            logger.debug("resourcesPath-----" + resourcesPath);
            logger.debug("dest----" + dest);
            throw new ShopOperationException("图片写入失败");
        }

        return dest;
    }

}
