package com.ruoyi.elderly.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.elderly.mapper.TElderlyMapper;
import com.ruoyi.elderly.domain.TElderly;
import com.ruoyi.elderly.service.ITElderlyService;

/**
 * 老人基础信息Service业务层处理
 * 
 * @author zhangTing
 * @date 2026-02-24
 */
@Service
public class TElderlyServiceImpl implements ITElderlyService 
{
    @Autowired
    private TElderlyMapper tElderlyMapper;

    /**
     * 查询老人基础信息
     * 
     * @param elderlyId 老人基础信息主键
     * @return 老人基础信息
     */
    @Override
    public TElderly selectTElderlyByElderlyId(Long elderlyId)
    {
        return tElderlyMapper.selectTElderlyByElderlyId(elderlyId);
    }

    /**
     * 查询老人基础信息列表
     * 
     * @param tElderly 老人基础信息
     * @return 老人基础信息
     */
    @Override
    public List<TElderly> selectTElderlyList(TElderly tElderly)
    {
        return tElderlyMapper.selectTElderlyList(tElderly);
    }

    /**
     * 新增老人基础信息
     * 
     * @param tElderly 老人基础信息
     * @return 结果
     */
    @Override
    public int insertTElderly(TElderly tElderly)
    {
        tElderly.setCreateTime(DateUtils.getNowDate());
        return tElderlyMapper.insertTElderly(tElderly);
    }

    /**
     * 修改老人基础信息
     * 
     * @param tElderly 老人基础信息
     * @return 结果
     */
    @Override
    public int updateTElderly(TElderly tElderly)
    {
        tElderly.setUpdateTime(DateUtils.getNowDate());
        return tElderlyMapper.updateTElderly(tElderly);
    }

    /**
     * 批量删除老人基础信息
     * 
     * @param elderlyIds 需要删除的老人基础信息主键
     * @return 结果
     */
    @Override
    public int deleteTElderlyByElderlyIds(Long[] elderlyIds)
    {
        return tElderlyMapper.deleteTElderlyByElderlyIds(elderlyIds);
    }

    /**
     * 删除老人基础信息信息
     * 
     * @param elderlyId 老人基础信息主键
     * @return 结果
     */
    @Override
    public int deleteTElderlyByElderlyId(Long elderlyId)
    {
        return tElderlyMapper.deleteTElderlyByElderlyId(elderlyId);
    }
}
