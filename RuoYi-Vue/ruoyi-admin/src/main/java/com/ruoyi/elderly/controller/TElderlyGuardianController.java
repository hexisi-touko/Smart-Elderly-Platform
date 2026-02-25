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
import com.ruoyi.elderly.domain.TElderlyGuardian;
import com.ruoyi.elderly.service.ITElderlyGuardianService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 老人-监护人关联Controller
 * 
 * @author zhangTing
 * @date 2026-02-24
 */
@RestController
@RequestMapping("/elderly/elderly_guardian")
public class TElderlyGuardianController extends BaseController
{
    @Autowired
    private ITElderlyGuardianService tElderlyGuardianService;

    /**
     * 查询老人-监护人关联列表
     */
    @PreAuthorize("@ss.hasPermi('elderly:elderly_guardian:list')")
    @GetMapping("/list")
    public TableDataInfo list(TElderlyGuardian tElderlyGuardian)
    {
        startPage();
        List<TElderlyGuardian> list = tElderlyGuardianService.selectTElderlyGuardianList(tElderlyGuardian);
        return getDataTable(list);
    }

    /**
     * 导出老人-监护人关联列表
     */
    @PreAuthorize("@ss.hasPermi('elderly:elderly_guardian:export')")
    @Log(title = "老人-监护人关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TElderlyGuardian tElderlyGuardian)
    {
        List<TElderlyGuardian> list = tElderlyGuardianService.selectTElderlyGuardianList(tElderlyGuardian);
        ExcelUtil<TElderlyGuardian> util = new ExcelUtil<TElderlyGuardian>(TElderlyGuardian.class);
        util.exportExcel(response, list, "老人-监护人关联数据");
    }

    /**
     * 获取老人-监护人关联详细信息
     */
    @PreAuthorize("@ss.hasPermi('elderly:elderly_guardian:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tElderlyGuardianService.selectTElderlyGuardianById(id));
    }

    /**
     * 新增老人-监护人关联
     */
    @PreAuthorize("@ss.hasPermi('elderly:elderly_guardian:add')")
    @Log(title = "老人-监护人关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TElderlyGuardian tElderlyGuardian)
    {
        return toAjax(tElderlyGuardianService.insertTElderlyGuardian(tElderlyGuardian));
    }

    /**
     * 修改老人-监护人关联
     */
    @PreAuthorize("@ss.hasPermi('elderly:elderly_guardian:edit')")
    @Log(title = "老人-监护人关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TElderlyGuardian tElderlyGuardian)
    {
        return toAjax(tElderlyGuardianService.updateTElderlyGuardian(tElderlyGuardian));
    }

    /**
     * 删除老人-监护人关联
     */
    @PreAuthorize("@ss.hasPermi('elderly:elderly_guardian:remove')")
    @Log(title = "老人-监护人关联", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tElderlyGuardianService.deleteTElderlyGuardianByIds(ids));
    }
}
