package com.o2o.util;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.interfaces.RSAPrivateKey;

public class PemKeyLoader {


    public static RSAPrivateKey privateKeyLoader (String keyPath) {

            String content = Files.readString(Paths.get(keyPath));

    }


}
