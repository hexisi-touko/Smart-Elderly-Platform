package com.ruoyi.spirit.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.spirit.mapper.TElderlyCourseMapper;
import com.ruoyi.spirit.domain.TElderlyCourse;
import com.ruoyi.spirit.service.ITElderlyCourseService;

/**
 * 老人-课程报名Service业务层处理
 * 
 * @author zhangTing
 * @date 2026-03-11
 */
@Service
public class TElderlyCourseServiceImpl implements ITElderlyCourseService 
{
    @Autowired
    private TElderlyCourseMapper tElderlyCourseMapper;

    /**
     * 查询老人-课程报名
     * 
     * @param id 老人-课程报名主键
     * @return 老人-课程报名
     */
    @Override
    public TElderlyCourse selectTElderlyCourseById(Long id)
    {
        return tElderlyCourseMapper.selectTElderlyCourseById(id);
    }

    /**
     * 查询老人-课程报名列表
     * 
     * @param tElderlyCourse 老人-课程报名
     * @return 老人-课程报名
     */
    @Override
    public List<TElderlyCourse> selectTElderlyCourseList(TElderlyCourse tElderlyCourse)
    {
        return tElderlyCourseMapper.selectTElderlyCourseList(tElderlyCourse);
    }

    /**
     * 新增老人-课程报名
     * 
     * @param tElderlyCourse 老人-课程报名
     * @return 结果
     */
    @Override
    public int insertTElderlyCourse(TElderlyCourse tElderlyCourse)
    {
        tElderlyCourse.setCreateTime(DateUtils.getNowDate());
        return tElderlyCourseMapper.insertTElderlyCourse(tElderlyCourse);
    }

    /**
     * 修改老人-课程报名
     * 
     * @param tElderlyCourse 老人-课程报名
     * @return 结果
     */
    @Override
    public int updateTElderlyCourse(TElderlyCourse tElderlyCourse)
    {
        tElderlyCourse.setUpdateTime(DateUtils.getNowDate());
        return tElderlyCourseMapper.updateTElderlyCourse(tElderlyCourse);
    }

    /**
     * 批量删除老人-课程报名
     * 
     * @param ids 需要删除的老人-课程报名主键
     * @return 结果
     */
    @Override
    public int deleteTElderlyCourseByIds(Long[] ids)
    {
        return tElderlyCourseMapper.deleteTElderlyCourseByIds(ids);
    }

    /**
     * 删除老人-课程报名信息
     * 
     * @param id 老人-课程报名主键
     * @return 结果
     */
    @Override
    public int deleteTElderlyCourseById(Long id)
    {
        return tElderlyCourseMapper.deleteTElderlyCourseById(id);
    }
}
