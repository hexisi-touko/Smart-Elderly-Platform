package com.ruoyi.elderly.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.elderly.mapper.TElderlyChronicMapper;
import com.ruoyi.elderly.domain.TElderlyChronic;
import com.ruoyi.elderly.service.ITElderlyChronicService;

/**
 * 老人慢病关联管理Service业务层处理
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@Service
public class TElderlyChronicServiceImpl implements ITElderlyChronicService 
{
    @Autowired
    private TElderlyChronicMapper tElderlyChronicMapper;

    /**
     * 查询老人慢病关联管理
     * 
     * @param id 老人慢病关联管理主键
     * @return 老人慢病关联管理
     */
    @Override
    public TElderlyChronic selectTElderlyChronicById(Long id)
    {
        return tElderlyChronicMapper.selectTElderlyChronicById(id);
    }

    /**
     * 查询老人慢病关联管理列表
     * 
     * @param tElderlyChronic 老人慢病关联管理
     * @return 老人慢病关联管理
     */
    @Override
    public List<TElderlyChronic> selectTElderlyChronicList(TElderlyChronic tElderlyChronic)
    {
        return tElderlyChronicMapper.selectTElderlyChronicList(tElderlyChronic);
    }

    /**
     * 新增老人慢病关联管理
     * 
     * @param tElderlyChronic 老人慢病关联管理
     * @return 结果
     */
    @Override
    public int insertTElderlyChronic(TElderlyChronic tElderlyChronic)
    {
        tElderlyChronic.setCreateTime(DateUtils.getNowDate());
        return tElderlyChronicMapper.insertTElderlyChronic(tElderlyChronic);
    }

    /**
     * 修改老人慢病关联管理
     * 
     * @param tElderlyChronic 老人慢病关联管理
     * @return 结果
     */
    @Override
    public int updateTElderlyChronic(TElderlyChronic tElderlyChronic)
    {
        tElderlyChronic.setUpdateTime(DateUtils.getNowDate());
        return tElderlyChronicMapper.updateTElderlyChronic(tElderlyChronic);
    }

    /**
     * 批量删除老人慢病关联管理
     * 
     * @param ids 需要删除的老人慢病关联管理主键
     * @return 结果
     */
    @Override
    public int deleteTElderlyChronicByIds(Long[] ids)
    {
        return tElderlyChronicMapper.deleteTElderlyChronicByIds(ids);
    }

    /**
     * 删除老人慢病关联管理信息
     * 
     * @param id 老人慢病关联管理主键
     * @return 结果
     */
    @Override
    public int deleteTElderlyChronicById(Long id)
    {
        return tElderlyChronicMapper.deleteTElderlyChronicById(id);
    }
}
