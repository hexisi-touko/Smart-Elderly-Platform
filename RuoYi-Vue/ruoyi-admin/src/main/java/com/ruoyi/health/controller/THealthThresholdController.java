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
import com.ruoyi.health.domain.THealthThreshold;
import com.ruoyi.health.service.ITHealthThresholdService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 健康阈值管理Controller
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@RestController
@RequestMapping("/health/healthThreshold")
public class THealthThresholdController extends BaseController
{
    @Autowired
    private ITHealthThresholdService tHealthThresholdService;

    /**
     * 查询健康阈值管理列表
     */
    @PreAuthorize("@ss.hasPermi('health:healthThreshold:list')")
    @GetMapping("/list")
    public TableDataInfo list(THealthThreshold tHealthThreshold)
    {
        startPage();
        List<THealthThreshold> list = tHealthThresholdService.selectTHealthThresholdList(tHealthThreshold);
        return getDataTable(list);
    }

    /**
     * 导出健康阈值管理列表
     */
    @PreAuthorize("@ss.hasPermi('health:healthThreshold:export')")
    @Log(title = "健康阈值管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, THealthThreshold tHealthThreshold)
    {
        List<THealthThreshold> list = tHealthThresholdService.selectTHealthThresholdList(tHealthThreshold);
        ExcelUtil<THealthThreshold> util = new ExcelUtil<THealthThreshold>(THealthThreshold.class);
        util.exportExcel(response, list, "健康阈值管理数据");
    }

    /**
     * 获取健康阈值管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('health:healthThreshold:query')")
    @GetMapping(value = "/{thresholdId}")
    public AjaxResult getInfo(@PathVariable("thresholdId") Long thresholdId)
    {
        return success(tHealthThresholdService.selectTHealthThresholdByThresholdId(thresholdId));
    }

    /**
     * 新增健康阈值管理
     */
    @PreAuthorize("@ss.hasPermi('health:healthThreshold:add')")
    @Log(title = "健康阈值管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody THealthThreshold tHealthThreshold)
    {
        return toAjax(tHealthThresholdService.insertTHealthThreshold(tHealthThreshold));
    }

    /**
     * 修改健康阈值管理
     */
    @PreAuthorize("@ss.hasPermi('health:healthThreshold:edit')")
    @Log(title = "健康阈值管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody THealthThreshold tHealthThreshold)
    {
        return toAjax(tHealthThresholdService.updateTHealthThreshold(tHealthThreshold));
    }

    /**
     * 删除健康阈值管理
     */
    @PreAuthorize("@ss.hasPermi('health:healthThreshold:remove')")
    @Log(title = "健康阈值管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{thresholdIds}")
    public AjaxResult remove(@PathVariable Long[] thresholdIds)
    {
        return toAjax(tHealthThresholdService.deleteTHealthThresholdByThresholdIds(thresholdIds));
    }
}
