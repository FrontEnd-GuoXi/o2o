package com.o2o.service;

import com.o2o.dto.PersonInfoDTO;
import com.o2o.entity.PersonInfo;
import com.o2o.entity.UserIdentity;
import com.o2o.vo.UserInfoVO;

public interface AuthService {

     void registerUser (PersonInfo personInfo, UserIdentity userIdentity);

     String login (String identity, String credential);

     PersonInfoDTO queryUserInfoById (int userId);

     UserInfoVO queryUserInfoVOById(int userId);

}
