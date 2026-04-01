package com.ruoyi.health.service;

import com.ruoyi.health.domain.THealthRecord;

/**
 * 健康数据异步持久化服务接口
 */
public interface IHealthAsyncService {

    /**
     * 将校验后的记录推入异步处理队列
     */
    void pushRecord(THealthRecord record);

    /**
     * 批量处理队列中的记录同步到数据库
     */
    void syncToDb();
}
