package com.ruoyi.service.controller;

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
import com.ruoyi.service.domain.TServiceStaff;
import com.ruoyi.service.service.ITServiceStaffService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 服务人员管理Controller
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@RestController
@RequestMapping("/service/serviceStaff")
public class TServiceStaffController extends BaseController
{
    @Autowired
    private ITServiceStaffService tServiceStaffService;

    /**
     * 查询服务人员管理列表
     */
    @PreAuthorize("@ss.hasPermi('service:serviceStaff:list')")
    @GetMapping("/list")
    public TableDataInfo list(TServiceStaff tServiceStaff)
    {
        startPage();
        List<TServiceStaff> list = tServiceStaffService.selectTServiceStaffList(tServiceStaff);
        return getDataTable(list);
    }

    /**
     * 导出服务人员管理列表
     */
    @PreAuthorize("@ss.hasPermi('service:serviceStaff:export')")
    @Log(title = "服务人员管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TServiceStaff tServiceStaff)
    {
        List<TServiceStaff> list = tServiceStaffService.selectTServiceStaffList(tServiceStaff);
        ExcelUtil<TServiceStaff> util = new ExcelUtil<TServiceStaff>(TServiceStaff.class);
        util.exportExcel(response, list, "服务人员管理数据");
    }

    /**
     * 获取服务人员管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('service:serviceStaff:query')")
    @GetMapping(value = "/{staffId}")
    public AjaxResult getInfo(@PathVariable("staffId") Long staffId)
    {
        return success(tServiceStaffService.selectTServiceStaffByStaffId(staffId));
    }

    /**
     * 新增服务人员管理
     */
    @PreAuthorize("@ss.hasPermi('service:serviceStaff:add')")
    @Log(title = "服务人员管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TServiceStaff tServiceStaff)
    {
        return toAjax(tServiceStaffService.insertTServiceStaff(tServiceStaff));
    }

    /**
     * 修改服务人员管理
     */
    @PreAuthorize("@ss.hasPermi('service:serviceStaff:edit')")
    @Log(title = "服务人员管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TServiceStaff tServiceStaff)
    {
        return toAjax(tServiceStaffService.updateTServiceStaff(tServiceStaff));
    }

    /**
     * 删除服务人员管理
     */
    @PreAuthorize("@ss.hasPermi('service:serviceStaff:remove')")
    @Log(title = "服务人员管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{staffIds}")
    public AjaxResult remove(@PathVariable Long[] staffIds)
    {
        return toAjax(tServiceStaffService.deleteTServiceStaffByStaffIds(staffIds));
    }
}
