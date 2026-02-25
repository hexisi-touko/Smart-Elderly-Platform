package com.ruoyi.system.controller;

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
import com.ruoyi.system.domain.TNotification;
import com.ruoyi.system.service.ITNotificationService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 消息通知管理Controller
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@RestController
@RequestMapping("/system/notification")
public class TNotificationController extends BaseController
{
    @Autowired
    private ITNotificationService tNotificationService;

    /**
     * 查询消息通知管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:notification:list')")
    @GetMapping("/list")
    public TableDataInfo list(TNotification tNotification)
    {
        startPage();
        List<TNotification> list = tNotificationService.selectTNotificationList(tNotification);
        return getDataTable(list);
    }

    /**
     * 导出消息通知管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:notification:export')")
    @Log(title = "消息通知管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TNotification tNotification)
    {
        List<TNotification> list = tNotificationService.selectTNotificationList(tNotification);
        ExcelUtil<TNotification> util = new ExcelUtil<TNotification>(TNotification.class);
        util.exportExcel(response, list, "消息通知管理数据");
    }

    /**
     * 获取消息通知管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:notification:query')")
    @GetMapping(value = "/{notificationId}")
    public AjaxResult getInfo(@PathVariable("notificationId") Long notificationId)
    {
        return success(tNotificationService.selectTNotificationByNotificationId(notificationId));
    }

    /**
     * 新增消息通知管理
     */
    @PreAuthorize("@ss.hasPermi('system:notification:add')")
    @Log(title = "消息通知管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TNotification tNotification)
    {
        return toAjax(tNotificationService.insertTNotification(tNotification));
    }

    /**
     * 修改消息通知管理
     */
    @PreAuthorize("@ss.hasPermi('system:notification:edit')")
    @Log(title = "消息通知管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TNotification tNotification)
    {
        return toAjax(tNotificationService.updateTNotification(tNotification));
    }

    /**
     * 删除消息通知管理
     */
    @PreAuthorize("@ss.hasPermi('system:notification:remove')")
    @Log(title = "消息通知管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{notificationIds}")
    public AjaxResult remove(@PathVariable Long[] notificationIds)
    {
        return toAjax(tNotificationService.deleteTNotificationByNotificationIds(notificationIds));
    }
}
