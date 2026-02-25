package com.ruoyi.elderly.mapper;

import java.util.List;
import com.ruoyi.elderly.domain.TAppUser;

/**
 * C端用户账号Mapper接口
 * 
 * @author zhangTing
 * @date 2026-02-24
 */
public interface TAppUserMapper {
    /**
     * 查询C端用户账号
     * 
     * @param userId C端用户账号主键
     * @return C端用户账号
     */
    public TAppUser selectTAppUserByUserId(Long userId);

    /**
     * 查询C端用户账号列表
     * 
     * @param tAppUser C端用户账号
     * @return C端用户账号集合
     */
    public List<TAppUser> selectTAppUserList(TAppUser tAppUser);

    /**
     * 新增C端用户账号
     * 
     * @param tAppUser C端用户账号
     * @return 结果
     */
    public int insertTAppUser(TAppUser tAppUser);

    /**
     * 修改C端用户账号
     * 
     * @param tAppUser C端用户账号
     * @return 结果
     */
    public int updateTAppUser(TAppUser tAppUser);

    /**
     * 删除C端用户账号
     * 
     * @param userId C端用户账号主键
     * @return 结果
     */
    public int deleteTAppUserByUserId(Long userId);

    /**
     * 批量删除C端用户账号
     * 
     * @param userIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTAppUserByUserIds(Long[] userIds);

    /**
     * 根据手机号查询C端用户
     * 
     * @param phone 手机号
     * @return C端用户账号
     */
    public TAppUser selectTAppUserByPhone(String phone);
}
