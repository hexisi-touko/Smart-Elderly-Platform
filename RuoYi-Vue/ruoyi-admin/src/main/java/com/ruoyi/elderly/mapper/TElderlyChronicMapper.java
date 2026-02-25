package com.ruoyi.elderly.mapper;

import java.util.List;
import com.ruoyi.elderly.domain.TElderlyChronic;

/**
 * 老人慢病关联管理Mapper接口
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public interface TElderlyChronicMapper 
{
    /**
     * 查询老人慢病关联管理
     * 
     * @param id 老人慢病关联管理主键
     * @return 老人慢病关联管理
     */
    public TElderlyChronic selectTElderlyChronicById(Long id);

    /**
     * 查询老人慢病关联管理列表
     * 
     * @param tElderlyChronic 老人慢病关联管理
     * @return 老人慢病关联管理集合
     */
    public List<TElderlyChronic> selectTElderlyChronicList(TElderlyChronic tElderlyChronic);

    /**
     * 新增老人慢病关联管理
     * 
     * @param tElderlyChronic 老人慢病关联管理
     * @return 结果
     */
    public int insertTElderlyChronic(TElderlyChronic tElderlyChronic);

    /**
     * 修改老人慢病关联管理
     * 
     * @param tElderlyChronic 老人慢病关联管理
     * @return 结果
     */
    public int updateTElderlyChronic(TElderlyChronic tElderlyChronic);

    /**
     * 删除老人慢病关联管理
     * 
     * @param id 老人慢病关联管理主键
     * @return 结果
     */
    public int deleteTElderlyChronicById(Long id);

    /**
     * 批量删除老人慢病关联管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTElderlyChronicByIds(Long[] ids);
}
