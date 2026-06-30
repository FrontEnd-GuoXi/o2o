package com.o2o;



import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {

    public static void main(String[] args) {
            Path fullPath = Paths.get("D:/shop/2/def.png");
            Path basePath = Paths.get("D:/shop/");

            Path relPath = basePath.relativize(fullPath);
            Path onlinePath = Paths.get(".").resolve("\\shop").resolve(relPath);
            System.out.println("onlinePath2对象结果: " + onlinePath);
    }

}
