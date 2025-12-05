package com.o2o.service.impl;

import com.o2o.dao.UserDao;
import com.o2o.entity.PersonInfo;
import com.o2o.entity.UserIdentity;
import com.o2o.exceptions.BusinessException;
import com.o2o.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private  UserDao userDao;

    @Transactional
    public void registerUser (PersonInfo personInfo, UserIdentity userIdentity) {
        try {
            int effectedRowOfInfo = userDao.insertUserInfo(personInfo);

            if (effectedRowOfInfo <= 0) {
                throw new BusinessException("创建用户基本信息失败");
            }

            Long userId = personInfo.getUserId();
            if (userId == null) {
                throw new BusinessException("用户ID未生成，请检查数据库自增配置");
            }

            userIdentity.setUserId(userId);
            int effectedRowOfIdentity = userDao.insertUserIdentity(userIdentity);

            if (effectedRowOfIdentity <= 0) {
                throw new BusinessException("创建用户身份信息失败");
            }

        } catch (BusinessException e) {
            // 业务异常：记录日志并重新抛出（保持语义）
            logger.warn("用户注册业务失败: {}", e.getMessage());
            throw e; // 重新抛出，不包装
        } catch (Exception e) {
            logger.error(e.toString());
            throw new BusinessException("注册服务暂时不可用，请稍后再试"); // 脱敏后抛出
        }
    }



}
