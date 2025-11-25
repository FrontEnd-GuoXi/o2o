package com.o2o.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.o2o.util.ReadFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(ReadFile.class);
    private static final long expirationTime = 60 * 60 * 1000;
    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;


    JwtService(@Value("${jwt.private-key-path}") String privateKeyPath,
               @Value("${jwt.public-key-path}") String publicKeyPath) {

        String privateKeyStr = ReadFile.readContentOfFile(privateKeyPath);
        String publicKeyStr = ReadFile.readContentOfFile(publicKeyPath);

        privateKey = parsePrivateKey(privateKeyStr);
        publicKey = parsePublicKey(publicKeyStr);

    }

    private RSAPrivateKey parsePrivateKey(String keyStr) {
        try {
            keyStr = keyStr.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] byteKey = Base64.getDecoder().decode(keyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(byteKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error(e.toString());
            return null;
        }
    }

    private RSAPublicKey parsePublicKey(String keyStr) {
        try {
            keyStr = keyStr.replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] byteKey = Base64.getDecoder().decode(keyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(byteKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error(e.toString());
            return null;
        }
    }


    public String genToken () {
        Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
        String token = JWT.create()
    }

}


