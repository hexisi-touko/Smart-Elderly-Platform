package com.ruoyi.health.service;

import java.util.List;
import com.ruoyi.health.domain.TMedicationLog;

/**
 * 用药记录管理Service接口
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public interface ITMedicationLogService 
{
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
     * 批量删除用药记录管理
     * 
     * @param logIds 需要删除的用药记录管理主键集合
     * @return 结果
     */
    public int deleteTMedicationLogByLogIds(Long[] logIds);

    /**
     * 删除用药记录管理信息
     * 
     * @param logId 用药记录管理主键
     * @return 结果
     */
    public int deleteTMedicationLogByLogId(Long logId);
}
