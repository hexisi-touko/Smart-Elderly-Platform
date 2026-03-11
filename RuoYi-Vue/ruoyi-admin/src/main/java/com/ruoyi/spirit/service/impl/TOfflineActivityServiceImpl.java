package com.ruoyi.spirit.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.spirit.mapper.TOfflineActivityMapper;
import com.ruoyi.spirit.domain.TOfflineActivity;
import com.ruoyi.spirit.service.ITOfflineActivityService;

/**
 * 线下活动管理Service业务层处理
 * 
 * @author zhangTing
 * @date 2026-03-11
 */
@Service
public class TOfflineActivityServiceImpl implements ITOfflineActivityService 
{
    @Autowired
    private TOfflineActivityMapper tOfflineActivityMapper;

    /**
     * 查询线下活动管理
     * 
     * @param activityId 线下活动管理主键
     * @return 线下活动管理
     */
    @Override
    public TOfflineActivity selectTOfflineActivityByActivityId(Long activityId)
    {
        return tOfflineActivityMapper.selectTOfflineActivityByActivityId(activityId);
    }

    /**
     * 查询线下活动管理列表
     * 
     * @param tOfflineActivity 线下活动管理
     * @return 线下活动管理
     */
    @Override
    public List<TOfflineActivity> selectTOfflineActivityList(TOfflineActivity tOfflineActivity)
    {
        return tOfflineActivityMapper.selectTOfflineActivityList(tOfflineActivity);
    }

    /**
     * 新增线下活动管理
     * 
     * @param tOfflineActivity 线下活动管理
     * @return 结果
     */
    @Override
    public int insertTOfflineActivity(TOfflineActivity tOfflineActivity)
    {
        tOfflineActivity.setCreateTime(DateUtils.getNowDate());
        return tOfflineActivityMapper.insertTOfflineActivity(tOfflineActivity);
    }

    /**
     * 修改线下活动管理
     * 
     * @param tOfflineActivity 线下活动管理
     * @return 结果
     */
    @Override
    public int updateTOfflineActivity(TOfflineActivity tOfflineActivity)
    {
        tOfflineActivity.setUpdateTime(DateUtils.getNowDate());
        return tOfflineActivityMapper.updateTOfflineActivity(tOfflineActivity);
    }

    /**
     * 批量删除线下活动管理
     * 
     * @param activityIds 需要删除的线下活动管理主键
     * @return 结果
     */
    @Override
    public int deleteTOfflineActivityByActivityIds(Long[] activityIds)
    {
        return tOfflineActivityMapper.deleteTOfflineActivityByActivityIds(activityIds);
    }

    /**
     * 删除线下活动管理信息
     * 
     * @param activityId 线下活动管理主键
     * @return 结果
     */
    @Override
    public int deleteTOfflineActivityByActivityId(Long activityId)
    {
        return tOfflineActivityMapper.deleteTOfflineActivityByActivityId(activityId);
    }
}
