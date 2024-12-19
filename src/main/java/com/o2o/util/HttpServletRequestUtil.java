package com.o2o.util;

import com.o2o.web.shopadmin.ShopManagementController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestUtil {
    private static final Logger logger = LoggerFactory.getLogger(ShopManagementController.class);


    public static Integer getInt (HttpServletRequest request, String key) {
        try {
            String numStr = request.getParameter(key);
            return Integer.decode(numStr);
        } catch (Exception e) {
            logger.error(e.toString());
            return -1;
        }
    }


    public static String getString (HttpServletRequest request, String key) {
        try{
            String result = request.getParameter(key);
            if (result != null) {
                if (result.equals("")) {
                    result = null;
                } else {
                    result = result.trim();
                }
            }
            return result;
        } catch (Exception e) {
            logger.error(e.toString());
            return null;
        }
    }


}
