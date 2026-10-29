package com.o2o.service.impl;

import com.o2o.dao.UserDao;
import com.o2o.dto.PersonInfoDTO;
import com.o2o.entity.PersonInfo;
import com.o2o.entity.UserIdentity;
import com.o2o.enums.HttpApiCode;
import com.o2o.enums.PlatformTypeEnum;
import com.o2o.enums.UserTypeEnum;
import com.o2o.exceptions.BusinessException;
import com.o2o.security.JwtService;
import com.o2o.service.AuthService;
import com.o2o.vo.UserInfoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private  UserDao userDao;

    @Autowired
    private JwtService jwtService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);

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

            int identifierCount = userDao.countByIdentifier(userIdentity.getIdentifier());
            if (identifierCount > 0) {
                throw new BusinessException("账号已存在");
            }


            // 密码明文编码
            String pwd = encoder.encode(userIdentity.getCredential());
            userIdentity.setCredential(pwd);
            int effectedRowOfIdentity = userDao.insertUserIdentity(userIdentity);

            if (effectedRowOfIdentity <= 0) {
                throw new BusinessException("创建用户身份信息失败");
            }

        } catch (DuplicateKeyException e) {
            logger.error(e.toString());
            throw new BusinessException("账号已存在"); // 脱敏后抛出
        } catch (BusinessException e) {
            // 业务异常：记录日志并重新抛出（保持语义）
            logger.warn("用户注册业务失败: {}", e.getMessage());
            throw e; // 重新抛出，不包装
        } catch (Exception e) {
            logger.error(e.toString());
            throw new BusinessException("注册服务暂时不可用，请稍后再试"); // 脱敏后抛出
        }
    }

    public String login (String identity, String credential, String platform) {
        try {
            UserIdentity userIdentity = userDao.queryUserIdentityByIdentifier(identity);
            if (userIdentity == null) {
                throw new BusinessException(HttpApiCode.UNAUTHORIZED, "密码或账号错误");
            }
            boolean pass = encoder.matches(credential, userIdentity.getCredential());

            if (!pass) {
                throw new BusinessException(HttpApiCode.UNAUTHORIZED, "密码或账号错误");
            }

            PersonInfo userInfo = userDao.queryUserInfoByUserId(userIdentity.getUserId().intValue());
            Integer userType = userInfo.getUserType();
            PlatformTypeEnum platformType = PlatformTypeEnum.fromCode(platform);
            if (platformType == null) {
                throw new BusinessException("不存在该平台参数！");
            }

            UserTypeEnum userTypeEnum = UserTypeEnum.fromCode(userType);
            switch (platformType) {
                case CONSUMER:
                    if (userTypeEnum != UserTypeEnum.CUSTOMER && userTypeEnum != UserTypeEnum.SHOP_OWNER) {
                        throw new BusinessException("该用户不是消费者或商家！");
                    }
                    break;
                case MANAGER:
                    if (userTypeEnum != UserTypeEnum.SHOP_OWNER && userTypeEnum != UserTypeEnum.SUPER_ADMIN) {
                        throw new BusinessException("该用户不是管理员或商家！");
                    }
                    break;
                default:
                    throw new BusinessException("不存在该平台参数！");
            }


            String token = jwtService.genToken(userIdentity.getUserId());
            if (token == null) {
                throw new BusinessException("登录令牌生成失败");
            }
            return token;
        } catch (BusinessException e) {
            logger.warn("登录失败：{}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error(e.toString());
            throw new BusinessException("登录服务异常");
        }

    }

    public PersonInfoDTO queryUserInfoById (int userId) {
        try {
            PersonInfo userInfo = userDao.queryUserInfoByUserId(userId);
            if (userInfo == null) {
                throw new BusinessException(HttpApiCode.NOT_FOUND_USER, "用户不存在");
            }
            PersonInfoDTO personInfoDTO = new PersonInfoDTO();
            personInfoDTO.setUserId(userInfo.getUserId());
            personInfoDTO.setGender(userInfo.getGender());
            personInfoDTO.setEnableStatus(userInfo.getEnableStatus());
            personInfoDTO.setUserType(userInfo.getUserType());
            return personInfoDTO;
        } catch (BusinessException e) {
            logger.warn("查询用户信息失败：{}", e.toString());
            throw e;
        } catch (Exception e) {
            logger.error(e.toString());
            throw new BusinessException("查询用户信息失败");
        }
    }

    public UserInfoVO queryUserInfoVOById (int userId) {
        try {
            PersonInfo userInfo = userDao.queryUserInfoByUserId(userId);
            if (userInfo == null) {
                throw new BusinessException(HttpApiCode.NOT_FOUND_USER, "用户不存在");
            }
            UserInfoVO userInfoVO = new UserInfoVO();
            BeanUtils.copyProperties(userInfo, userInfoVO);

            userInfoVO.setUserId(String.valueOf(userInfo.getUserId()));
            userInfoVO.setUserTypeName(UserTypeEnum.fromCode(userInfo.getUserType()).getDesc());
            return userInfoVO;
        } catch (BusinessException e) {
            logger.warn("查询用户信息VO失败：{}", e.toString());
            throw e;
        } catch (Exception e) {
            logger.error(e.toString());
            throw new BusinessException("查询用户信息VO失败");
        }
    }









}
