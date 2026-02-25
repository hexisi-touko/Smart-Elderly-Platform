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
import com.ruoyi.order.service.ITServiceOrderService;
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
}
