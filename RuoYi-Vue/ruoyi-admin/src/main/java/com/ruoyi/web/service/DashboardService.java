package com.ruoyi.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.web.mapper.DashboardMapper;

/**
 * 仪表盘数据 服务层
 */
@Service
public class DashboardService
{
    @Autowired
    private DashboardMapper dashboardMapper;

    /**
     * 获取核心指标概览
     */
    public Map<String, Object> getOverview()
    {
        Map<String, Object> result = new HashMap<>();
        result.put("totalElderly", dashboardMapper.countElderly());
        result.put("totalProviders", dashboardMapper.countProviders());
        result.put("totalOrders", dashboardMapper.countOrders());
        result.put("todayOrders", dashboardMapper.countTodayOrders());
        result.put("todayAlerts", dashboardMapper.countTodayAlerts());
        result.put("avgSatisfaction", dashboardMapper.avgSatisfaction());
        return result;
    }

    /**
     * 获取订单趋势（近N天）
     */
    public List<Map<String, Object>> getOrderTrend(int days)
    {
        return dashboardMapper.orderTrend(days);
    }

    /**
     * 获取预警类型统计
     */
    public List<Map<String, Object>> getAlertTypeStats()
    {
        return dashboardMapper.alertTypeStats();
    }

    /**
     * 获取订单状态分布
     */
    public List<Map<String, Object>> getOrderStatusDistribution()
    {
        return dashboardMapper.orderStatusDistribution();
    }

    /**
     * 获取服务满意度分布
     */
    public List<Map<String, Object>> getSatisfactionDistribution()
    {
        return dashboardMapper.satisfactionDistribution();
    }
}
