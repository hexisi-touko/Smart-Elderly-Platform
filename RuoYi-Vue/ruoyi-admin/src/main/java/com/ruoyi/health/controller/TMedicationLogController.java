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
import com.ruoyi.health.domain.TMedicationLog;
import com.ruoyi.health.service.ITMedicationLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用药记录管理Controller
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@RestController
@RequestMapping("/health/medicationLog")
public class TMedicationLogController extends BaseController
{
    @Autowired
    private ITMedicationLogService tMedicationLogService;

    /**
     * 查询用药记录管理列表
     */
    @PreAuthorize("@ss.hasPermi('health:medicationLog:list')")
    @GetMapping("/list")
    public TableDataInfo list(TMedicationLog tMedicationLog)
    {
        startPage();
        List<TMedicationLog> list = tMedicationLogService.selectTMedicationLogList(tMedicationLog);
        return getDataTable(list);
    }

    /**
     * 导出用药记录管理列表
     */
    @PreAuthorize("@ss.hasPermi('health:medicationLog:export')")
    @Log(title = "用药记录管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TMedicationLog tMedicationLog)
    {
        List<TMedicationLog> list = tMedicationLogService.selectTMedicationLogList(tMedicationLog);
        ExcelUtil<TMedicationLog> util = new ExcelUtil<TMedicationLog>(TMedicationLog.class);
        util.exportExcel(response, list, "用药记录管理数据");
    }

    /**
     * 获取用药记录管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('health:medicationLog:query')")
    @GetMapping(value = "/{logId}")
    public AjaxResult getInfo(@PathVariable("logId") Long logId)
    {
        return success(tMedicationLogService.selectTMedicationLogByLogId(logId));
    }

    /**
     * 新增用药记录管理
     */
    @PreAuthorize("@ss.hasPermi('health:medicationLog:add')")
    @Log(title = "用药记录管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TMedicationLog tMedicationLog)
    {
        return toAjax(tMedicationLogService.insertTMedicationLog(tMedicationLog));
    }

    /**
     * 修改用药记录管理
     */
    @PreAuthorize("@ss.hasPermi('health:medicationLog:edit')")
    @Log(title = "用药记录管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TMedicationLog tMedicationLog)
    {
        return toAjax(tMedicationLogService.updateTMedicationLog(tMedicationLog));
    }

    /**
     * 删除用药记录管理
     */
    @PreAuthorize("@ss.hasPermi('health:medicationLog:remove')")
    @Log(title = "用药记录管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{logIds}")
    public AjaxResult remove(@PathVariable Long[] logIds)
    {
        return toAjax(tMedicationLogService.deleteTMedicationLogByLogIds(logIds));
    }
}
