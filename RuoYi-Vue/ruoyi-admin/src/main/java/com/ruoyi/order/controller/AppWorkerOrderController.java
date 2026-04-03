package com.ruoyi.order.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.order.domain.TServiceOrder;
import com.ruoyi.order.service.ITServiceOrderService;

/**
 * 员工端（服务人员）订单操作Controller
 * 
 * @author antigravity
 */
@RestController
@RequestMapping("/app/worker/order")
public class AppWorkerOrderController extends BaseController {

    @Autowired
    private ITServiceOrderService tServiceOrderService;

    /**
     * 查询员工关联的订单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(TServiceOrder tServiceOrder) {
        startPage();
        List<TServiceOrder> list = tServiceOrderService.selectWorkerOrderList(tServiceOrder, SecurityUtils.getUserId());
        return getDataTable(list);
    }

    /**
     * 员工接单
     */
    @PutMapping("/accept/{orderId}")
    public AjaxResult accept(@PathVariable Long orderId) {
        return toAjax(tServiceOrderService.acceptOrder(orderId, SecurityUtils.getUserId()));
    }

    /**
     * 开始服务
     */
    @PutMapping("/start/{orderId}")
    public AjaxResult start(@PathVariable Long orderId) {
        return toAjax(tServiceOrderService.startService(orderId, SecurityUtils.getUserId()));
    }

    /**
     * 完成服务
     */
    @PutMapping("/complete")
    public AjaxResult complete(@RequestBody TServiceOrder order) {
        return toAjax(tServiceOrderService.completeService(order.getOrderId(), 
            SecurityUtils.getUserId(), 
            order.getServiceRecord(), 
            order.getServicePhotos()));
    }
}
