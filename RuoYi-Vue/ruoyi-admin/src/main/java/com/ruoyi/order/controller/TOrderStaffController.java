package com.ruoyi.order.controller;

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
import com.ruoyi.order.domain.TOrderStaff;
import com.ruoyi.order.service.ITOrderStaffService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 订单-服务人员关联Controller
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@RestController
@RequestMapping("/order/orderStaff")
public class TOrderStaffController extends BaseController
{
    @Autowired
    private ITOrderStaffService tOrderStaffService;

    /**
     * 查询订单-服务人员关联列表
     */
    @PreAuthorize("@ss.hasPermi('order:orderStaff:list')")
    @GetMapping("/list")
    public TableDataInfo list(TOrderStaff tOrderStaff)
    {
        startPage();
        List<TOrderStaff> list = tOrderStaffService.selectTOrderStaffList(tOrderStaff);
        return getDataTable(list);
    }

    /**
     * 导出订单-服务人员关联列表
     */
    @PreAuthorize("@ss.hasPermi('order:orderStaff:export')")
    @Log(title = "订单-服务人员关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TOrderStaff tOrderStaff)
    {
        List<TOrderStaff> list = tOrderStaffService.selectTOrderStaffList(tOrderStaff);
        ExcelUtil<TOrderStaff> util = new ExcelUtil<TOrderStaff>(TOrderStaff.class);
        util.exportExcel(response, list, "订单-服务人员关联数据");
    }

    /**
     * 获取订单-服务人员关联详细信息
     */
    @PreAuthorize("@ss.hasPermi('order:orderStaff:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tOrderStaffService.selectTOrderStaffById(id));
    }

    /**
     * 新增订单-服务人员关联
     */
    @PreAuthorize("@ss.hasPermi('order:orderStaff:add')")
    @Log(title = "订单-服务人员关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TOrderStaff tOrderStaff)
    {
        return toAjax(tOrderStaffService.insertTOrderStaff(tOrderStaff));
    }

    /**
     * 修改订单-服务人员关联
     */
    @PreAuthorize("@ss.hasPermi('order:orderStaff:edit')")
    @Log(title = "订单-服务人员关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TOrderStaff tOrderStaff)
    {
        return toAjax(tOrderStaffService.updateTOrderStaff(tOrderStaff));
    }

    /**
     * 删除订单-服务人员关联
     */
    @PreAuthorize("@ss.hasPermi('order:orderStaff:remove')")
    @Log(title = "订单-服务人员关联", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tOrderStaffService.deleteTOrderStaffByIds(ids));
    }
}
