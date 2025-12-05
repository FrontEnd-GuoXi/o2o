package com.o2o.service;

import com.o2o.entity.PersonInfo;
import com.o2o.entity.UserIdentity;

public interface AuthService {

     void registerUser (PersonInfo personInfo, UserIdentity userIdentity);

}
