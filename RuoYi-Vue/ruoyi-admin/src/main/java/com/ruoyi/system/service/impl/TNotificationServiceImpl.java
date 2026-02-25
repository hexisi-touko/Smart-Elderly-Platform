package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TNotificationMapper;
import com.ruoyi.system.domain.TNotification;
import com.ruoyi.system.service.ITNotificationService;

/**
 * 消息通知管理Service业务层处理
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@Service
public class TNotificationServiceImpl implements ITNotificationService 
{
    @Autowired
    private TNotificationMapper tNotificationMapper;

    /**
     * 查询消息通知管理
     * 
     * @param notificationId 消息通知管理主键
     * @return 消息通知管理
     */
    @Override
    public TNotification selectTNotificationByNotificationId(Long notificationId)
    {
        return tNotificationMapper.selectTNotificationByNotificationId(notificationId);
    }

    /**
     * 查询消息通知管理列表
     * 
     * @param tNotification 消息通知管理
     * @return 消息通知管理
     */
    @Override
    public List<TNotification> selectTNotificationList(TNotification tNotification)
    {
        return tNotificationMapper.selectTNotificationList(tNotification);
    }

    /**
     * 新增消息通知管理
     * 
     * @param tNotification 消息通知管理
     * @return 结果
     */
    @Override
    public int insertTNotification(TNotification tNotification)
    {
        tNotification.setCreateTime(DateUtils.getNowDate());
        return tNotificationMapper.insertTNotification(tNotification);
    }

    /**
     * 修改消息通知管理
     * 
     * @param tNotification 消息通知管理
     * @return 结果
     */
    @Override
    public int updateTNotification(TNotification tNotification)
    {
        tNotification.setUpdateTime(DateUtils.getNowDate());
        return tNotificationMapper.updateTNotification(tNotification);
    }

    /**
     * 批量删除消息通知管理
     * 
     * @param notificationIds 需要删除的消息通知管理主键
     * @return 结果
     */
    @Override
    public int deleteTNotificationByNotificationIds(Long[] notificationIds)
    {
        return tNotificationMapper.deleteTNotificationByNotificationIds(notificationIds);
    }

    /**
     * 删除消息通知管理信息
     * 
     * @param notificationId 消息通知管理主键
     * @return 结果
     */
    @Override
    public int deleteTNotificationByNotificationId(Long notificationId)
    {
        return tNotificationMapper.deleteTNotificationByNotificationId(notificationId);
    }
}
