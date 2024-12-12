package com.o2o.util;


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

public class ImageUtil {
    private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);
    private static String resourcesPath;

    public static String getResourcesPath () {
        if (resourcesPath == null) {
            try {
                resourcesPath = ImageUtil.class.getClassLoader().getResource("").getPath();
            } catch (NullPointerException nulle) {
                logger.error(nulle.getMessage());
            }

        }
        return resourcesPath;
    }

    public static String genImgName () {
        String curDate = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(new Date());
        int randomNum = new Random(666).nextInt(10000) + 1;
        return "o2o_" + curDate + "_" + randomNum;
    }

    public static String saveImg (String imgName, Long shopId) {
        String saveBasePath = "D:/javaImages/process" + shopId;
        File processDir = new File(saveBasePath);
        if (!processDir.exists()) {
             if (!processDir.mkdir()) {
                 throw new RuntimeException("文件创建失败");
             }
        }
        return saveBasePath + imgName;
    }

    public static String genImgAndSave (File rawFile, Long shopId) {
        String dest = "";
        try {
            dest = saveImg(genImgName(), shopId);
            Thumbnails.of(rawFile).size(200, 200).watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(getResourcesPath() + "/logo.png")), 0.5f)
                    .outputQuality(0.8).toFile(new File(dest));
        } catch (RuntimeException e) {
            logger.error(e.toString());
        } catch (IOException e) {
            logger.error(e.toString());
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
