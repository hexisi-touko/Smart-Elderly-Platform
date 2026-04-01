package com.ruoyi.safety.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.elderly.domain.TElderly;
import com.ruoyi.elderly.service.ITElderlyService;
import com.ruoyi.safety.domain.TSafetyAlert;
import com.ruoyi.safety.service.ITSafetyAlertService;

/**
 * C端安全预警Controller
 * 为App端老人提供预警查询、SOS报警和预警详情查看接口
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/app/alert")
public class AppAlertController extends BaseController {

    @Autowired
    private ITSafetyAlertService tSafetyAlertService;

    @Autowired
    private ITElderlyService tElderlyService;

    /**
     * 查询当前老人的预警列表
     */
    @GetMapping("/list")
    public TableDataInfo list(TSafetyAlert tSafetyAlert) {
        Long elderlyId = getCurrentElderlyId();
        if (elderlyId == null) {
            return getDataTable(new ArrayList<TSafetyAlert>());
        }
        tSafetyAlert.setElderlyId(elderlyId);
        startPage();
        List<TSafetyAlert> alerts = tSafetyAlertService.selectTSafetyAlertList(tSafetyAlert);
        return getDataTable(alerts);
    }

    /**
     * 获取预警详细信息
     */
    @GetMapping("/{alertId}")
    public AjaxResult getInfo(@PathVariable Long alertId) {
        return success(tSafetyAlertService.selectTSafetyAlertByAlertId(alertId));
    }

    /**
     * 发出 SOS 一键报警
     * @param alert 包含经纬度等信息
     */
    @PostMapping("/sos")
    public AjaxResult sosAlert(@RequestBody TSafetyAlert alert) {
        Long userId = SecurityUtils.getUserId();

        // 设置报警基本属性
        alert.setAlertType("SOS一键呼叫");
        alert.setDeviceId(0L); // 0L 表示来源于移动端APP

        return toAjax(tSafetyAlertService.insertAppSafetyAlert(alert, userId));
    }

    /**
     * 获取当前登录用户对应的老人ID
     */
    private Long getCurrentElderlyId() {
        Long userId = SecurityUtils.getUserId();
        TElderly params = new TElderly();
        params.setUserId(userId);
        List<TElderly> list = tElderlyService.selectTElderlyList(params);
        if (list != null && !list.isEmpty()) {
            return list.get(0).getElderlyId();
        }
        return null;
    }
}
