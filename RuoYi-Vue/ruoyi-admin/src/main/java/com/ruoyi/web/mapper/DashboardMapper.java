package com.ruoyi.web.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * 仪表盘数据 Mapper接口
 */
public interface DashboardMapper
{
    int countElderly();

    int countProviders();

    int countOrders();

    int countTodayOrders();

    int countTodayAlerts();

    BigDecimal avgSatisfaction();

    List<Map<String, Object>> orderTrend(@Param("days") int days);

    List<Map<String, Object>> alertTypeStats();

    List<Map<String, Object>> orderStatusDistribution();

    List<Map<String, Object>> satisfactionDistribution();
}
