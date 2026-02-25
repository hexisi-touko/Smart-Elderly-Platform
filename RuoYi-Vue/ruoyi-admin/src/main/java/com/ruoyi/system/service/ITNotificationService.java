package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.TNotification;

/**
 * 消息通知管理Service接口
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public interface ITNotificationService 
{
    /**
     * 查询消息通知管理
     * 
     * @param notificationId 消息通知管理主键
     * @return 消息通知管理
     */
    public TNotification selectTNotificationByNotificationId(Long notificationId);

    /**
     * 查询消息通知管理列表
     * 
     * @param tNotification 消息通知管理
     * @return 消息通知管理集合
     */
    public List<TNotification> selectTNotificationList(TNotification tNotification);

    /**
     * 新增消息通知管理
     * 
     * @param tNotification 消息通知管理
     * @return 结果
     */
    public int insertTNotification(TNotification tNotification);

    /**
     * 修改消息通知管理
     * 
     * @param tNotification 消息通知管理
     * @return 结果
     */
    public int updateTNotification(TNotification tNotification);

    /**
     * 批量删除消息通知管理
     * 
     * @param notificationIds 需要删除的消息通知管理主键集合
     * @return 结果
     */
    public int deleteTNotificationByNotificationIds(Long[] notificationIds);

    /**
     * 删除消息通知管理信息
     * 
     * @param notificationId 消息通知管理主键
     * @return 结果
     */
    public int deleteTNotificationByNotificationId(Long notificationId);
}
