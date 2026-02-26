package com.ruoyi.health.mapper;

import java.util.List;
import com.ruoyi.health.domain.TMedicationLog;

/**
 * 用药记录管理Mapper接口
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public interface TMedicationLogMapper {
    /**
     * 查询用药记录管理
     * 
     * @param logId 用药记录管理主键
     * @return 用药记录管理
     */
    public TMedicationLog selectTMedicationLogByLogId(Long logId);

    /**
     * 查询用药记录管理列表
     * 
     * @param tMedicationLog 用药记录管理
     * @return 用药记录管理集合
     */
    public List<TMedicationLog> selectTMedicationLogList(TMedicationLog tMedicationLog);

    /**
     * 新增用药记录管理
     * 
     * @param tMedicationLog 用药记录管理
     * @return 结果
     */
    public int insertTMedicationLog(TMedicationLog tMedicationLog);

    /**
     * 修改用药记录管理
     * 
     * @param tMedicationLog 用药记录管理
     * @return 结果
     */
    public int updateTMedicationLog(TMedicationLog tMedicationLog);

    /**
     * 删除用药记录管理
     * 
     * @param logId 用药记录管理主键
     * @return 结果
     */
    public int deleteTMedicationLogByLogId(Long logId);

    /**
     * 批量删除用药记录管理
     * 
     * @param logIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTMedicationLogByLogIds(Long[] logIds);

    /**
     * 根据提醒ID查询用药记录列表（主子表关联查询）
     * 
     * @param reminderId 用药提醒主键
     * @return 用药记录列表
     */
    public List<TMedicationLog> selectTMedicationLogByReminderId(Long reminderId);
}
