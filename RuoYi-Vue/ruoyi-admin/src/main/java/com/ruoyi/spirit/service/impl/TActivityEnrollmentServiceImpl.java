package com.ruoyi.spirit.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.spirit.mapper.TActivityEnrollmentMapper;
import com.ruoyi.spirit.domain.TActivityEnrollment;
import com.ruoyi.spirit.service.ITActivityEnrollmentService;

/**
 * 活动报名管理Service业务层处理
 * 
 * @author zhangTing
 * @date 2026-03-11
 */
@Service
public class TActivityEnrollmentServiceImpl implements ITActivityEnrollmentService 
{
    @Autowired
    private TActivityEnrollmentMapper tActivityEnrollmentMapper;

    /**
     * 查询活动报名管理
     * 
     * @param id 活动报名管理主键
     * @return 活动报名管理
     */
    @Override
    public TActivityEnrollment selectTActivityEnrollmentById(Long id)
    {
        return tActivityEnrollmentMapper.selectTActivityEnrollmentById(id);
    }

    /**
     * 查询活动报名管理列表
     * 
     * @param tActivityEnrollment 活动报名管理
     * @return 活动报名管理
     */
    @Override
    public List<TActivityEnrollment> selectTActivityEnrollmentList(TActivityEnrollment tActivityEnrollment)
    {
        return tActivityEnrollmentMapper.selectTActivityEnrollmentList(tActivityEnrollment);
    }

    /**
     * 新增活动报名管理
     * 
     * @param tActivityEnrollment 活动报名管理
     * @return 结果
     */
    @Override
    public int insertTActivityEnrollment(TActivityEnrollment tActivityEnrollment)
    {
        tActivityEnrollment.setCreateTime(DateUtils.getNowDate());
        return tActivityEnrollmentMapper.insertTActivityEnrollment(tActivityEnrollment);
    }

    /**
     * 修改活动报名管理
     * 
     * @param tActivityEnrollment 活动报名管理
     * @return 结果
     */
    @Override
    public int updateTActivityEnrollment(TActivityEnrollment tActivityEnrollment)
    {
        tActivityEnrollment.setUpdateTime(DateUtils.getNowDate());
        return tActivityEnrollmentMapper.updateTActivityEnrollment(tActivityEnrollment);
    }

    /**
     * 批量删除活动报名管理
     * 
     * @param ids 需要删除的活动报名管理主键
     * @return 结果
     */
    @Override
    public int deleteTActivityEnrollmentByIds(Long[] ids)
    {
        return tActivityEnrollmentMapper.deleteTActivityEnrollmentByIds(ids);
    }

    /**
     * 删除活动报名管理信息
     * 
     * @param id 活动报名管理主键
     * @return 结果
     */
    @Override
    public int deleteTActivityEnrollmentById(Long id)
    {
        return tActivityEnrollmentMapper.deleteTActivityEnrollmentById(id);
    }
}
