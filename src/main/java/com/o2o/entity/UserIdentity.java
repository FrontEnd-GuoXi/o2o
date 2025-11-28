package com.o2o.entity;


import java.util.Date;

/**
 * 用户登录凭证实体类
 * 对应数据库表 tb_user_identity
 */
public class UserIdentity {

    private Integer id;                    // 主键 ID
    private Long userId;                // 关联用户 ID（tb_person_info.user_id）
    private String identityType;           // 登录类型：password, wechat, phone 等
    private String identifier;             // 唯一标识：用户名、微信 openid、手机号等
    private String credential;             // 凭证：密码哈希值、access_token 等
    private Date createTime;               // 创建时间
    private Date lastEditTime;

    // ================== Getter and Setter ==================

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(Date lastEditTime) {
        this.lastEditTime = lastEditTime;
    }


}