package com.o2o.util;

public class AboutString {

    public static boolean isEmpty (String str) {
        if (str == null || "".equals(str.trim())) {
            return true;
        } else {
            return false;
        }
    }

}
