package com.ruoyi.safety.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.safety.mapper.TSafetyAlertMapper;
import com.ruoyi.safety.domain.TSafetyAlert;
import com.ruoyi.safety.service.ITSafetyAlertService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.elderly.domain.TElderly;
import com.ruoyi.elderly.domain.TGuardian;
import com.ruoyi.elderly.service.ITElderlyService;
import com.ruoyi.elderly.service.ITGuardianService;
import org.springframework.transaction.annotation.Transactional;


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

    @Autowired
    private ITElderlyService tElderlyService;

    @Autowired
    private ITGuardianService tGuardianService;

    @Autowired
    private com.ruoyi.system.service.ISysNoticeService sysNoticeService;


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

    /**
     * App端上报安全预警
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertAppSafetyAlert(TSafetyAlert tSafetyAlert, Long userId) {
        // 1. 识别身份并校验
        TElderly elderlyParams = new TElderly();
        elderlyParams.setUserId(userId);
        List<TElderly> elderlyList = tElderlyService.selectTElderlyList(elderlyParams);

        String elderlyName = "老人";
        if (elderlyList != null && !elderlyList.isEmpty()) {
            tSafetyAlert.setElderlyId(elderlyList.get(0).getElderlyId());
            elderlyName = elderlyList.get(0).getName();
        } else {
            TGuardian guardianParams = new TGuardian();
            guardianParams.setUserId(userId);
            List<TGuardian> guardianList = tGuardianService.selectTGuardianList(guardianParams);
            if (guardianList != null && !guardianList.isEmpty()) {
                if (tSafetyAlert.getElderlyId() == null) {
                    throw new ServiceException("监护人上报预警失败：未指定老人ID");
                }
                TElderly e = tElderlyService.selectTElderlyByElderlyId(tSafetyAlert.getElderlyId());
                if (e != null) {
                    elderlyName = e.getName();
                }
            } else {
                throw new ServiceException("上报失败：未找到您的角色档案，请先完善身份资料");
            }
        }

        // 2. 补全预警基本信息
        tSafetyAlert.setAlertTime(DateUtils.getNowDate());
        tSafetyAlert.setAlertStatus(0L); // 0-待处理
        tSafetyAlert.setCreateTime(DateUtils.getNowDate());

        int result = tSafetyAlertMapper.insertTSafetyAlert(tSafetyAlert);

        // 3. 触发系统通知，告知监护人
        try {
            com.ruoyi.system.domain.SysNotice notice = new com.ruoyi.system.domain.SysNotice();
            notice.setNoticeTitle("【紧急】安全预警通知");
            notice.setNoticeType("1"); // 1为通知
            String alertType = tSafetyAlert.getAlertType() != null ? tSafetyAlert.getAlertType() : "安全预警";
            notice.setNoticeContent("您关联的老人【" + elderlyName + "】触发了【" + alertType + "】，请及时关注并跟进处理进度！");
            notice.setStatus("0"); // 正常
            notice.setCreateBy("系统自动发送");
            notice.setCreateTime(DateUtils.getNowDate());
            sysNoticeService.insertNotice(notice);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
