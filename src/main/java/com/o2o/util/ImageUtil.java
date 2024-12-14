package com.o2o.util;


import com.o2o.exceptions.ShopOperationException;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.nio.file.Paths;

public class ImageUtil {
    private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);
    private static  String resourcesPath = getResourcesPath();
    // private static final String resourcesPath = Paths.get("").toAbsolutePath().toString();



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

    public static String saveImg (String imgName, Long shopId) {
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
        try {
            dest = saveImg(genImgName(rawFile), shopId);
            Thumbnails.of(rawFile).size(200, 200).watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(resourcesPath + "/logo.png")), 0.5f)
                    .outputQuality(0.8).toFile(new File(dest));
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

    /**
     *
     * 加载需要打水印的图片，然后设置大小，再设置水印的位置
     * 水印的路径需要通过线程类去获取，合成之后就需要保存到特定的位置
     *
     * **/

//    public static void main (String[] args) {
//
//        try {
//            Thumbnails.of(new File("D:/javaImages/raw/1.jpeg")).size(800, 800)
//                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(getResourcesPath() + "/logo.png")), 0.5f)
//                    .outputQuality(0.8)
//                    .toFile(new File(savePath(genImgName())));
//        } catch (RuntimeException rte) {
//            logger.error(rte.getMessage());
//        } catch (IOException ioe) {
//            logger.error(ioe.getMessage());
//        }
//
//
//
//    }
}
