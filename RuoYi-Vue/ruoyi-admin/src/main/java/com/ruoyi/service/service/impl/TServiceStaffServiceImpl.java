package com.ruoyi.service.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.service.mapper.TServiceStaffMapper;
import com.ruoyi.service.domain.TServiceStaff;
import com.ruoyi.service.service.ITServiceStaffService;
import com.ruoyi.elderly.domain.TAppUser;
import com.ruoyi.elderly.service.ITAppUserService;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务人员管理Service业务层处理
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@Service
public class TServiceStaffServiceImpl implements ITServiceStaffService 
{
    @Autowired
    private TServiceStaffMapper tServiceStaffMapper;

    @Autowired
    private ITAppUserService appUserService;

    /**
     * 查询服务人员管理
     * 
     * @param staffId 服务人员管理主键
     * @return 服务人员管理
     */
    @Override
    public TServiceStaff selectTServiceStaffByStaffId(Long staffId)
    {
        return tServiceStaffMapper.selectTServiceStaffByStaffId(staffId);
    }

    /**
     * 查询服务人员管理列表
     * 
     * @param tServiceStaff 服务人员管理
     * @return 服务人员管理
     */
    @Override
    public List<TServiceStaff> selectTServiceStaffList(TServiceStaff tServiceStaff)
    {
        return tServiceStaffMapper.selectTServiceStaffList(tServiceStaff);
    }

    /**
     * 新增服务人员管理
     * 
     * @param tServiceStaff 服务人员管理
     * @return 结果
     */
    @Override
    @Transactional
    public int insertTServiceStaff(TServiceStaff tServiceStaff)
    {
        // 自动开通小程序登录账号
        if (tServiceStaff.getPhone() != null) {
            TAppUser query = new TAppUser();
            query.setPhone(tServiceStaff.getPhone());
            List<TAppUser> users = appUserService.selectTAppUserList(query);
            
            Long userId;
            if (users != null && !users.isEmpty()) {
                // 如果账号已存在，直接获取userId
                userId = users.get(0).getUserId();
            } else {
                // 如果不存在，则新建账号
                TAppUser newUser = new TAppUser();
                newUser.setPhone(tServiceStaff.getPhone());
                newUser.setRealName(tServiceStaff.getStaffName());
                // 默认初始密码 123456
                newUser.setPassword(SecurityUtils.encryptPassword("123456"));
                newUser.setUserType("worker");
                newUser.setStatus(0L); // 正常
                appUserService.insertTAppUser(newUser);
                userId = newUser.getUserId();
            }
            // 绑定到员工表
            tServiceStaff.setUserId(userId);
        }
        
        tServiceStaff.setCreateTime(DateUtils.getNowDate());
        return tServiceStaffMapper.insertTServiceStaff(tServiceStaff);
    }

    /**
     * 修改服务人员管理
     * 
     * @param tServiceStaff 服务人员管理
     * @return 结果
     */
    @Override
    public int updateTServiceStaff(TServiceStaff tServiceStaff)
    {
        tServiceStaff.setUpdateTime(DateUtils.getNowDate());
        return tServiceStaffMapper.updateTServiceStaff(tServiceStaff);
    }

    /**
     * 批量删除服务人员管理
     * 
     * @param staffIds 需要删除的服务人员管理主键
     * @return 结果
     */
    @Override
    public int deleteTServiceStaffByStaffIds(Long[] staffIds)
    {
        return tServiceStaffMapper.deleteTServiceStaffByStaffIds(staffIds);
    }

    /**
     * 删除服务人员管理信息
     * 
     * @param staffId 服务人员管理主键
     * @return 结果
     */
    @Override
    public int deleteTServiceStaffByStaffId(Long staffId)
    {
        return tServiceStaffMapper.deleteTServiceStaffByStaffId(staffId);
    }
}
