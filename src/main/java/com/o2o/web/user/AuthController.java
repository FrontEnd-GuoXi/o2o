package com.o2o.web.user;

import com.o2o.dto.LoginRequestDTO;
import com.o2o.dto.PersonInfoDTO;
import com.o2o.dto.RegisterRequestDTO;
import com.o2o.entity.PersonInfo;
import com.o2o.entity.UserIdentity;
import com.o2o.exceptions.BusinessException;
import com.o2o.security.UserContextHolder;
import com.o2o.service.AuthService;
import com.o2o.util.ResponseResultWrap;
import com.o2o.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Date;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @Value("${imgStore}")
    private String imgStore;

    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseResultWrap<Object> userRegister(@Valid @RequestPart("data") RegisterRequestDTO registerRequestDTO,
                                                   BindingResult result,
                                                   @RequestPart(value = "profileImg", required = false) MultipartFile profileImg) {
        if (result.hasErrors()) {
            String errStr = result
                    .getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + "：" + error.getDefaultMessage())
                    .collect(Collectors.joining());
            throw new BusinessException(errStr);
        }

        PersonInfo personInfo = new PersonInfo();
        personInfo.setName(registerRequestDTO.getName());
        personInfo.setGender(registerRequestDTO.getGender());
        personInfo.setUserType(registerRequestDTO.getUserType());
        personInfo.setEnableStatus(0);
        personInfo.setCreateTime(new Date());
        personInfo.setLastEditTime(new Date());

        if(profileImg != null && !profileImg.isEmpty()) {
            String uploadFolder = 
        }

        UserIdentity userIdentity = new UserIdentity();
        userIdentity.setIdentityType("password");
        userIdentity.setIdentifier(registerRequestDTO.getIdentifier());
        userIdentity.setCredential(registerRequestDTO.getCredential());
        userIdentity.setCreateTime(new Date());
        userIdentity.setLastEditTime(new Date());

        authService.registerUser(personInfo, userIdentity);
        return ResponseResultWrap.success("注册成功");

    }

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseResultWrap<String> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO, BindingResult result) {
        if (result.hasErrors()) {
            String errStr = result
                    .getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + "：" + error.getDefaultMessage())
                    .collect(Collectors.joining());
            throw new BusinessException(errStr);
        }

        String token = authService.login(loginRequestDTO.getIdentifier(), loginRequestDTO.getCredential());
        return ResponseResultWrap.success(token, "登录成功");
    }

    @ResponseBody
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public ResponseResultWrap<UserInfoVO> getUserInfo() {
        PersonInfoDTO personInfoDTO = UserContextHolder.getUserInfo();
        UserInfoVO userInfoVO = authService.queryUserInfoVOById(Math.toIntExact(personInfoDTO.getUserId()));
        return ResponseResultWrap.success(userInfoVO);
    }


}
