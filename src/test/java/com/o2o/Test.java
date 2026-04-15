package com.o2o;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        try {
            List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C"));
            list.forEach(item -> {
                if (item.equals("B")) {
                    throw new RuntimeException("B点报错");
                }
                System.out.println("输出"+ item);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
