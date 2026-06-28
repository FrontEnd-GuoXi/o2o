package com.o2o.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.o2o.enums.HttpApiCode;
import com.o2o.exceptions.BusinessException;
import com.o2o.util.ReadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtService {
    private static final long EXPIRATION_TIME = 60 * 60 * 1000;
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
            if (keyStr == null || keyStr.trim().isEmpty()) {
                throw new BusinessException("JWT私钥内容为空");
            }
            keyStr = keyStr.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] byteKey = Base64.getDecoder().decode(keyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(byteKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (BusinessException e) {
            throw e;
        } catch (IllegalArgumentException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new BusinessException("JWT私钥解析失败", e);
        }
    }

    private RSAPublicKey parsePublicKey(String keyStr) {
        try {
            if (keyStr == null || keyStr.trim().isEmpty()) {
                throw new BusinessException("JWT公钥内容为空");
            }
            keyStr = keyStr.replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] byteKey = Base64.getDecoder().decode(keyStr);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(byteKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (BusinessException e) {
            throw e;
        } catch (IllegalArgumentException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new BusinessException("JWT公钥解析失败", e);
        }
    }


    public String genToken (long userId) {
        try {
            Algorithm algorithm = createAlgorithm();
            Date date = new Date();
            long currentTime = date.getTime();
            date.setTime(currentTime + EXPIRATION_TIME);
            return JWT.create()
                    .withIssuer("o2o")
                    .withExpiresAt(date)
                    .withClaim("userId", userId)
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new BusinessException("token生成失败", e);
        }

    }


    public DecodedJWT validateToken (String token) {
        try {
            Algorithm algorithm = createAlgorithm();
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("o2o")
                    .build();
            return verifier.verify(token);
        } catch (TokenExpiredException e) {
            throw new BusinessException(HttpApiCode.TOKEN_EXPIRED, "token过期");
        } catch (JWTVerificationException e) {
            throw new BusinessException(HttpApiCode.INVALID_TOKEN, "token校验失败");
        }
    }

    private Algorithm createAlgorithm() {
        try {
            return Algorithm.RSA256(publicKey, privateKey);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("JWT算法初始化失败", e);
        }
    }




}


