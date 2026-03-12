package com.ruoyi.elderly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.elderly.domain.TElderly;
import com.ruoyi.elderly.domain.TGuardian;
import com.ruoyi.elderly.mapper.TElderlyMapper;
import com.ruoyi.elderly.mapper.TGuardianMapper;
import com.ruoyi.elderly.domain.TAppUser;
import com.ruoyi.elderly.mapper.TAppUserMapper;
import com.ruoyi.common.exception.ServiceException;

@RestController
@RequestMapping("/app/profile")
public class AppProfileController {

    @Autowired
    private TElderlyMapper tElderlyMapper;

    @Autowired
    private TGuardianMapper tGuardianMapper;

    @Autowired
    private TAppUserMapper tAppUserMapper;

    @PostMapping("/elderly")
    public AjaxResult completeElderlyProfile(@RequestBody TElderly tElderly) {
        Long userId = SecurityUtils.getUserId();
        
        // Ensure the phone is saved or matched
        if (tElderly.getPhone() == null || tElderly.getPhone().isEmpty()) {
            throw new ServiceException("手机号不能为空");
        }

        tElderly.setUserId(userId);
        tElderly.setCreateTime(DateUtils.getNowDate());
        tElderlyMapper.insertTElderly(tElderly);
        return AjaxResult.success();
    }

    @PostMapping("/guardian")
    public AjaxResult completeGuardianProfile(@RequestBody TGuardian tGuardian) {
        Long userId = SecurityUtils.getUserId();

        if (tGuardian.getPhone() == null || tGuardian.getPhone().isEmpty()) {
            throw new ServiceException("手机号不能为空");
        }

        tGuardian.setUserId(userId);
        tGuardian.setCreateTime(DateUtils.getNowDate());
        tGuardianMapper.insertTGuardian(tGuardian);
        return AjaxResult.success();
    }
}
