package com.o2o.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.o2o.dao.UserDao;
import com.o2o.dto.PersonInfoDTO;
import com.o2o.entity.PersonInfo;
import com.o2o.enums.HttpApiCode;
import com.o2o.util.ResponseResultWrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


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
                writeResponse(response, HttpApiCode.INVALID_TOKEN);
                return false;
            }

            String token = authHeader.substring(7); // 去掉 "Bearer "
            DecodedJWT decodedJWT = jwtService.validateToken(token);
            int userId = decodedJWT.getClaim("userId").asInt();
            PersonInfo userInfo = userDao.queryUserInfoByUserId(userId);



            if (userInfo != null) {
                PersonInfoDTO personInfoDTO = new PersonInfoDTO();
                personInfoDTO.setUserId(userInfo.getUserId());
                personInfoDTO.setGender(userInfo.getGender());
                personInfoDTO.setEnableStatus(userInfo.getEnableStatus());
                personInfoDTO.setUserType(userInfo.getUserType());
                UserContextHolder.setUserInfo(personInfoDTO);
                return true;
            } else {
                writeResponse(response, HttpApiCode.NOT_FOUND_USER);
                return false;
            }
        } catch (Exception e) {
            logger.error(e.toString());
            // 区分不同异常类型（可选）
            if (e.getMessage().contains("Expired")) {
                writeResponse(response, HttpApiCode.TOKEN_EXPIRED);
            } else if (e.getMessage().contains("Invalid")) {
                writeResponse(response, HttpApiCode.INVALID_TOKEN);
            } else {
                writeResponse(response, HttpApiCode.UNAUTHORIZED);
            }
            return false;
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        UserContextHolder.clear();
    }

    /**
     * 向响应中写入 JSON 格式的错误信息
     */
    private void writeResponse(HttpServletResponse response, HttpApiCode code)  {
        try {
            response.setContentType("application/json;charset=UTF-8");
            ResponseResultWrap<Object> result =  ResponseResultWrap.getResultByHttpCode(code, null);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(response.getWriter(), result);
        } catch (IOException e) {
            logger.error(e.toString());
        }

    }

}
