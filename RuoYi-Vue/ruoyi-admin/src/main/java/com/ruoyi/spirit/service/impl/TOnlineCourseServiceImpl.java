package com.ruoyi.spirit.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.spirit.mapper.TOnlineCourseMapper;
import com.ruoyi.spirit.domain.TOnlineCourse;
import com.ruoyi.spirit.service.ITOnlineCourseService;

/**
 * 线上课程管理Service业务层处理
 * 
 * @author zhangTing
 * @date 2026-03-11
 */
@Service
public class TOnlineCourseServiceImpl implements ITOnlineCourseService 
{
    @Autowired
    private TOnlineCourseMapper tOnlineCourseMapper;

    /**
     * 查询线上课程管理
     * 
     * @param courseId 线上课程管理主键
     * @return 线上课程管理
     */
    @Override
    public TOnlineCourse selectTOnlineCourseByCourseId(Long courseId)
    {
        return tOnlineCourseMapper.selectTOnlineCourseByCourseId(courseId);
    }

    /**
     * 查询线上课程管理列表
     * 
     * @param tOnlineCourse 线上课程管理
     * @return 线上课程管理
     */
    @Override
    public List<TOnlineCourse> selectTOnlineCourseList(TOnlineCourse tOnlineCourse)
    {
        return tOnlineCourseMapper.selectTOnlineCourseList(tOnlineCourse);
    }

    /**
     * 新增线上课程管理
     * 
     * @param tOnlineCourse 线上课程管理
     * @return 结果
     */
    @Override
    public int insertTOnlineCourse(TOnlineCourse tOnlineCourse)
    {
        tOnlineCourse.setCreateTime(DateUtils.getNowDate());
        return tOnlineCourseMapper.insertTOnlineCourse(tOnlineCourse);
    }

    /**
     * 修改线上课程管理
     * 
     * @param tOnlineCourse 线上课程管理
     * @return 结果
     */
    @Override
    public int updateTOnlineCourse(TOnlineCourse tOnlineCourse)
    {
        tOnlineCourse.setUpdateTime(DateUtils.getNowDate());
        return tOnlineCourseMapper.updateTOnlineCourse(tOnlineCourse);
    }

    /**
     * 批量删除线上课程管理
     * 
     * @param courseIds 需要删除的线上课程管理主键
     * @return 结果
     */
    @Override
    public int deleteTOnlineCourseByCourseIds(Long[] courseIds)
    {
        return tOnlineCourseMapper.deleteTOnlineCourseByCourseIds(courseIds);
    }

    /**
     * 删除线上课程管理信息
     * 
     * @param courseId 线上课程管理主键
     * @return 结果
     */
    @Override
    public int deleteTOnlineCourseByCourseId(Long courseId)
    {
        return tOnlineCourseMapper.deleteTOnlineCourseByCourseId(courseId);
    }
}
