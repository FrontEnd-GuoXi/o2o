package com.o2o.security;

import com.o2o.dto.PersonInfoDTO;

public class UserContextHolder {

    private static final ThreadLocal<PersonInfoDTO> userHolder = new ThreadLocal<>();

    public static  void setUserInfo(PersonInfoDTO personInfoDTO) {
        userHolder.set(personInfoDTO);
    }

    public static PersonInfoDTO getUserInfo () {
        return userHolder.get();
    }

    public static void clear() {
        userHolder.remove();
    }

}
