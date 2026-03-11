package com.ruoyi.spirit.mapper;

import java.util.List;
import com.ruoyi.spirit.domain.TElderlyCourse;

/**
 * 老人-课程报名Mapper接口
 * 
 * @author zhangTing
 * @date 2026-03-11
 */
public interface TElderlyCourseMapper 
{
    /**
     * 查询老人-课程报名
     * 
     * @param id 老人-课程报名主键
     * @return 老人-课程报名
     */
    public TElderlyCourse selectTElderlyCourseById(Long id);

    /**
     * 查询老人-课程报名列表
     * 
     * @param tElderlyCourse 老人-课程报名
     * @return 老人-课程报名集合
     */
    public List<TElderlyCourse> selectTElderlyCourseList(TElderlyCourse tElderlyCourse);

    /**
     * 新增老人-课程报名
     * 
     * @param tElderlyCourse 老人-课程报名
     * @return 结果
     */
    public int insertTElderlyCourse(TElderlyCourse tElderlyCourse);

    /**
     * 修改老人-课程报名
     * 
     * @param tElderlyCourse 老人-课程报名
     * @return 结果
     */
    public int updateTElderlyCourse(TElderlyCourse tElderlyCourse);

    /**
     * 删除老人-课程报名
     * 
     * @param id 老人-课程报名主键
     * @return 结果
     */
    public int deleteTElderlyCourseById(Long id);

    /**
     * 批量删除老人-课程报名
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTElderlyCourseByIds(Long[] ids);
}
