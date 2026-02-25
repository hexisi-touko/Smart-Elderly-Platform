package com.ruoyi.health.service;

import java.util.List;
import com.ruoyi.health.domain.THealthThreshold;

/**
 * 健康阈值管理Service接口
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public interface ITHealthThresholdService 
{
    /**
     * 查询健康阈值管理
     * 
     * @param thresholdId 健康阈值管理主键
     * @return 健康阈值管理
     */
    public THealthThreshold selectTHealthThresholdByThresholdId(Long thresholdId);

    /**
     * 查询健康阈值管理列表
     * 
     * @param tHealthThreshold 健康阈值管理
     * @return 健康阈值管理集合
     */
    public List<THealthThreshold> selectTHealthThresholdList(THealthThreshold tHealthThreshold);

    /**
     * 新增健康阈值管理
     * 
     * @param tHealthThreshold 健康阈值管理
     * @return 结果
     */
    public int insertTHealthThreshold(THealthThreshold tHealthThreshold);

    /**
     * 修改健康阈值管理
     * 
     * @param tHealthThreshold 健康阈值管理
     * @return 结果
     */
    public int updateTHealthThreshold(THealthThreshold tHealthThreshold);

    /**
     * 批量删除健康阈值管理
     * 
     * @param thresholdIds 需要删除的健康阈值管理主键集合
     * @return 结果
     */
    public int deleteTHealthThresholdByThresholdIds(Long[] thresholdIds);

    /**
     * 删除健康阈值管理信息
     * 
     * @param thresholdId 健康阈值管理主键
     * @return 结果
     */
    public int deleteTHealthThresholdByThresholdId(Long thresholdId);
}
