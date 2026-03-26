package com.ruoyi.safety.service;

import java.util.List;
import com.ruoyi.safety.domain.TSafetyAlert;

/**
 * 安全预警管理Service接口
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public interface ITSafetyAlertService {
    /**
     * 查询安全预警管理
     * 
     * @param alertId 安全预警管理主键
     * @return 安全预警管理
     */
    public TSafetyAlert selectTSafetyAlertByAlertId(Long alertId);

    /**
     * 查询安全预警管理列表
     * 
     * @param tSafetyAlert 安全预警管理
     * @return 安全预警管理集合
     */
    public List<TSafetyAlert> selectTSafetyAlertList(TSafetyAlert tSafetyAlert);

    /**
     * 新增安全预警管理
     * 
     * @param tSafetyAlert 安全预警管理
     * @return 结果
     */
    public int insertTSafetyAlert(TSafetyAlert tSafetyAlert);

    /**
     * 修改安全预警管理
     * 
     * @param tSafetyAlert 安全预警管理
     * @return 结果
     */
    public int updateTSafetyAlert(TSafetyAlert tSafetyAlert);

    /**
     * 批量删除安全预警管理
     * 
     * @param alertIds 需要删除的安全预警管理主键集合
     * @return 结果
     */
    public int deleteTSafetyAlertByAlertIds(Long[] alertIds);

    /**
     * 删除安全预警管理信息
     * 
     * @param alertId 安全预警管理主键
     * @return 结果
     */
    public int deleteTSafetyAlertByAlertId(Long alertId);

    /**
     * App端上报安全预警
     */
    public int insertAppSafetyAlert(TSafetyAlert tSafetyAlert, Long userId);
}
