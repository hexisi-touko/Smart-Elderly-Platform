package com.ruoyi.health.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.health.mapper.TMedicationReminderMapper;
import com.ruoyi.health.domain.TMedicationReminder;
import com.ruoyi.health.service.ITMedicationReminderService;

/**
 * 用药提醒管理Service业务层处理
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@Service
public class TMedicationReminderServiceImpl implements ITMedicationReminderService 
{
    @Autowired
    private TMedicationReminderMapper tMedicationReminderMapper;

    /**
     * 查询用药提醒管理
     * 
     * @param reminderId 用药提醒管理主键
     * @return 用药提醒管理
     */
    @Override
    public TMedicationReminder selectTMedicationReminderByReminderId(Long reminderId)
    {
        return tMedicationReminderMapper.selectTMedicationReminderByReminderId(reminderId);
    }

    /**
     * 查询用药提醒管理列表
     * 
     * @param tMedicationReminder 用药提醒管理
     * @return 用药提醒管理
     */
    @Override
    public List<TMedicationReminder> selectTMedicationReminderList(TMedicationReminder tMedicationReminder)
    {
        return tMedicationReminderMapper.selectTMedicationReminderList(tMedicationReminder);
    }

    /**
     * 新增用药提醒管理
     * 
     * @param tMedicationReminder 用药提醒管理
     * @return 结果
     */
    @Override
    public int insertTMedicationReminder(TMedicationReminder tMedicationReminder)
    {
        tMedicationReminder.setCreateTime(DateUtils.getNowDate());
        return tMedicationReminderMapper.insertTMedicationReminder(tMedicationReminder);
    }

    /**
     * 修改用药提醒管理
     * 
     * @param tMedicationReminder 用药提醒管理
     * @return 结果
     */
    @Override
    public int updateTMedicationReminder(TMedicationReminder tMedicationReminder)
    {
        tMedicationReminder.setUpdateTime(DateUtils.getNowDate());
        return tMedicationReminderMapper.updateTMedicationReminder(tMedicationReminder);
    }

    /**
     * 批量删除用药提醒管理
     * 
     * @param reminderIds 需要删除的用药提醒管理主键
     * @return 结果
     */
    @Override
    public int deleteTMedicationReminderByReminderIds(Long[] reminderIds)
    {
        return tMedicationReminderMapper.deleteTMedicationReminderByReminderIds(reminderIds);
    }

    /**
     * 删除用药提醒管理信息
     * 
     * @param reminderId 用药提醒管理主键
     * @return 结果
     */
    @Override
    public int deleteTMedicationReminderByReminderId(Long reminderId)
    {
        return tMedicationReminderMapper.deleteTMedicationReminderByReminderId(reminderId);
    }
}
