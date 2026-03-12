package com.ruoyi.elderly.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.elderly.mapper.TElderlyMapper;
import com.ruoyi.elderly.domain.TElderly;
import com.ruoyi.elderly.mapper.TAppUserMapper;
import com.ruoyi.elderly.domain.TAppUser;
import com.ruoyi.elderly.service.ITElderlyService;

/**
 * 老人基础信息Service业务层处理
 * 
 * @author zhangTing
 * @date 2026-02-24
 */
@Service
public class TElderlyServiceImpl implements ITElderlyService 
{
    @Autowired
    private TElderlyMapper tElderlyMapper;

    @Autowired
    private TAppUserMapper tAppUserMapper;

    /**
     * 查询老人基础信息
     * 
     * @param elderlyId 老人基础信息主键
     * @return 老人基础信息
     */
    @Override
    public TElderly selectTElderlyByElderlyId(Long elderlyId)
    {
        return tElderlyMapper.selectTElderlyByElderlyId(elderlyId);
    }

    /**
     * 查询老人基础信息列表
     * 
     * @param tElderly 老人基础信息
     * @return 老人基础信息
     */
    @Override
    public List<TElderly> selectTElderlyList(TElderly tElderly)
    {
        return tElderlyMapper.selectTElderlyList(tElderly);
    }

    /**
     * 新增老人基础信息
     * 
     * @param tElderly 老人基础信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertTElderly(TElderly tElderly)
    {
        // 1. 检查手机号：必须有手机号才能生成 C 端账号
        String phone = tElderly.getPhone();
        if (phone == null || phone.trim().isEmpty()) {
            throw new ServiceException("新增老人失败，手机号不能为空，因为需要用于创建注册账号");
        }

        TAppUser existUser = tAppUserMapper.selectTAppUserByPhone(phone);
        if (existUser != null) {
            throw new ServiceException("新增老人失败，手机号 " + phone + " 已注册账号");
        }
        
        // 2. 自动创建 C端 用户账号 t_app_user
        TAppUser newUser = new TAppUser();
        newUser.setPhone(phone);
        newUser.setPassword(SecurityUtils.encryptPassword("123456")); // 默认密码 123456
        newUser.setRealName(tElderly.getName());
        newUser.setUserType("elderly");
        newUser.setStatus(0L); // 0正常
        newUser.setCreateTime(DateUtils.getNowDate());
        tAppUserMapper.insertTAppUser(newUser);

        // 3. 将生成的 userId 赋值给老人信息
        tElderly.setUserId(newUser.getUserId());

        tElderly.setCreateTime(DateUtils.getNowDate());
        return tElderlyMapper.insertTElderly(tElderly);
    }

    /**
     * 修改老人基础信息
     * 
     * @param tElderly 老人基础信息
     * @return 结果
     */
    @Override
    public int updateTElderly(TElderly tElderly)
    {
        tElderly.setUpdateTime(DateUtils.getNowDate());
        return tElderlyMapper.updateTElderly(tElderly);
    }

    /**
     * 批量删除老人基础信息
     * 
     * @param elderlyIds 需要删除的老人基础信息主键
     * @return 结果
     */
    @Override
    public int deleteTElderlyByElderlyIds(Long[] elderlyIds)
    {
        return tElderlyMapper.deleteTElderlyByElderlyIds(elderlyIds);
    }

    /**
     * 删除老人基础信息信息
     * 
     * @param elderlyId 老人基础信息主键
     * @return 结果
     */
    @Override
    public int deleteTElderlyByElderlyId(Long elderlyId)
    {
        return tElderlyMapper.deleteTElderlyByElderlyId(elderlyId);
    }
}
