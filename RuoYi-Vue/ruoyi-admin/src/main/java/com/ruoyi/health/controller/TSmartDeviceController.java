package com.ruoyi.health.controller;

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
import com.ruoyi.health.domain.TSmartDevice;
import com.ruoyi.health.service.ITSmartDeviceService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 智能设备管理Controller
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@RestController
@RequestMapping("/health/device")
public class TSmartDeviceController extends BaseController
{
    @Autowired
    private ITSmartDeviceService tSmartDeviceService;

    /**
     * 查询智能设备管理列表
     */
    @PreAuthorize("@ss.hasPermi('health:device:list')")
    @GetMapping("/list")
    public TableDataInfo list(TSmartDevice tSmartDevice)
    {
        startPage();
        List<TSmartDevice> list = tSmartDeviceService.selectTSmartDeviceList(tSmartDevice);
        return getDataTable(list);
    }

    /**
     * 导出智能设备管理列表
     */
    @PreAuthorize("@ss.hasPermi('health:device:export')")
    @Log(title = "智能设备管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TSmartDevice tSmartDevice)
    {
        List<TSmartDevice> list = tSmartDeviceService.selectTSmartDeviceList(tSmartDevice);
        ExcelUtil<TSmartDevice> util = new ExcelUtil<TSmartDevice>(TSmartDevice.class);
        util.exportExcel(response, list, "智能设备管理数据");
    }

    /**
     * 获取智能设备管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('health:device:query')")
    @GetMapping(value = "/{deviceId}")
    public AjaxResult getInfo(@PathVariable("deviceId") Long deviceId)
    {
        return success(tSmartDeviceService.selectTSmartDeviceByDeviceId(deviceId));
    }

    /**
     * 新增智能设备管理
     */
    @PreAuthorize("@ss.hasPermi('health:device:add')")
    @Log(title = "智能设备管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TSmartDevice tSmartDevice)
    {
        return toAjax(tSmartDeviceService.insertTSmartDevice(tSmartDevice));
    }

    /**
     * 修改智能设备管理
     */
    @PreAuthorize("@ss.hasPermi('health:device:edit')")
    @Log(title = "智能设备管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TSmartDevice tSmartDevice)
    {
        return toAjax(tSmartDeviceService.updateTSmartDevice(tSmartDevice));
    }

    /**
     * 删除智能设备管理
     */
    @PreAuthorize("@ss.hasPermi('health:device:remove')")
    @Log(title = "智能设备管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{deviceIds}")
    public AjaxResult remove(@PathVariable Long[] deviceIds)
    {
        return toAjax(tSmartDeviceService.deleteTSmartDeviceByDeviceIds(deviceIds));
    }
}
