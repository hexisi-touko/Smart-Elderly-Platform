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
import com.ruoyi.order.domain.TServiceOrder;
import com.ruoyi.order.domain.TOrderStaff;
import com.ruoyi.order.service.ITServiceOrderService;
import com.ruoyi.order.service.ITOrderStaffService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 服务订单Controller
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@RestController
@RequestMapping("/order/serviceOrder")
public class TServiceOrderController extends BaseController
{
    @Autowired
    private ITServiceOrderService tServiceOrderService;

    @Autowired
    private ITOrderStaffService tOrderStaffService;

    /**
     * 查询服务订单列表
     */
    @PreAuthorize("@ss.hasPermi('order:serviceOrder:list')")
    @GetMapping("/list")
    public TableDataInfo list(TServiceOrder tServiceOrder)
    {
        startPage();
        List<TServiceOrder> list = tServiceOrderService.selectTServiceOrderList(tServiceOrder);
        return getDataTable(list);
    }

    /**
     * 导出服务订单列表
     */
    @PreAuthorize("@ss.hasPermi('order:serviceOrder:export')")
    @Log(title = "服务订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TServiceOrder tServiceOrder)
    {
        List<TServiceOrder> list = tServiceOrderService.selectTServiceOrderList(tServiceOrder);
        ExcelUtil<TServiceOrder> util = new ExcelUtil<TServiceOrder>(TServiceOrder.class);
        util.exportExcel(response, list, "服务订单数据");
    }

    /**
     * 获取服务订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('order:serviceOrder:query')")
    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable("orderId") Long orderId)
    {
        return success(tServiceOrderService.selectTServiceOrderByOrderId(orderId));
    }

    /**
     * 新增服务订单
     */
    @PreAuthorize("@ss.hasPermi('order:serviceOrder:add')")
    @Log(title = "服务订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TServiceOrder tServiceOrder)
    {
        return toAjax(tServiceOrderService.insertTServiceOrder(tServiceOrder));
    }

    /**
     * 修改服务订单
     */
    @PreAuthorize("@ss.hasPermi('order:serviceOrder:edit')")
    @Log(title = "服务订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TServiceOrder tServiceOrder)
    {
        return toAjax(tServiceOrderService.updateTServiceOrder(tServiceOrder));
    }

    /**
     * 删除服务订单
     */
    @PreAuthorize("@ss.hasPermi('order:serviceOrder:remove')")
    @Log(title = "服务订单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{orderIds}")
    public AjaxResult remove(@PathVariable Long[] orderIds)
    {
        return toAjax(tServiceOrderService.deleteTServiceOrderByOrderIds(orderIds));
    }

    /**
     * 指派服务人员
     */
    @PreAuthorize("@ss.hasPermi('order:serviceOrder:edit')")
    @Log(title = "订单指派", businessType = BusinessType.UPDATE)
    @PutMapping("/assign")
    public AjaxResult assignStaff(@RequestBody java.util.Map<String, Long> params)
    {
        Long orderId = params.get("orderId");
        Long staffId = params.get("staffId");
        if (orderId == null || staffId == null) {
            return error("订单ID和服务人员ID不能为空");
        }
        TOrderStaff orderStaff = new TOrderStaff();
        orderStaff.setOrderId(orderId);
        orderStaff.setStaffId(staffId);
        orderStaff.setIsPrimary(1L);
        tOrderStaffService.insertTOrderStaff(orderStaff);

        TServiceOrder update = new TServiceOrder();
        update.setOrderId(orderId);
        update.setOrderStatus(1L);
        update.setAcceptTime(new java.util.Date());
        return toAjax(tServiceOrderService.updateTServiceOrder(update));
    }

    /**
     * 变更订单状态（开始服务/标记完成）
     */
    @PreAuthorize("@ss.hasPermi('order:serviceOrder:edit')")
    @Log(title = "订单状态变更", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody TServiceOrder order)
    {
        if (order.getOrderId() == null || order.getOrderStatus() == null) {
            return error("订单ID和目标状态不能为空");
        }
        TServiceOrder update = new TServiceOrder();
        update.setOrderId(order.getOrderId());
        update.setOrderStatus(order.getOrderStatus());
        if (order.getOrderStatus() == 2L) {
            update.setStartTime(new java.util.Date());
        } else if (order.getOrderStatus() == 3L) {
            update.setCompleteTime(new java.util.Date());
        }
        return toAjax(tServiceOrderService.updateTServiceOrder(update));
    }
}
