package com.ruoyi.safety.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.safety.domain.TSafetyAlert;
import com.ruoyi.safety.service.ITSafetyAlertService;

/**
 * C端安全预警Controller
 */
@RestController
@RequestMapping("/app/alert")
public class AppAlertController extends BaseController {

    @Autowired
    private ITSafetyAlertService tSafetyAlertService;

    @Autowired
    private com.ruoyi.elderly.service.ITElderlyService tElderlyService;

    /**
     * 查询当前老人的预警列表
     */
    @org.springframework.web.bind.annotation.GetMapping("/list")
    public com.ruoyi.common.core.page.TableDataInfo list(TSafetyAlert tSafetyAlert) {
        Long userId = com.ruoyi.common.utils.SecurityUtils.getUserId();

        // 查找对应的老人ID
        com.ruoyi.elderly.domain.TElderly elderlyParams = new com.ruoyi.elderly.domain.TElderly();
        elderlyParams.setUserId(userId);
        java.util.List<com.ruoyi.elderly.domain.TElderly> list = tElderlyService.selectTElderlyList(elderlyParams);

        if (list != null && !list.isEmpty()) {
            tSafetyAlert.setElderlyId(list.get(0).getElderlyId());
        } else {
            return getDataTable(new java.util.ArrayList<TSafetyAlert>());
        }

        startPage();
        java.util.List<TSafetyAlert> alerts = tSafetyAlertService.selectTSafetyAlertList(tSafetyAlert);
        return getDataTable(alerts);
    }

    /**
     * 发出 SOS 一键报警
     * @param alert 包含经纬度等信息
     */
    @PostMapping("/sos")
    public AjaxResult sosAlert(@org.springframework.web.bind.annotation.RequestBody TSafetyAlert alert) {
        Long userId = com.ruoyi.common.utils.SecurityUtils.getUserId();

        // 设置报警基本属性
        alert.setAlertType("SOS一键呼叫");
        alert.setDeviceId(0L); // 0L 表示来源于移动端APP

        return toAjax(tSafetyAlertService.insertAppSafetyAlert(alert, userId));
    }
}
