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
import com.ruoyi.spirit.domain.TActivityEnrollment;
import com.ruoyi.spirit.service.ITActivityEnrollmentService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 活动报名管理Controller
 * 
 * @author zhangTing
 * @date 2026-03-11
 */
@RestController
@RequestMapping("/spirit/activityEnrollment")
public class TActivityEnrollmentController extends BaseController
{
    @Autowired
    private ITActivityEnrollmentService tActivityEnrollmentService;

    /**
     * 查询活动报名管理列表
     */
    @PreAuthorize("@ss.hasPermi('spirit:activityEnrollment:list')")
    @GetMapping("/list")
    public TableDataInfo list(TActivityEnrollment tActivityEnrollment)
    {
        startPage();
        List<TActivityEnrollment> list = tActivityEnrollmentService.selectTActivityEnrollmentList(tActivityEnrollment);
        return getDataTable(list);
    }

    /**
     * 导出活动报名管理列表
     */
    @PreAuthorize("@ss.hasPermi('spirit:activityEnrollment:export')")
    @Log(title = "活动报名管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TActivityEnrollment tActivityEnrollment)
    {
        List<TActivityEnrollment> list = tActivityEnrollmentService.selectTActivityEnrollmentList(tActivityEnrollment);
        ExcelUtil<TActivityEnrollment> util = new ExcelUtil<TActivityEnrollment>(TActivityEnrollment.class);
        util.exportExcel(response, list, "活动报名管理数据");
    }

    /**
     * 获取活动报名管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('spirit:activityEnrollment:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tActivityEnrollmentService.selectTActivityEnrollmentById(id));
    }

    /**
     * 新增活动报名管理
     */
    @PreAuthorize("@ss.hasPermi('spirit:activityEnrollment:add')")
    @Log(title = "活动报名管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TActivityEnrollment tActivityEnrollment)
    {
        return toAjax(tActivityEnrollmentService.insertTActivityEnrollment(tActivityEnrollment));
    }

    /**
     * 修改活动报名管理
     */
    @PreAuthorize("@ss.hasPermi('spirit:activityEnrollment:edit')")
    @Log(title = "活动报名管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TActivityEnrollment tActivityEnrollment)
    {
        return toAjax(tActivityEnrollmentService.updateTActivityEnrollment(tActivityEnrollment));
    }

    /**
     * 删除活动报名管理
     */
    @PreAuthorize("@ss.hasPermi('spirit:activityEnrollment:remove')")
    @Log(title = "活动报名管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tActivityEnrollmentService.deleteTActivityEnrollmentByIds(ids));
    }
}
