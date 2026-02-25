package com.ruoyi.elderly.service;

import com.ruoyi.common.core.domain.model.AppRegisterBody;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.elderly.domain.TAppUser;

/**
 * C端用户登录注册Service接口
 * 
 * @author zhangTing
 */
public interface IAppLoginService {
    /**
     * C端用户登录
     * 
     * @param phone    手机号
     * @param password 密码
     * @return Token
     */
    public String login(String phone, String password);

    /**
     * C端用户注册
     * 
     * @param body 注册请求体
     */
    public void register(AppRegisterBody body);

    /**
     * 获取当前登录的C端用户信息
     * 
     * @param loginUser 登录用户
     * @return C端用户信息
     */
    public TAppUser getAppUserInfo(LoginUser loginUser);
}
