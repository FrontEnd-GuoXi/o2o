package com.o2o.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.o2o.dao.UserDao;
import com.o2o.entity.PersonInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class JwtFilter implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDao userDao;


    @Override
    public boolean preHandle (HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return false;
            }

            String token = authHeader.substring(7); // 去掉 "Bearer "
            DecodedJWT decodedJWT = jwtService.validateToken(token);
            int userId = decodedJWT.getClaim("userId").asInt();
            PersonInfo userInfo = userDao.queryUserInfoByUserId(userId);

            return userInfo != null;
        } catch (Exception e) {
            logger.error(e.toString());
            return false;
        }

    }

}
