package com.ruoyi.safety.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.elderly.domain.TElderly;
import com.ruoyi.elderly.service.ITElderlyService;
import com.ruoyi.safety.domain.TSafetyZone;
import com.ruoyi.safety.service.ITSafetyZoneService;

/**
 * C端安全区域Controller
 * 为App端老人提供安全区域的增删改查
 */
@RestController
@RequestMapping("/app/zone")
public class AppSafetyZoneController extends BaseController {

    @Autowired
    private ITSafetyZoneService tSafetyZoneService;

    @Autowired
    private ITElderlyService tElderlyService;

    /**
     * 查询当前老人的安全区域列表
     */
    @GetMapping("/list")
    public TableDataInfo list() {
        Long elderlyId = getCurrentElderlyId();
        if (elderlyId == null) {
            return getDataTable(new ArrayList<TSafetyZone>());
        }
        TSafetyZone query = new TSafetyZone();
        query.setElderlyId(elderlyId);
        startPage();
        List<TSafetyZone> list = tSafetyZoneService.selectTSafetyZoneList(query);
        return getDataTable(list);
    }

    /**
     * 获取安全区域详情
     */
    @GetMapping("/{zoneId}")
    public AjaxResult getInfo(@PathVariable Long zoneId) {
        return success(tSafetyZoneService.selectTSafetyZoneByZoneId(zoneId));
    }

    /**
     * 新增安全区域
     */
    @PostMapping
    public AjaxResult add(@RequestBody TSafetyZone zone) {
        Long elderlyId = getCurrentElderlyId();
        if (elderlyId == null) {
            return error("未找到关联老人信息");
        }
        zone.setElderlyId(elderlyId);
        zone.setIsActive(1L);
        return toAjax(tSafetyZoneService.insertTSafetyZone(zone));
    }

    /**
     * 修改安全区域
     */
    @PutMapping
    public AjaxResult edit(@RequestBody TSafetyZone zone) {
        return toAjax(tSafetyZoneService.updateTSafetyZone(zone));
    }

    /**
     * 删除安全区域
     */
    @DeleteMapping("/{zoneIds}")
    public AjaxResult remove(@PathVariable Long[] zoneIds) {
        return toAjax(tSafetyZoneService.deleteTSafetyZoneByZoneIds(zoneIds));
    }

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
