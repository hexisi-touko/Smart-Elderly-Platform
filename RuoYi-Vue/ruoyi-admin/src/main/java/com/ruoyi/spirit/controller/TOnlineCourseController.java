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
import com.ruoyi.spirit.domain.TOnlineCourse;
import com.ruoyi.spirit.service.ITOnlineCourseService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 线上课程管理Controller
 * 
 * @author zhangTing
 * @date 2026-03-11
 */
@RestController
@RequestMapping("/spirit/onlineCourse")
public class TOnlineCourseController extends BaseController
{
    @Autowired
    private ITOnlineCourseService tOnlineCourseService;

    /**
     * 查询线上课程管理列表
     */
    @PreAuthorize("@ss.hasPermi('spirit:onlineCourse:list')")
    @GetMapping("/list")
    public TableDataInfo list(TOnlineCourse tOnlineCourse)
    {
        startPage();
        List<TOnlineCourse> list = tOnlineCourseService.selectTOnlineCourseList(tOnlineCourse);
        return getDataTable(list);
    }

    /**
     * 导出线上课程管理列表
     */
    @PreAuthorize("@ss.hasPermi('spirit:onlineCourse:export')")
    @Log(title = "线上课程管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TOnlineCourse tOnlineCourse)
    {
        List<TOnlineCourse> list = tOnlineCourseService.selectTOnlineCourseList(tOnlineCourse);
        ExcelUtil<TOnlineCourse> util = new ExcelUtil<TOnlineCourse>(TOnlineCourse.class);
        util.exportExcel(response, list, "线上课程管理数据");
    }

    /**
     * 获取线上课程管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('spirit:onlineCourse:query')")
    @GetMapping(value = "/{courseId}")
    public AjaxResult getInfo(@PathVariable("courseId") Long courseId)
    {
        return success(tOnlineCourseService.selectTOnlineCourseByCourseId(courseId));
    }

    /**
     * 新增线上课程管理
     */
    @PreAuthorize("@ss.hasPermi('spirit:onlineCourse:add')")
    @Log(title = "线上课程管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TOnlineCourse tOnlineCourse)
    {
        return toAjax(tOnlineCourseService.insertTOnlineCourse(tOnlineCourse));
    }

    /**
     * 修改线上课程管理
     */
    @PreAuthorize("@ss.hasPermi('spirit:onlineCourse:edit')")
    @Log(title = "线上课程管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TOnlineCourse tOnlineCourse)
    {
        return toAjax(tOnlineCourseService.updateTOnlineCourse(tOnlineCourse));
    }

    /**
     * 删除线上课程管理
     */
    @PreAuthorize("@ss.hasPermi('spirit:onlineCourse:remove')")
    @Log(title = "线上课程管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{courseIds}")
    public AjaxResult remove(@PathVariable Long[] courseIds)
    {
        return toAjax(tOnlineCourseService.deleteTOnlineCourseByCourseIds(courseIds));
    }
}
