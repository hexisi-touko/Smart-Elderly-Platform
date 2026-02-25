package com.ruoyi.common.core.domain.model;

/**
 * C端用户登录请求体
 * 
 * @author zhangTing
 */
public class AppLoginBody {
    /** 手机号 */
    private String phone;

    /** 密码 */
    private String password;

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
}
