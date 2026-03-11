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
import com.ruoyi.spirit.domain.TElderlyCourse;
import com.ruoyi.spirit.service.ITElderlyCourseService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 老人-课程报名Controller
 * 
 * @author zhangTing
 * @date 2026-03-11
 */
@RestController
@RequestMapping("/spirit/elderlyCourse")
public class TElderlyCourseController extends BaseController
{
    @Autowired
    private ITElderlyCourseService tElderlyCourseService;

    /**
     * 查询老人-课程报名列表
     */
    @PreAuthorize("@ss.hasPermi('spirit:elderlyCourse:list')")
    @GetMapping("/list")
    public TableDataInfo list(TElderlyCourse tElderlyCourse)
    {
        startPage();
        List<TElderlyCourse> list = tElderlyCourseService.selectTElderlyCourseList(tElderlyCourse);
        return getDataTable(list);
    }

    /**
     * 导出老人-课程报名列表
     */
    @PreAuthorize("@ss.hasPermi('spirit:elderlyCourse:export')")
    @Log(title = "老人-课程报名", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TElderlyCourse tElderlyCourse)
    {
        List<TElderlyCourse> list = tElderlyCourseService.selectTElderlyCourseList(tElderlyCourse);
        ExcelUtil<TElderlyCourse> util = new ExcelUtil<TElderlyCourse>(TElderlyCourse.class);
        util.exportExcel(response, list, "老人-课程报名数据");
    }

    /**
     * 获取老人-课程报名详细信息
     */
    @PreAuthorize("@ss.hasPermi('spirit:elderlyCourse:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tElderlyCourseService.selectTElderlyCourseById(id));
    }

    /**
     * 新增老人-课程报名
     */
    @PreAuthorize("@ss.hasPermi('spirit:elderlyCourse:add')")
    @Log(title = "老人-课程报名", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TElderlyCourse tElderlyCourse)
    {
        return toAjax(tElderlyCourseService.insertTElderlyCourse(tElderlyCourse));
    }

    /**
     * 修改老人-课程报名
     */
    @PreAuthorize("@ss.hasPermi('spirit:elderlyCourse:edit')")
    @Log(title = "老人-课程报名", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TElderlyCourse tElderlyCourse)
    {
        return toAjax(tElderlyCourseService.updateTElderlyCourse(tElderlyCourse));
    }

    /**
     * 删除老人-课程报名
     */
    @PreAuthorize("@ss.hasPermi('spirit:elderlyCourse:remove')")
    @Log(title = "老人-课程报名", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tElderlyCourseService.deleteTElderlyCourseByIds(ids));
    }
}
