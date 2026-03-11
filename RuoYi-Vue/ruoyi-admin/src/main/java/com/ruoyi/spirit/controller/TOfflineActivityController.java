package com.ruoyi.spirit.controller;

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
import com.ruoyi.spirit.domain.TOfflineActivity;
import com.ruoyi.spirit.service.ITOfflineActivityService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 线下活动管理Controller
 * 
 * @author zhangTing
 * @date 2026-03-11
 */
@RestController
@RequestMapping("/spirit/offlineActivity")
public class TOfflineActivityController extends BaseController
{
    @Autowired
    private ITOfflineActivityService tOfflineActivityService;

    /**
     * 查询线下活动管理列表
     */
    @PreAuthorize("@ss.hasPermi('spirit:offlineActivity:list')")
    @GetMapping("/list")
    public TableDataInfo list(TOfflineActivity tOfflineActivity)
    {
        startPage();
        List<TOfflineActivity> list = tOfflineActivityService.selectTOfflineActivityList(tOfflineActivity);
        return getDataTable(list);
    }

    /**
     * 导出线下活动管理列表
     */
    @PreAuthorize("@ss.hasPermi('spirit:offlineActivity:export')")
    @Log(title = "线下活动管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TOfflineActivity tOfflineActivity)
    {
        List<TOfflineActivity> list = tOfflineActivityService.selectTOfflineActivityList(tOfflineActivity);
        ExcelUtil<TOfflineActivity> util = new ExcelUtil<TOfflineActivity>(TOfflineActivity.class);
        util.exportExcel(response, list, "线下活动管理数据");
    }

    /**
     * 获取线下活动管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('spirit:offlineActivity:query')")
    @GetMapping(value = "/{activityId}")
    public AjaxResult getInfo(@PathVariable("activityId") Long activityId)
    {
        return success(tOfflineActivityService.selectTOfflineActivityByActivityId(activityId));
    }

    /**
     * 新增线下活动管理
     */
    @PreAuthorize("@ss.hasPermi('spirit:offlineActivity:add')")
    @Log(title = "线下活动管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TOfflineActivity tOfflineActivity)
    {
        return toAjax(tOfflineActivityService.insertTOfflineActivity(tOfflineActivity));
    }

    /**
     * 修改线下活动管理
     */
    @PreAuthorize("@ss.hasPermi('spirit:offlineActivity:edit')")
    @Log(title = "线下活动管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TOfflineActivity tOfflineActivity)
    {
        return toAjax(tOfflineActivityService.updateTOfflineActivity(tOfflineActivity));
    }

    /**
     * 删除线下活动管理
     */
    @PreAuthorize("@ss.hasPermi('spirit:offlineActivity:remove')")
    @Log(title = "线下活动管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{activityIds}")
    public AjaxResult remove(@PathVariable Long[] activityIds)
    {
        return toAjax(tOfflineActivityService.deleteTOfflineActivityByActivityIds(activityIds));
    }
}
