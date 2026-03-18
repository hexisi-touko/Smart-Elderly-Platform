package com.ruoyi.order.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.order.domain.TServiceOrder;
import com.ruoyi.order.service.ITServiceOrderService;

/**
 * C端服务订单Controller
 */
@RestController
@RequestMapping("/app/order")
public class AppOrderController extends BaseController {

    @Autowired
    private ITServiceOrderService tServiceOrderService;

    /**
     * 查询当前用户的订单列表
     */
    @GetMapping("/list")
    public TableDataInfo listMyOrders(@RequestParam(required = false) Long orderStatus) {
        Long userId = SecurityUtils.getUserId();
        
        TServiceOrder query = new TServiceOrder();
        if (orderStatus != null) {
            query.setOrderStatus(orderStatus);
        }

        // startPage() 必须紧贴在最后的数据库查询操作之前
        startPage();
        List<TServiceOrder> list = tServiceOrderService.selectAppServiceOrderList(query, userId);
        return getDataTable(list);
    }

    /**
     * 免权限下订单
     */
    @PostMapping
    public AjaxResult addOrder(@RequestBody TServiceOrder tServiceOrder) {
        Long userId = SecurityUtils.getUserId();
        return toAjax(tServiceOrderService.insertAppServiceOrder(tServiceOrder, userId));
    }

    /**
     * 取消订单
     */
    @PutMapping("/cancel/{orderId}")
    public AjaxResult cancelOrder(@PathVariable Long orderId) {
        Long userId = SecurityUtils.getUserId();
        return toAjax(tServiceOrderService.cancelAppServiceOrder(orderId, userId));
    }
}
