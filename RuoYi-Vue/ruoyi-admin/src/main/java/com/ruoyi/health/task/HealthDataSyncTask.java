package com.ruoyi.health.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.ruoyi.health.service.IHealthAsyncService;

/**
 * 健康数据异步同步定时任务
 * 每 5 秒执行一次批量写库
 */
@Component
public class HealthDataSyncTask {

    private static final Logger log = LoggerFactory.getLogger(HealthDataSyncTask.class);

    @Autowired
    private IHealthAsyncService healthAsyncService;

    // 每 5 秒同步一次到数据库
    @Scheduled(fixedDelay = 5000)
    public void syncHealthData() {
        try {
            healthAsyncService.syncToDb();
        } catch (Exception e) {
            log.error("健康数据异步同步任务执行失败: {}", e.getMessage());
        }
    }
}
