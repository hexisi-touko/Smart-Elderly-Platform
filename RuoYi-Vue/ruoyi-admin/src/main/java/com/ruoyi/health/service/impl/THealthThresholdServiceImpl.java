package com.ruoyi.health.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.health.mapper.THealthThresholdMapper;
import com.ruoyi.health.domain.THealthThreshold;
import com.ruoyi.health.service.ITHealthThresholdService;

/**
 * 健康阈值管理Service业务层处理
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@Service
public class THealthThresholdServiceImpl implements ITHealthThresholdService 
{
    @Autowired
    private THealthThresholdMapper tHealthThresholdMapper;

    /**
     * 查询健康阈值管理
     * 
     * @param thresholdId 健康阈值管理主键
     * @return 健康阈值管理
     */
    @Override
    public THealthThreshold selectTHealthThresholdByThresholdId(Long thresholdId)
    {
        return tHealthThresholdMapper.selectTHealthThresholdByThresholdId(thresholdId);
    }

    /**
     * 查询健康阈值管理列表
     * 
     * @param tHealthThreshold 健康阈值管理
     * @return 健康阈值管理
     */
    @Override
    public List<THealthThreshold> selectTHealthThresholdList(THealthThreshold tHealthThreshold)
    {
        return tHealthThresholdMapper.selectTHealthThresholdList(tHealthThreshold);
    }

    /**
     * 新增健康阈值管理
     * 
     * @param tHealthThreshold 健康阈值管理
     * @return 结果
     */
    @Override
    public int insertTHealthThreshold(THealthThreshold tHealthThreshold)
    {
        tHealthThreshold.setCreateTime(DateUtils.getNowDate());
        return tHealthThresholdMapper.insertTHealthThreshold(tHealthThreshold);
    }

    /**
     * 修改健康阈值管理
     * 
     * @param tHealthThreshold 健康阈值管理
     * @return 结果
     */
    @Override
    public int updateTHealthThreshold(THealthThreshold tHealthThreshold)
    {
        tHealthThreshold.setUpdateTime(DateUtils.getNowDate());
        return tHealthThresholdMapper.updateTHealthThreshold(tHealthThreshold);
    }

    /**
     * 批量删除健康阈值管理
     * 
     * @param thresholdIds 需要删除的健康阈值管理主键
     * @return 结果
     */
    @Override
    public int deleteTHealthThresholdByThresholdIds(Long[] thresholdIds)
    {
        return tHealthThresholdMapper.deleteTHealthThresholdByThresholdIds(thresholdIds);
    }

    /**
     * 删除健康阈值管理信息
     * 
     * @param thresholdId 健康阈值管理主键
     * @return 结果
     */
    @Override
    public int deleteTHealthThresholdByThresholdId(Long thresholdId)
    {
        return tHealthThresholdMapper.deleteTHealthThresholdByThresholdId(thresholdId);
    }
}
