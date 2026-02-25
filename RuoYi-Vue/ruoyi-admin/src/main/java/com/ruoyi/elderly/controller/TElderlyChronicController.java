package com.ruoyi.elderly.controller;

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
import com.ruoyi.elderly.domain.TElderlyChronic;
import com.ruoyi.elderly.service.ITElderlyChronicService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 老人慢病关联管理Controller
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@RestController
@RequestMapping("/elderly/elderlyChronic")
public class TElderlyChronicController extends BaseController
{
    @Autowired
    private ITElderlyChronicService tElderlyChronicService;

    /**
     * 查询老人慢病关联管理列表
     */
    @PreAuthorize("@ss.hasPermi('elderly:elderlyChronic:list')")
    @GetMapping("/list")
    public TableDataInfo list(TElderlyChronic tElderlyChronic)
    {
        startPage();
        List<TElderlyChronic> list = tElderlyChronicService.selectTElderlyChronicList(tElderlyChronic);
        return getDataTable(list);
    }

    /**
     * 导出老人慢病关联管理列表
     */
    @PreAuthorize("@ss.hasPermi('elderly:elderlyChronic:export')")
    @Log(title = "老人慢病关联管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TElderlyChronic tElderlyChronic)
    {
        List<TElderlyChronic> list = tElderlyChronicService.selectTElderlyChronicList(tElderlyChronic);
        ExcelUtil<TElderlyChronic> util = new ExcelUtil<TElderlyChronic>(TElderlyChronic.class);
        util.exportExcel(response, list, "老人慢病关联管理数据");
    }

    /**
     * 获取老人慢病关联管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('elderly:elderlyChronic:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tElderlyChronicService.selectTElderlyChronicById(id));
    }

    /**
     * 新增老人慢病关联管理
     */
    @PreAuthorize("@ss.hasPermi('elderly:elderlyChronic:add')")
    @Log(title = "老人慢病关联管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TElderlyChronic tElderlyChronic)
    {
        return toAjax(tElderlyChronicService.insertTElderlyChronic(tElderlyChronic));
    }

    /**
     * 修改老人慢病关联管理
     */
    @PreAuthorize("@ss.hasPermi('elderly:elderlyChronic:edit')")
    @Log(title = "老人慢病关联管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TElderlyChronic tElderlyChronic)
    {
        return toAjax(tElderlyChronicService.updateTElderlyChronic(tElderlyChronic));
    }

    /**
     * 删除老人慢病关联管理
     */
    @PreAuthorize("@ss.hasPermi('elderly:elderlyChronic:remove')")
    @Log(title = "老人慢病关联管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tElderlyChronicService.deleteTElderlyChronicByIds(ids));
    }
}
