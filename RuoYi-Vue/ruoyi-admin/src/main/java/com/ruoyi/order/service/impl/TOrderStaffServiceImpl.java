package com.ruoyi.order.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.order.mapper.TOrderStaffMapper;
import com.ruoyi.order.domain.TOrderStaff;
import com.ruoyi.order.service.ITOrderStaffService;

/**
 * 订单-服务人员关联Service业务层处理
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@Service
public class TOrderStaffServiceImpl implements ITOrderStaffService 
{
    @Autowired
    private TOrderStaffMapper tOrderStaffMapper;

    /**
     * 查询订单-服务人员关联
     * 
     * @param id 订单-服务人员关联主键
     * @return 订单-服务人员关联
     */
    @Override
    public TOrderStaff selectTOrderStaffById(Long id)
    {
        return tOrderStaffMapper.selectTOrderStaffById(id);
    }

    /**
     * 查询订单-服务人员关联列表
     * 
     * @param tOrderStaff 订单-服务人员关联
     * @return 订单-服务人员关联
     */
    @Override
    public List<TOrderStaff> selectTOrderStaffList(TOrderStaff tOrderStaff)
    {
        return tOrderStaffMapper.selectTOrderStaffList(tOrderStaff);
    }

    /**
     * 新增订单-服务人员关联
     * 
     * @param tOrderStaff 订单-服务人员关联
     * @return 结果
     */
    @Override
    public int insertTOrderStaff(TOrderStaff tOrderStaff)
    {
        tOrderStaff.setCreateTime(DateUtils.getNowDate());
        return tOrderStaffMapper.insertTOrderStaff(tOrderStaff);
    }

    /**
     * 修改订单-服务人员关联
     * 
     * @param tOrderStaff 订单-服务人员关联
     * @return 结果
     */
    @Override
    public int updateTOrderStaff(TOrderStaff tOrderStaff)
    {
        tOrderStaff.setUpdateTime(DateUtils.getNowDate());
        return tOrderStaffMapper.updateTOrderStaff(tOrderStaff);
    }

    /**
     * 批量删除订单-服务人员关联
     * 
     * @param ids 需要删除的订单-服务人员关联主键
     * @return 结果
     */
    @Override
    public int deleteTOrderStaffByIds(Long[] ids)
    {
        return tOrderStaffMapper.deleteTOrderStaffByIds(ids);
    }

    /**
     * 删除订单-服务人员关联信息
     * 
     * @param id 订单-服务人员关联主键
     * @return 结果
     */
    @Override
    public int deleteTOrderStaffById(Long id)
    {
        return tOrderStaffMapper.deleteTOrderStaffById(id);
    }
}
