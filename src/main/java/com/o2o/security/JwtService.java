package com.o2o.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.o2o.entity.PersonInfo;
import com.o2o.exceptions.BusinessException;
import com.o2o.util.ReadFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
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
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(byteKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error(e.toString());
            return null;
        }
    }


    public String genToken (long userId) {
        try {
            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
            Date date = new Date();
            long currentTime = date.getTime();
            date.setTime(currentTime + expirationTime);
            return JWT.create()
                    .withIssuer("o2o")
                    .withExpiresAt(date)
                    .withClaim("userId", userId)
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            logger.error(e.toString());
            return null;
        }

    }


    public DecodedJWT validateToken (String token) {
        try {
            DecodedJWT decodedJWT;
            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("o2o")
                    .build();

            decodedJWT = verifier.verify(token);
            return decodedJWT;
        } catch (TokenExpiredException e) {
            logger.error(e.toString());
            throw new BusinessException("Expired：token过期");
        } catch (JWTVerificationException e) {
            logger.error(e.toString());
            throw new BusinessException("Invalid：token校验失败");
        }
    }




}


