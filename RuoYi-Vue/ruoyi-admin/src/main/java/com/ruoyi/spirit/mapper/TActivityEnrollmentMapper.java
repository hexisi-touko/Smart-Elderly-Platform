package com.ruoyi.spirit.mapper;

import java.util.List;
import com.ruoyi.spirit.domain.TActivityEnrollment;

/**
 * 活动报名管理Mapper接口
 * 
 * @author zhangTing
 * @date 2026-03-11
 */
public interface TActivityEnrollmentMapper 
{
    /**
     * 查询活动报名管理
     * 
     * @param id 活动报名管理主键
     * @return 活动报名管理
     */
    public TActivityEnrollment selectTActivityEnrollmentById(Long id);

    /**
     * 查询活动报名管理列表
     * 
     * @param tActivityEnrollment 活动报名管理
     * @return 活动报名管理集合
     */
    public List<TActivityEnrollment> selectTActivityEnrollmentList(TActivityEnrollment tActivityEnrollment);

    /**
     * 新增活动报名管理
     * 
     * @param tActivityEnrollment 活动报名管理
     * @return 结果
     */
    public int insertTActivityEnrollment(TActivityEnrollment tActivityEnrollment);

    /**
     * 修改活动报名管理
     * 
     * @param tActivityEnrollment 活动报名管理
     * @return 结果
     */
    public int updateTActivityEnrollment(TActivityEnrollment tActivityEnrollment);

    /**
     * 删除活动报名管理
     * 
     * @param id 活动报名管理主键
     * @return 结果
     */
    public int deleteTActivityEnrollmentById(Long id);

    /**
     * 批量删除活动报名管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTActivityEnrollmentByIds(Long[] ids);
}
