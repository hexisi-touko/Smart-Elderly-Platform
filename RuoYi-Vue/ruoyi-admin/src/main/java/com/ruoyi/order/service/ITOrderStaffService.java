package com.ruoyi.order.service;

import java.util.List;
import com.ruoyi.order.domain.TOrderStaff;

/**
 * 订单-服务人员关联Service接口
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public interface ITOrderStaffService 
{
    /**
     * 查询订单-服务人员关联
     * 
     * @param id 订单-服务人员关联主键
     * @return 订单-服务人员关联
     */
    public TOrderStaff selectTOrderStaffById(Long id);

    /**
     * 查询订单-服务人员关联列表
     * 
     * @param tOrderStaff 订单-服务人员关联
     * @return 订单-服务人员关联集合
     */
    public List<TOrderStaff> selectTOrderStaffList(TOrderStaff tOrderStaff);

    /**
     * 新增订单-服务人员关联
     * 
     * @param tOrderStaff 订单-服务人员关联
     * @return 结果
     */
    public int insertTOrderStaff(TOrderStaff tOrderStaff);

    /**
     * 修改订单-服务人员关联
     * 
     * @param tOrderStaff 订单-服务人员关联
     * @return 结果
     */
    public int updateTOrderStaff(TOrderStaff tOrderStaff);

    /**
     * 批量删除订单-服务人员关联
     * 
     * @param ids 需要删除的订单-服务人员关联主键集合
     * @return 结果
     */
    public int deleteTOrderStaffByIds(Long[] ids);

    /**
     * 删除订单-服务人员关联信息
     * 
     * @param id 订单-服务人员关联主键
     * @return 结果
     */
    public int deleteTOrderStaffById(Long id);
}
