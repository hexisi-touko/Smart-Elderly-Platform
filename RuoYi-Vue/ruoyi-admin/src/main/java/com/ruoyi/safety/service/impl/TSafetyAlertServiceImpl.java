package com.ruoyi.safety.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.safety.mapper.TSafetyAlertMapper;
import com.ruoyi.safety.domain.TSafetyAlert;
import com.ruoyi.safety.service.ITSafetyAlertService;

/**
 * 安全预警管理Service业务层处理
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@Service
public class TSafetyAlertServiceImpl implements ITSafetyAlertService {
    @Autowired
    private TSafetyAlertMapper tSafetyAlertMapper;

    /**
     * 查询安全预警管理
     * 
     * @param alertId 安全预警管理主键
     * @return 安全预警管理
     */
    @Override
    public TSafetyAlert selectTSafetyAlertByAlertId(Long alertId) {
        return tSafetyAlertMapper.selectTSafetyAlertByAlertId(alertId);
    }

    /**
     * 查询安全预警管理列表
     * 
     * @param tSafetyAlert 安全预警管理
     * @return 安全预警管理
     */
    @Override
    public List<TSafetyAlert> selectTSafetyAlertList(TSafetyAlert tSafetyAlert) {
        return tSafetyAlertMapper.selectTSafetyAlertList(tSafetyAlert);
    }

    /**
     * 新增安全预警管理
     * 
     * @param tSafetyAlert 安全预警管理
     * @return 结果
     */
    @Override
    public int insertTSafetyAlert(TSafetyAlert tSafetyAlert) {
        tSafetyAlert.setCreateTime(DateUtils.getNowDate());
        return tSafetyAlertMapper.insertTSafetyAlert(tSafetyAlert);
    }

    /**
     * 修改安全预警管理
     * 
     * @param tSafetyAlert 安全预警管理
     * @return 结果
     */
    @Override
    public int updateTSafetyAlert(TSafetyAlert tSafetyAlert) {
        tSafetyAlert.setUpdateTime(DateUtils.getNowDate());
        return tSafetyAlertMapper.updateTSafetyAlert(tSafetyAlert);
    }

    /**
     * 批量删除安全预警管理
     * 
     * @param alertIds 需要删除的安全预警管理主键
     * @return 结果
     */
    @Override
    public int deleteTSafetyAlertByAlertIds(Long[] alertIds) {
        return tSafetyAlertMapper.deleteTSafetyAlertByAlertIds(alertIds);
    }

    /**
     * 删除安全预警管理信息
     * 
     * @param alertId 安全预警管理主键
     * @return 结果
     */
    @Override
    public int deleteTSafetyAlertByAlertId(Long alertId) {
        return tSafetyAlertMapper.deleteTSafetyAlertByAlertId(alertId);
    }
}
