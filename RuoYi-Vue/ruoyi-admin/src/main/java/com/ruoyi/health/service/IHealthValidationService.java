package com.ruoyi.health.service;

import com.ruoyi.health.domain.THealthRecord;

/**
 * 健康数据验证服务接口
 * 执行两层算法：优先级阈值校验 + 动态偏差校验
 */
public interface IHealthValidationService {

    /**
     * 验证单条健康记录是否异常
     * @return 状态码：0-正常，1-一般异常，2-严重异常（数据突变或大幅超标）
     */
    int validateRecord(THealthRecord record);

    /**
     * 获取最近一词验证的详细结果描述
     */
    String getValidationMessage();
}
