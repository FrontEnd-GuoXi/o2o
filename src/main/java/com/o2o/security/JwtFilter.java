package com.o2o.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.o2o.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class JwtFilter implements HandlerInterceptor {

    @Autowired
    JwtService jwtService;

    @Autowired
    UserDao userDao;


    @Override
    public Boolean preHandle (HttpServletRequest request, HttpServletResponse response, Object handler) {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return false;
            }

            String token = authHeader.substring(7); // 去掉 "Bearer "
            DecodedJWT decodedJWT = jwtService.validateToken(token);
            String userId = String.valueOf(decodedJWT.getClaim("userId"));


    }

}
