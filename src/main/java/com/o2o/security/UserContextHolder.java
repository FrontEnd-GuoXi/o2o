package com.o2o.security;

import com.o2o.dto.PersonInfoDTO;
import com.o2o.enums.HttpApiCode;
import com.o2o.exceptions.BusinessException;

public class UserContextHolder {

    private static final ThreadLocal<PersonInfoDTO> userHolder = new ThreadLocal<>();

    public static  void setUserInfo(PersonInfoDTO personInfoDTO) {
        userHolder.set(personInfoDTO);
    }

    public static PersonInfoDTO getUserInfo () {
        PersonInfoDTO personInfoDTO = userHolder.get();
        if (personInfoDTO == null) {
            throw new BusinessException(HttpApiCode.UNAUTHORIZED, "用户未登录或登录已过期");
        }
        return personInfoDTO;
    }

    public static void clear() {
        userHolder.remove();
    }

}
