package com.ruoyi.health.service;

import java.util.List;
import com.ruoyi.health.domain.TMedicationReminder;

/**
 * 用药提醒管理Service接口
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public interface ITMedicationReminderService 
{
    /**
     * 查询用药提醒管理
     * 
     * @param reminderId 用药提醒管理主键
     * @return 用药提醒管理
     */
    public TMedicationReminder selectTMedicationReminderByReminderId(Long reminderId);

    /**
     * 查询用药提醒管理列表
     * 
     * @param tMedicationReminder 用药提醒管理
     * @return 用药提醒管理集合
     */
    public List<TMedicationReminder> selectTMedicationReminderList(TMedicationReminder tMedicationReminder);

    /**
     * 新增用药提醒管理
     * 
     * @param tMedicationReminder 用药提醒管理
     * @return 结果
     */
    public int insertTMedicationReminder(TMedicationReminder tMedicationReminder);

    /**
     * 修改用药提醒管理
     * 
     * @param tMedicationReminder 用药提醒管理
     * @return 结果
     */
    public int updateTMedicationReminder(TMedicationReminder tMedicationReminder);

    /**
     * 批量删除用药提醒管理
     * 
     * @param reminderIds 需要删除的用药提醒管理主键集合
     * @return 结果
     */
    public int deleteTMedicationReminderByReminderIds(Long[] reminderIds);

    /**
     * 删除用药提醒管理信息
     * 
     * @param reminderId 用药提醒管理主键
     * @return 结果
     */
    public int deleteTMedicationReminderByReminderId(Long reminderId);
}
