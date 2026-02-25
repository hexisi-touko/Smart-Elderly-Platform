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
import com.ruoyi.elderly.domain.TElderly;
import com.ruoyi.elderly.service.ITElderlyService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 老人基础信息Controller
 * 
 * @author zhangTing
 * @date 2026-02-24
 */
@RestController
@RequestMapping("/elderly/elderly")
public class TElderlyController extends BaseController
{
    @Autowired
    private ITElderlyService tElderlyService;

    /**
     * 查询老人基础信息列表
     */
    @PreAuthorize("@ss.hasPermi('elderly:elderly:list')")
    @GetMapping("/list")
    public TableDataInfo list(TElderly tElderly)
    {
        startPage();
        List<TElderly> list = tElderlyService.selectTElderlyList(tElderly);
        return getDataTable(list);
    }

    /**
     * 导出老人基础信息列表
     */
    @PreAuthorize("@ss.hasPermi('elderly:elderly:export')")
    @Log(title = "老人基础信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TElderly tElderly)
    {
        List<TElderly> list = tElderlyService.selectTElderlyList(tElderly);
        ExcelUtil<TElderly> util = new ExcelUtil<TElderly>(TElderly.class);
        util.exportExcel(response, list, "老人基础信息数据");
    }

    /**
     * 获取老人基础信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('elderly:elderly:query')")
    @GetMapping(value = "/{elderlyId}")
    public AjaxResult getInfo(@PathVariable("elderlyId") Long elderlyId)
    {
        return success(tElderlyService.selectTElderlyByElderlyId(elderlyId));
    }

    /**
     * 新增老人基础信息
     */
    @PreAuthorize("@ss.hasPermi('elderly:elderly:add')")
    @Log(title = "老人基础信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TElderly tElderly)
    {
        return toAjax(tElderlyService.insertTElderly(tElderly));
    }

    /**
     * 修改老人基础信息
     */
    @PreAuthorize("@ss.hasPermi('elderly:elderly:edit')")
    @Log(title = "老人基础信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TElderly tElderly)
    {
        return toAjax(tElderlyService.updateTElderly(tElderly));
    }

    /**
     * 删除老人基础信息
     */
    @PreAuthorize("@ss.hasPermi('elderly:elderly:remove')")
    @Log(title = "老人基础信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{elderlyIds}")
    public AjaxResult remove(@PathVariable Long[] elderlyIds)
    {
        return toAjax(tElderlyService.deleteTElderlyByElderlyIds(elderlyIds));
    }
}
