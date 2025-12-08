package com.o2o.dao;

import com.o2o.entity.PersonInfo;
import com.o2o.entity.UserIdentity;

public interface UserDao {
    int insertUserInfo (PersonInfo personInfo);

    int insertUserIdentity (UserIdentity userIdentity);

    UserIdentity queryUserIdentityByIdentifier (String identifier);

    PersonInfo queryUserInfoByUserId (int userId);

}
