package com.ruoyi.service.mapper;

import java.util.List;
import com.ruoyi.service.domain.TServiceStaff;

/**
 * 服务人员管理Mapper接口
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public interface TServiceStaffMapper 
{
    /**
     * 查询服务人员管理
     * 
     * @param staffId 服务人员管理主键
     * @return 服务人员管理
     */
    public TServiceStaff selectTServiceStaffByStaffId(Long staffId);

    /**
     * 查询服务人员管理列表
     * 
     * @param tServiceStaff 服务人员管理
     * @return 服务人员管理集合
     */
    public List<TServiceStaff> selectTServiceStaffList(TServiceStaff tServiceStaff);

    /**
     * 新增服务人员管理
     * 
     * @param tServiceStaff 服务人员管理
     * @return 结果
     */
    public int insertTServiceStaff(TServiceStaff tServiceStaff);

    /**
     * 修改服务人员管理
     * 
     * @param tServiceStaff 服务人员管理
     * @return 结果
     */
    public int updateTServiceStaff(TServiceStaff tServiceStaff);

    /**
     * 删除服务人员管理
     * 
     * @param staffId 服务人员管理主键
     * @return 结果
     */
    public int deleteTServiceStaffByStaffId(Long staffId);

    /**
     * 批量删除服务人员管理
     * 
     * @param staffIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTServiceStaffByStaffIds(Long[] staffIds);
}
