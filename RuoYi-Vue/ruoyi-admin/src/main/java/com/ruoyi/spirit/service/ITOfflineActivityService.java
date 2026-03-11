package com.ruoyi.spirit.service;

import java.util.List;
import com.ruoyi.spirit.domain.TOfflineActivity;

/**
 * 线下活动管理Service接口
 * 
 * @author zhangTing
 * @date 2026-03-11
 */
public interface ITOfflineActivityService 
{
    /**
     * 查询线下活动管理
     * 
     * @param activityId 线下活动管理主键
     * @return 线下活动管理
     */
    public TOfflineActivity selectTOfflineActivityByActivityId(Long activityId);

    /**
     * 查询线下活动管理列表
     * 
     * @param tOfflineActivity 线下活动管理
     * @return 线下活动管理集合
     */
    public List<TOfflineActivity> selectTOfflineActivityList(TOfflineActivity tOfflineActivity);

    /**
     * 新增线下活动管理
     * 
     * @param tOfflineActivity 线下活动管理
     * @return 结果
     */
    public int insertTOfflineActivity(TOfflineActivity tOfflineActivity);

    /**
     * 修改线下活动管理
     * 
     * @param tOfflineActivity 线下活动管理
     * @return 结果
     */
    public int updateTOfflineActivity(TOfflineActivity tOfflineActivity);

    /**
     * 批量删除线下活动管理
     * 
     * @param activityIds 需要删除的线下活动管理主键集合
     * @return 结果
     */
    public int deleteTOfflineActivityByActivityIds(Long[] activityIds);

    /**
     * 删除线下活动管理信息
     * 
     * @param activityId 线下活动管理主键
     * @return 结果
     */
    public int deleteTOfflineActivityByActivityId(Long activityId);
}
