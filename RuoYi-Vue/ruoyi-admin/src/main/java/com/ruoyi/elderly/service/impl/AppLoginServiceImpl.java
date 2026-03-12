package com.ruoyi.elderly.service.impl;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.domain.model.AppRegisterBody;
import com.ruoyi.common.core.domain.model.AppUserInfo;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.elderly.domain.TAppUser;
import com.ruoyi.elderly.mapper.TAppUserMapper;
import com.ruoyi.elderly.service.IAppLoginService;
import com.ruoyi.framework.web.service.TokenService;

/**
 * C端用户登录注册Service实现
 * 
 * @author zhangTing
 */
@Service
public class AppLoginServiceImpl implements IAppLoginService {

    /** 最大重试次数 */
    private static final int MAX_RETRY_COUNT = 3;

    /** 锁定时间（分钟） */
    private static final int LOCK_TIME = 10;

    /** Redis缓存key前缀 */
    private static final String APP_LOGIN_RETRY_KEY = "app_login_retry:";

    @Autowired
    private TAppUserMapper appUserMapper;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisCache redisCache;

    /**
     * C端用户登录
     */
    @Override
    public String login(String phone, String password) {
        // 参数校验
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(password)) {
            throw new ServiceException("手机号或密码不能为空");
        }

        // 检查是否已被锁定
        String retryKey = APP_LOGIN_RETRY_KEY + phone;
        Integer retryCount = redisCache.getCacheObject(retryKey);
        if (retryCount != null && retryCount >= MAX_RETRY_COUNT) {
            throw new ServiceException("密码错误次数过多，账号已锁定" + LOCK_TIME + "分钟，请稍后再试");
        }

        // 查询用户
        TAppUser appUser = appUserMapper.selectTAppUserByPhone(phone);
        if (appUser == null) {
            throw new ServiceException("用户不存在或手机号错误");
        }

        // 检查账号状态
        if (appUser.getStatus() != null && appUser.getStatus() != 0) {
            throw new ServiceException("账号已被禁用，请联系管理员");
        }

        // 验证密码
        if (!SecurityUtils.matchesPassword(password, appUser.getPassword())) {
            // 增加重试次数
            retryCount = (retryCount == null) ? 1 : retryCount + 1;
            redisCache.setCacheObject(retryKey, retryCount, LOCK_TIME, TimeUnit.MINUTES);
            int remaining = MAX_RETRY_COUNT - retryCount;
            if (remaining <= 0) {
                throw new ServiceException("密码错误次数过多，账号已锁定" + LOCK_TIME + "分钟，请稍后再试");
            }
            throw new ServiceException("密码错误，还剩" + remaining + "次机会");
        }

        // 登录成功，清除重试计数
        redisCache.deleteObject(retryKey);

        // 构建 LoginUser
        AppUserInfo appUserInfo = toAppUserInfo(appUser);
        LoginUser loginUser = new LoginUser(appUser.getUserId(), appUserInfo);

        // 更新登录信息
        recordLoginInfo(appUser.getUserId());

        // 生成 Token
        return tokenService.createToken(loginUser);
    }

    /**
     * C端用户注册
     */
    @Override
    public void register(AppRegisterBody body) {
        // 参数校验
        if (StringUtils.isEmpty(body.getPhone()) || StringUtils.isEmpty(body.getPassword())) {
            throw new ServiceException("手机号和密码不能为空");
        }

        // 检查手机号是否已注册
        TAppUser existUser = appUserMapper.selectTAppUserByPhone(body.getPhone());
        if (existUser != null) {
            throw new ServiceException("该手机号已注册");
        }

        // 创建新用户
        TAppUser newUser = new TAppUser();
        newUser.setPhone(body.getPhone());
        newUser.setPassword(SecurityUtils.encryptPassword(body.getPassword()));
        newUser.setRealName(body.getRealName());
        newUser.setUserType(StringUtils.isEmpty(body.getUserType()) ? "elderly" : body.getUserType());
        newUser.setStatus(0L); // 0表示正常，1表示禁用
        newUser.setCreateTime(DateUtils.getNowDate());
        appUserMapper.insertTAppUser(newUser);
    }

    /**
     * 获取当前登录的C端用户信息
     */
    @Override
    public TAppUser getAppUserInfo(LoginUser loginUser) {
        if (loginUser == null || loginUser.getAppUser() == null) {
            throw new ServiceException("未登录或非C端用户");
        }
        return appUserMapper.selectTAppUserByUserId(loginUser.getUserId());
    }

    /**
     * 记录登录信息
     */
    private void recordLoginInfo(Long userId) {
        TAppUser updateUser = new TAppUser();
        updateUser.setUserId(userId);
        updateUser.setLastLoginTime(DateUtils.getNowDate());
        updateUser.setLastLoginIp(IpUtils.getIpAddr());
        appUserMapper.updateTAppUser(updateUser);
    }

    /**
     * TAppUser 转 AppUserInfo（跨模块传递）
     */
    private AppUserInfo toAppUserInfo(TAppUser appUser) {
        AppUserInfo info = new AppUserInfo();
        info.setUserId(appUser.getUserId());
        info.setPhone(appUser.getPhone());
        info.setPassword(appUser.getPassword());
        info.setRealName(appUser.getRealName());
        info.setUserType(appUser.getUserType());
        info.setAvatar(appUser.getAvatar());
        info.setStatus(appUser.getStatus());
        info.setLastLoginTime(appUser.getLastLoginTime());
        info.setLastLoginIp(appUser.getLastLoginIp());
        return info;
    }
}
