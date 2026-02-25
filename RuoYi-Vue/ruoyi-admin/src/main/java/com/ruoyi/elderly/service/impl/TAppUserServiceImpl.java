package com.ruoyi.elderly.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.elderly.mapper.TAppUserMapper;
import com.ruoyi.elderly.domain.TAppUser;
import com.ruoyi.elderly.service.ITAppUserService;

/**
 * C端用户账号Service业务层处理
 * 
 * @author zhangTing
 * @date 2026-02-24
 */
@Service
public class TAppUserServiceImpl implements ITAppUserService 
{
    @Autowired
    private TAppUserMapper tAppUserMapper;

    /**
     * 查询C端用户账号
     * 
     * @param userId C端用户账号主键
     * @return C端用户账号
     */
    @Override
    public TAppUser selectTAppUserByUserId(Long userId)
    {
        return tAppUserMapper.selectTAppUserByUserId(userId);
    }

    /**
     * 查询C端用户账号列表
     * 
     * @param tAppUser C端用户账号
     * @return C端用户账号
     */
    @Override
    public List<TAppUser> selectTAppUserList(TAppUser tAppUser)
    {
        return tAppUserMapper.selectTAppUserList(tAppUser);
    }

    /**
     * 新增C端用户账号
     * 
     * @param tAppUser C端用户账号
     * @return 结果
     */
    @Override
    public int insertTAppUser(TAppUser tAppUser)
    {
        tAppUser.setCreateTime(DateUtils.getNowDate());
        return tAppUserMapper.insertTAppUser(tAppUser);
    }

    /**
     * 修改C端用户账号
     * 
     * @param tAppUser C端用户账号
     * @return 结果
     */
    @Override
    public int updateTAppUser(TAppUser tAppUser)
    {
        tAppUser.setUpdateTime(DateUtils.getNowDate());
        return tAppUserMapper.updateTAppUser(tAppUser);
    }

    /**
     * 批量删除C端用户账号
     * 
     * @param userIds 需要删除的C端用户账号主键
     * @return 结果
     */
    @Override
    public int deleteTAppUserByUserIds(Long[] userIds)
    {
        return tAppUserMapper.deleteTAppUserByUserIds(userIds);
    }

    /**
     * 删除C端用户账号信息
     * 
     * @param userId C端用户账号主键
     * @return 结果
     */
    @Override
    public int deleteTAppUserByUserId(Long userId)
    {
        return tAppUserMapper.deleteTAppUserByUserId(userId);
    }
}
