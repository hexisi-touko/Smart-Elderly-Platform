package com.ruoyi.elderly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.AppLoginBody;
import com.ruoyi.common.core.domain.model.AppRegisterBody;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.elderly.domain.TAppUser;
import com.ruoyi.elderly.service.IAppLoginService;
import com.ruoyi.framework.web.service.TokenService;

/**
 * C端用户认证Controller
 * 
 * @author zhangTing
 */
@RestController
@RequestMapping("/app")
public class AppLoginController {
    @Autowired
    private IAppLoginService appLoginService;

    @Autowired
    private TokenService tokenService;

    /**
     * C端用户登录
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody AppLoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        String token = appLoginService.login(loginBody.getPhone(), loginBody.getPassword());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * C端用户注册
     */
    @PostMapping("/register")
    public AjaxResult register(@RequestBody AppRegisterBody registerBody) {
        appLoginService.register(registerBody);
        return AjaxResult.success("注册成功");
    }

    /**
     * 获取C端用户信息
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        TAppUser appUser = appLoginService.getAppUserInfo(loginUser);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", appUser);
        return ajax;
    }

    /**
     * C端用户退出
     */
    @PostMapping("/logout")
    public AjaxResult logout() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser != null) {
            tokenService.delLoginUser(loginUser.getToken());
        }
        return AjaxResult.success("退出成功");
    }
}
