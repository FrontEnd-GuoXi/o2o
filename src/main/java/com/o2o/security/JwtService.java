package com.o2o.security;

import org.springframework.beans.factory.annotation.Value;

public class JwtService {

    private static final long expirationTime =  60 * 60 * 1000;
    private final String privateKey;
    private final String publicKey;


    JwtService(@Value("${jwt.private-key-path}") String privateKeyPath,
            @Value("${jwt.public-key-path}") String publicKeyPath) {


    }
}


