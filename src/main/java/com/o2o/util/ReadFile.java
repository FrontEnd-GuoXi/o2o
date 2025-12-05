package com.o2o.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadFile {
    private static final Logger logger = LoggerFactory.getLogger(ReadFile.class);

    public static String readContentOfFile(String filePath) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            StringBuffer content = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }
            return content.toString();
        } catch (IOException e) {
            logger.error(e.toString());
            return "";
        }

    }

}
