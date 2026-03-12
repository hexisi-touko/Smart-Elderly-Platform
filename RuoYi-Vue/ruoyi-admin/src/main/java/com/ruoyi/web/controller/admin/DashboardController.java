package com.ruoyi.web.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.service.DashboardService;

/**
 * 仪表盘数据 控制器
 */
@RestController
@RequestMapping("/admin/dashboard")
public class DashboardController
{
    @Autowired
    private DashboardService dashboardService;

    /**
     * 获取核心指标概览
     */
    @GetMapping("/overview")
    public AjaxResult overview()
    {
        return AjaxResult.success(dashboardService.getOverview());
    }

    /**
     * 获取订单趋势
     */
    @GetMapping("/orderTrend")
    public AjaxResult orderTrend(@RequestParam(defaultValue = "7") int days)
    {
        return AjaxResult.success(dashboardService.getOrderTrend(days));
    }

    /**
     * 获取预警类型统计
     */
    @GetMapping("/alertStats")
    public AjaxResult alertStats()
    {
        return AjaxResult.success(dashboardService.getAlertTypeStats());
    }

    /**
     * 获取订单状态分布
     */
    @GetMapping("/orderStatusDistribution")
    public AjaxResult orderStatusDistribution()
    {
        return AjaxResult.success(dashboardService.getOrderStatusDistribution());
    }

    /**
     * 获取服务满意度分布
     */
    @GetMapping("/satisfaction")
    public AjaxResult satisfaction()
    {
        return AjaxResult.success(dashboardService.getSatisfactionDistribution());
    }
}
