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
import com.ruoyi.health.domain.TMedicationReminder;
import com.ruoyi.health.service.ITMedicationReminderService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用药提醒管理Controller
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@RestController
@RequestMapping("/health/medicationReminder")
public class TMedicationReminderController extends BaseController
{
    @Autowired
    private ITMedicationReminderService tMedicationReminderService;

    /**
     * 查询用药提醒管理列表
     */
    @PreAuthorize("@ss.hasPermi('health:medicationReminder:list')")
    @GetMapping("/list")
    public TableDataInfo list(TMedicationReminder tMedicationReminder)
    {
        startPage();
        List<TMedicationReminder> list = tMedicationReminderService.selectTMedicationReminderList(tMedicationReminder);
        return getDataTable(list);
    }

    /**
     * 导出用药提醒管理列表
     */
    @PreAuthorize("@ss.hasPermi('health:medicationReminder:export')")
    @Log(title = "用药提醒管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TMedicationReminder tMedicationReminder)
    {
        List<TMedicationReminder> list = tMedicationReminderService.selectTMedicationReminderList(tMedicationReminder);
        ExcelUtil<TMedicationReminder> util = new ExcelUtil<TMedicationReminder>(TMedicationReminder.class);
        util.exportExcel(response, list, "用药提醒管理数据");
    }

    /**
     * 获取用药提醒管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('health:medicationReminder:query')")
    @GetMapping(value = "/{reminderId}")
    public AjaxResult getInfo(@PathVariable("reminderId") Long reminderId)
    {
        return success(tMedicationReminderService.selectTMedicationReminderByReminderId(reminderId));
    }

    /**
     * 新增用药提醒管理
     */
    @PreAuthorize("@ss.hasPermi('health:medicationReminder:add')")
    @Log(title = "用药提醒管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TMedicationReminder tMedicationReminder)
    {
        return toAjax(tMedicationReminderService.insertTMedicationReminder(tMedicationReminder));
    }

    /**
     * 修改用药提醒管理
     */
    @PreAuthorize("@ss.hasPermi('health:medicationReminder:edit')")
    @Log(title = "用药提醒管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TMedicationReminder tMedicationReminder)
    {
        return toAjax(tMedicationReminderService.updateTMedicationReminder(tMedicationReminder));
    }

    /**
     * 删除用药提醒管理
     */
    @PreAuthorize("@ss.hasPermi('health:medicationReminder:remove')")
    @Log(title = "用药提醒管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{reminderIds}")
    public AjaxResult remove(@PathVariable Long[] reminderIds)
    {
        return toAjax(tMedicationReminderService.deleteTMedicationReminderByReminderIds(reminderIds));
    }
}
