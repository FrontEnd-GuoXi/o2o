package com.o2o.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.o2o.exceptions.BusinessException;
import com.o2o.dto.PersonInfoDTO;
import com.o2o.enums.HttpApiCode;
import com.o2o.service.AuthService;
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
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    private JwtService jwtService;


    @Autowired
    private AuthService authService;




    @Override
    public boolean preHandle (HttpServletRequest request, HttpServletResponse response, Object handler) {
        String method = request.getMethod();
        logger.info("contextPath={}, servletPath={}, requestURI={}",
        request.getContextPath(),
        request.getServletPath(),
        request.getRequestURI());

        // 1. 放行 OPTIONS 请求 (CORS 预检)
        if ("OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }


        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("认证失败: 缺少或非法的Authorization请求头");
            writeResponse(response, HttpApiCode.INVALID_TOKEN, HttpApiCode.INVALID_TOKEN.getMsg());
            return false;
        }

        try {
            String token = authHeader.substring(7); // 去掉 "Bearer "
            DecodedJWT decodedJWT = jwtService.validateToken(token);
            int userId = decodedJWT.getClaim("userId").asInt();
            PersonInfoDTO personInfoDTO = authService.queryUserInfoById(userId);
            UserContextHolder.setUserInfo(personInfoDTO);
            return true;
        } catch (BusinessException e) {
            HttpApiCode apiCode = e.getApiCode() == null ? HttpApiCode.UNAUTHORIZED : e.getApiCode();
            logger.warn("认证失败: {}", e.getMessage());
            writeResponse(response, apiCode, e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("认证链路异常", e);
            writeResponse(response, HttpApiCode.UNAUTHORIZED, HttpApiCode.UNAUTHORIZED.getMsg());
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
    private void writeResponse(HttpServletResponse response, HttpApiCode code, String msg)  {
        try {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(resolveHttpStatus(code));
            ResponseResultWrap<Object> result = ResponseResultWrap.fail(code, msg);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(response.getWriter(), result);
        } catch (IOException e) {
            logger.error("认证响应写回失败", e);
        }
    }

    private int resolveHttpStatus(HttpApiCode code) {
        switch (code) {
            case TOKEN_EXPIRED:
            case INVALID_TOKEN:
            case UNAUTHORIZED:
                return HttpServletResponse.SC_UNAUTHORIZED;
            case NOT_FOUND_USER:
                return HttpServletResponse.SC_NOT_FOUND;
            default:
                return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }
    }

}
