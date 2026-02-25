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
import com.ruoyi.elderly.domain.TGuardian;
import com.ruoyi.elderly.service.ITGuardianService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 监护人信息Controller
 * 
 * @author zhangTing
 * @date 2026-02-24
 */
@RestController
@RequestMapping("/elderly/guardian")
public class TGuardianController extends BaseController
{
    @Autowired
    private ITGuardianService tGuardianService;

    /**
     * 查询监护人信息列表
     */
    @PreAuthorize("@ss.hasPermi('elderly:guardian:list')")
    @GetMapping("/list")
    public TableDataInfo list(TGuardian tGuardian)
    {
        startPage();
        List<TGuardian> list = tGuardianService.selectTGuardianList(tGuardian);
        return getDataTable(list);
    }

    /**
     * 导出监护人信息列表
     */
    @PreAuthorize("@ss.hasPermi('elderly:guardian:export')")
    @Log(title = "监护人信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TGuardian tGuardian)
    {
        List<TGuardian> list = tGuardianService.selectTGuardianList(tGuardian);
        ExcelUtil<TGuardian> util = new ExcelUtil<TGuardian>(TGuardian.class);
        util.exportExcel(response, list, "监护人信息数据");
    }

    /**
     * 获取监护人信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('elderly:guardian:query')")
    @GetMapping(value = "/{guardianId}")
    public AjaxResult getInfo(@PathVariable("guardianId") Long guardianId)
    {
        return success(tGuardianService.selectTGuardianByGuardianId(guardianId));
    }

    /**
     * 新增监护人信息
     */
    @PreAuthorize("@ss.hasPermi('elderly:guardian:add')")
    @Log(title = "监护人信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TGuardian tGuardian)
    {
        return toAjax(tGuardianService.insertTGuardian(tGuardian));
    }

    /**
     * 修改监护人信息
     */
    @PreAuthorize("@ss.hasPermi('elderly:guardian:edit')")
    @Log(title = "监护人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TGuardian tGuardian)
    {
        return toAjax(tGuardianService.updateTGuardian(tGuardian));
    }

    /**
     * 删除监护人信息
     */
    @PreAuthorize("@ss.hasPermi('elderly:guardian:remove')")
    @Log(title = "监护人信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{guardianIds}")
    public AjaxResult remove(@PathVariable Long[] guardianIds)
    {
        return toAjax(tGuardianService.deleteTGuardianByGuardianIds(guardianIds));
    }
}
