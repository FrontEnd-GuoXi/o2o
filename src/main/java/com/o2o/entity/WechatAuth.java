package com.o2o.entity;

import java.util.Date;

public class WechatAuth {

    private Long wechatAuthId;
    private String openId;
    private Date createTime;
    private PersonInfo personInfo;


    public Long getWechatAuthId() {
        return wechatAuthId;
    }

    public String getOpenId() {
        return openId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public PersonInfo getPersonInfo() {
        return personInfo;
    }


    public void setWechatAuthId(Long wechatAuthId) {
        this.wechatAuthId = wechatAuthId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setPersonInfo(PersonInfo personInfo) {
        this.personInfo = personInfo;
    }
}
