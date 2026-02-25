package com.ruoyi.safety.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.safety.domain.TSafetyZone;
import com.ruoyi.safety.service.ITSafetyZoneService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 安全区域/地理围栏Controller
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@RestController
@RequestMapping("/safety/zone")
public class TSafetyZoneController extends BaseController
{
    @Autowired
    private ITSafetyZoneService tSafetyZoneService;

    /**
     * 查询安全区域/地理围栏列表
     */
    @PreAuthorize("@ss.hasPermi('safety:zone:list')")
    @GetMapping("/list")
    public TableDataInfo list(TSafetyZone tSafetyZone)
    {
        startPage();
        List<TSafetyZone> list = tSafetyZoneService.selectTSafetyZoneList(tSafetyZone);
        return getDataTable(list);
    }

    /**
     * 导出安全区域/地理围栏列表
     */
    @PreAuthorize("@ss.hasPermi('safety:zone:export')")
    @Log(title = "安全区域/地理围栏", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TSafetyZone tSafetyZone)
    {
        List<TSafetyZone> list = tSafetyZoneService.selectTSafetyZoneList(tSafetyZone);
        ExcelUtil<TSafetyZone> util = new ExcelUtil<TSafetyZone>(TSafetyZone.class);
        util.exportExcel(response, list, "安全区域/地理围栏数据");
    }

    /**
     * 获取安全区域/地理围栏详细信息
     */
    @PreAuthorize("@ss.hasPermi('safety:zone:query')")
    @GetMapping(value = "/{zoneId}")
    public AjaxResult getInfo(@PathVariable("zoneId") Long zoneId)
    {
        return success(tSafetyZoneService.selectTSafetyZoneByZoneId(zoneId));
    }

    /**
     * 新增安全区域/地理围栏
     */
    @PreAuthorize("@ss.hasPermi('safety:zone:add')")
    @Log(title = "安全区域/地理围栏", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TSafetyZone tSafetyZone)
    {
        return toAjax(tSafetyZoneService.insertTSafetyZone(tSafetyZone));
    }

    /**
     * 修改安全区域/地理围栏
     */
    @PreAuthorize("@ss.hasPermi('safety:zone:edit')")
    @Log(title = "安全区域/地理围栏", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TSafetyZone tSafetyZone)
    {
        return toAjax(tSafetyZoneService.updateTSafetyZone(tSafetyZone));
    }

    /**
     * 删除安全区域/地理围栏
     */
    @PreAuthorize("@ss.hasPermi('safety:zone:remove')")
    @Log(title = "安全区域/地理围栏", businessType = BusinessType.DELETE)
	@DeleteMapping("/{zoneIds}")
    public AjaxResult remove(@PathVariable Long[] zoneIds)
    {
        return toAjax(tSafetyZoneService.deleteTSafetyZoneByZoneIds(zoneIds));
    }
}
