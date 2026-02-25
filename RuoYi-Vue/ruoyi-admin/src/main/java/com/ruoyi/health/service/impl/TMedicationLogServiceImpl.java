package com.ruoyi.health.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.health.mapper.TMedicationLogMapper;
import com.ruoyi.health.domain.TMedicationLog;
import com.ruoyi.health.service.ITMedicationLogService;

/**
 * 用药记录管理Service业务层处理
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@Service
public class TMedicationLogServiceImpl implements ITMedicationLogService 
{
    @Autowired
    private TMedicationLogMapper tMedicationLogMapper;

    /**
     * 查询用药记录管理
     * 
     * @param logId 用药记录管理主键
     * @return 用药记录管理
     */
    @Override
    public TMedicationLog selectTMedicationLogByLogId(Long logId)
    {
        return tMedicationLogMapper.selectTMedicationLogByLogId(logId);
    }

    /**
     * 查询用药记录管理列表
     * 
     * @param tMedicationLog 用药记录管理
     * @return 用药记录管理
     */
    @Override
    public List<TMedicationLog> selectTMedicationLogList(TMedicationLog tMedicationLog)
    {
        return tMedicationLogMapper.selectTMedicationLogList(tMedicationLog);
    }

    /**
     * 新增用药记录管理
     * 
     * @param tMedicationLog 用药记录管理
     * @return 结果
     */
    @Override
    public int insertTMedicationLog(TMedicationLog tMedicationLog)
    {
        tMedicationLog.setCreateTime(DateUtils.getNowDate());
        return tMedicationLogMapper.insertTMedicationLog(tMedicationLog);
    }

    /**
     * 修改用药记录管理
     * 
     * @param tMedicationLog 用药记录管理
     * @return 结果
     */
    @Override
    public int updateTMedicationLog(TMedicationLog tMedicationLog)
    {
        tMedicationLog.setUpdateTime(DateUtils.getNowDate());
        return tMedicationLogMapper.updateTMedicationLog(tMedicationLog);
    }

    /**
     * 批量删除用药记录管理
     * 
     * @param logIds 需要删除的用药记录管理主键
     * @return 结果
     */
    @Override
    public int deleteTMedicationLogByLogIds(Long[] logIds)
    {
        return tMedicationLogMapper.deleteTMedicationLogByLogIds(logIds);
    }

    /**
     * 删除用药记录管理信息
     * 
     * @param logId 用药记录管理主键
     * @return 结果
     */
    @Override
    public int deleteTMedicationLogByLogId(Long logId)
    {
        return tMedicationLogMapper.deleteTMedicationLogByLogId(logId);
    }
}
