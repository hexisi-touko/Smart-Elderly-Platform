package com.ruoyi.health.service;

import java.util.Map;

/**
 * 健康数据缓存服务接口
 * 处理高并发下的阈值获取与动态基线维护
 */
public interface IHealthCacheService {

    /**
     * 获取指定老人的综合阈值配置（优先从Redis获取）
     */
    Map<String, Object> getThreshold(Long elderlyId);

    /**
     * 刷新并预热指定老人的阈值缓存
     */
    void refreshThreshold(Long elderlyId);

    /**
     * 异步更新老人的动态基线（7天收缩压滑动平均值）
     */
    void updateDynamicBaseline(Long elderlyId);
}
