package com.ruoyi.spirit.service;

import java.util.List;
import com.ruoyi.spirit.domain.TOnlineCourse;

/**
 * 线上课程管理Service接口
 * 
 * @author zhangTing
 * @date 2026-03-11
 */
public interface ITOnlineCourseService 
{
    /**
     * 查询线上课程管理
     * 
     * @param courseId 线上课程管理主键
     * @return 线上课程管理
     */
    public TOnlineCourse selectTOnlineCourseByCourseId(Long courseId);

    /**
     * 查询线上课程管理列表
     * 
     * @param tOnlineCourse 线上课程管理
     * @return 线上课程管理集合
     */
    public List<TOnlineCourse> selectTOnlineCourseList(TOnlineCourse tOnlineCourse);

    /**
     * 新增线上课程管理
     * 
     * @param tOnlineCourse 线上课程管理
     * @return 结果
     */
    public int insertTOnlineCourse(TOnlineCourse tOnlineCourse);

    /**
     * 修改线上课程管理
     * 
     * @param tOnlineCourse 线上课程管理
     * @return 结果
     */
    public int updateTOnlineCourse(TOnlineCourse tOnlineCourse);

    /**
     * 批量删除线上课程管理
     * 
     * @param courseIds 需要删除的线上课程管理主键集合
     * @return 结果
     */
    public int deleteTOnlineCourseByCourseIds(Long[] courseIds);

    /**
     * 删除线上课程管理信息
     * 
     * @param courseId 线上课程管理主键
     * @return 结果
     */
    public int deleteTOnlineCourseByCourseId(Long courseId);
}
