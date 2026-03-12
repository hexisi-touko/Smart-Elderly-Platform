package com.ruoyi.elderly.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.elderly.mapper.TGuardianMapper;
import com.ruoyi.elderly.domain.TGuardian;
import com.ruoyi.elderly.mapper.TAppUserMapper;
import com.ruoyi.elderly.domain.TAppUser;
import com.ruoyi.elderly.service.ITGuardianService;

/**
 * 监护人信息Service业务层处理
 * 
 * @author zhangTing
 * @date 2026-02-24
 */
@Service
public class TGuardianServiceImpl implements ITGuardianService 
{
    @Autowired
    private TGuardianMapper tGuardianMapper;

    @Autowired
    private TAppUserMapper tAppUserMapper;

    /**
     * 查询监护人信息
     * 
     * @param guardianId 监护人信息主键
     * @return 监护人信息
     */
    @Override
    public TGuardian selectTGuardianByGuardianId(Long guardianId)
    {
        return tGuardianMapper.selectTGuardianByGuardianId(guardianId);
    }

    /**
     * 查询监护人信息列表
     * 
     * @param tGuardian 监护人信息
     * @return 监护人信息
     */
    @Override
    public List<TGuardian> selectTGuardianList(TGuardian tGuardian)
    {
        return tGuardianMapper.selectTGuardianList(tGuardian);
    }

    /**
     * 新增监护人信息
     * 
     * @param tGuardian 监护人信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertTGuardian(TGuardian tGuardian)
    {
        // 1. 检查手机号：必须有手机号才能生成 C 端账号
        String phone = tGuardian.getPhone();
        if (phone == null || phone.trim().isEmpty()) {
            throw new ServiceException("新增监护人失败，手机号不能为空，因为需要用于创建注册账号");
        }

        TAppUser existUser = tAppUserMapper.selectTAppUserByPhone(phone);
        if (existUser != null) {
            throw new ServiceException("新增监护人失败，手机号 " + phone + " 已注册账号");
        }
        
        // 2. 自动创建 C端 用户账号 t_app_user
        TAppUser newUser = new TAppUser();
        newUser.setPhone(phone);
        newUser.setPassword(SecurityUtils.encryptPassword("123456")); // 默认密码 123456
        newUser.setRealName(tGuardian.getName());
        newUser.setUserType("guardian");
        newUser.setStatus(0L); // 0正常
        newUser.setCreateTime(DateUtils.getNowDate());
        tAppUserMapper.insertTAppUser(newUser);

        // 3. 将生成的 userId 赋值给监护人信息
        tGuardian.setUserId(newUser.getUserId());

        tGuardian.setCreateTime(DateUtils.getNowDate());
        return tGuardianMapper.insertTGuardian(tGuardian);
    }

    /**
     * 修改监护人信息
     * 
     * @param tGuardian 监护人信息
     * @return 结果
     */
    @Override
    public int updateTGuardian(TGuardian tGuardian)
    {
        tGuardian.setUpdateTime(DateUtils.getNowDate());
        return tGuardianMapper.updateTGuardian(tGuardian);
    }

    /**
     * 批量删除监护人信息
     * 
     * @param guardianIds 需要删除的监护人信息主键
     * @return 结果
     */
    @Override
    public int deleteTGuardianByGuardianIds(Long[] guardianIds)
    {
        return tGuardianMapper.deleteTGuardianByGuardianIds(guardianIds);
    }

    /**
     * 删除监护人信息信息
     * 
     * @param guardianId 监护人信息主键
     * @return 结果
     */
    @Override
    public int deleteTGuardianByGuardianId(Long guardianId)
    {
        return tGuardianMapper.deleteTGuardianByGuardianId(guardianId);
    }
}
