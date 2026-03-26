package com.ruoyi.safety.controller;

import java.util.List;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.safety.domain.TSafetyAlert;
import com.ruoyi.safety.service.ITSafetyAlertService;
import com.ruoyi.elderly.domain.TElderly;
import com.ruoyi.elderly.mapper.TElderlyMapper;
import com.ruoyi.elderly.domain.TGuardian;
import com.ruoyi.elderly.mapper.TGuardianMapper;

/**
 * C端安全预警Controller
 */
@RestController
@RequestMapping("/app/alert")
public class AppAlertController extends BaseController {

    @Autowired
    private ITSafetyAlertService tSafetyAlertService;

    @Autowired
    private TElderlyMapper tElderlyMapper;

    @Autowired
    private TGuardianMapper tGuardianMapper;

    /**
     * 免权限发出 SOS 一键报警
     */
    @PostMapping("/sos")
    public AjaxResult sosAlert() {
        Long userId = SecurityUtils.getUserId();

        // 查询当前登录用户的 elderlyId
        TElderly queryElderly = new TElderly();
        queryElderly.setUserId(userId);
        List<TElderly> elderlyList = tElderlyMapper.selectTElderlyList(queryElderly);

        Long finalElderlyId = null;

        if (elderlyList != null && !elderlyList.isEmpty()) {
            finalElderlyId = elderlyList.get(0).getElderlyId();
        } else {
            // 监护人代客触发 SOS
            TGuardian queryGuardian = new TGuardian();
            queryGuardian.setUserId(userId);
            List<TGuardian> guardianList = tGuardianMapper.selectTGuardianList(queryGuardian);
            if (guardianList != null && !guardianList.isEmpty()) {
                // 简化逻辑：这里应该指定为哪个老人报警，为简化先报错或者找关联的第一个
                throw new ServiceException("监护人SOS逻辑暂缓：当前仅支持老人端一键报警");
            } else {
                throw new ServiceException("报警失败：未找到有效身份信息");
            }
        }

        TSafetyAlert alert = new TSafetyAlert();
        alert.setElderlyId(finalElderlyId);
        alert.setAlertType("SOS一键呼叫");
        alert.setAlertTime(DateUtils.getNowDate());
        alert.setAlertStatus(0L); // 0表示未处理
        alert.setDeviceId(0L); // 0L 表示来源于移动端APP，非实体物联网设备
        alert.setAlertLng(new BigDecimal("0.0"));
        alert.setAlertLat(new BigDecimal("0.0"));
        alert.setAlertAddress("移动端一键呼救位置");

        return toAjax(tSafetyAlertService.insertTSafetyAlert(alert));
    }
}
