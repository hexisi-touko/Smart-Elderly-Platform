package com.ruoyi.common.core.domain.model;

/**
 * C端用户注册请求体
 * 
 * @author zhangTing
 */
public class AppRegisterBody {
    /** 手机号 */
    private String phone;

    /** 密码 */
    private String password;

    /** 真实姓名 */
    private String realName;

    /** 用户类型（elderly/guardian） */
    private String userType;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
