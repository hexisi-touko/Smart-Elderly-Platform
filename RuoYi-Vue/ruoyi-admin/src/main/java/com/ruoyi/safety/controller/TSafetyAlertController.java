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
import com.ruoyi.safety.domain.TSafetyAlert;
import com.ruoyi.safety.service.ITSafetyAlertService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 安全预警管理Controller
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@RestController
@RequestMapping("/safety/alert")
public class TSafetyAlertController extends BaseController
{
    @Autowired
    private ITSafetyAlertService tSafetyAlertService;

    /**
     * 查询安全预警管理列表
     */
    @PreAuthorize("@ss.hasPermi('safety:alert:list')")
    @GetMapping("/list")
    public TableDataInfo list(TSafetyAlert tSafetyAlert)
    {
        startPage();
        List<TSafetyAlert> list = tSafetyAlertService.selectTSafetyAlertList(tSafetyAlert);
        return getDataTable(list);
    }

    /**
     * 导出安全预警管理列表
     */
    @PreAuthorize("@ss.hasPermi('safety:alert:export')")
    @Log(title = "安全预警管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TSafetyAlert tSafetyAlert)
    {
        List<TSafetyAlert> list = tSafetyAlertService.selectTSafetyAlertList(tSafetyAlert);
        ExcelUtil<TSafetyAlert> util = new ExcelUtil<TSafetyAlert>(TSafetyAlert.class);
        util.exportExcel(response, list, "安全预警管理数据");
    }

    /**
     * 获取安全预警管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('safety:alert:query')")
    @GetMapping(value = "/{alertId}")
    public AjaxResult getInfo(@PathVariable("alertId") Long alertId)
    {
        return success(tSafetyAlertService.selectTSafetyAlertByAlertId(alertId));
    }

    /**
     * 新增安全预警管理
     */
    @PreAuthorize("@ss.hasPermi('safety:alert:add')")
    @Log(title = "安全预警管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TSafetyAlert tSafetyAlert)
    {
        return toAjax(tSafetyAlertService.insertTSafetyAlert(tSafetyAlert));
    }

    /**
     * 修改安全预警管理
     */
    @PreAuthorize("@ss.hasPermi('safety:alert:edit')")
    @Log(title = "安全预警管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TSafetyAlert tSafetyAlert)
    {
        return toAjax(tSafetyAlertService.updateTSafetyAlert(tSafetyAlert));
    }

    /**
     * 删除安全预警管理
     */
    @PreAuthorize("@ss.hasPermi('safety:alert:remove')")
    @Log(title = "安全预警管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{alertIds}")
    public AjaxResult remove(@PathVariable Long[] alertIds)
    {
        return toAjax(tSafetyAlertService.deleteTSafetyAlertByAlertIds(alertIds));
    }
}
