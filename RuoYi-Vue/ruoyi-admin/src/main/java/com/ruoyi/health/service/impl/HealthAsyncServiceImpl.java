package com.ruoyi.health.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.ruoyi.health.domain.THealthRecord;
import com.ruoyi.health.service.IHealthAsyncService;
import com.ruoyi.health.service.IHealthCacheService;
import com.ruoyi.health.service.ITHealthRecordService;

/**
 * 健康数据异步持久化服务实现类
 */
@Service
public class HealthAsyncServiceImpl implements IHealthAsyncService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(HealthAsyncServiceImpl.class);
    private static final String HEALTH_RECORD_QUEUE = "queue:health:record";

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private ITHealthRecordService tHealthRecordService;

    @Autowired
    private IHealthCacheService healthCacheService;

    @Override
    public void pushRecord(THealthRecord record) {
        // 将记录推入 Redis 列表右端
        redisTemplate.opsForList().rightPush(HEALTH_RECORD_QUEUE, record);
    }

    @Override
    public void syncToDb() {
        // 批量拉取数据
        List<Object> records = new ArrayList<>();
        Object recordObj;
        
        // 每次同步最多取 500 条，防止长事务
        int count = 0;
        while (count < 500 && (recordObj = redisTemplate.opsForList().leftPop(HEALTH_RECORD_QUEUE)) != null) {
            records.add(recordObj);
            count++;
        }

        if (records.isEmpty()) return;

        log.info("健康数据异步同步开始，共 {} 条记录", records.size());
        for (Object obj : records) {
            THealthRecord record = (THealthRecord) obj;
            try {
                // 保存至 MySQL
                tHealthRecordService.insertTHealthRecord(record);
                
                // 每保存一条后，记录对应的老人ID，准备刷新其基线
                healthCacheService.updateDynamicBaseline(record.getElderlyId());
            } catch (Exception e) {
                log.error("健康数据同步失败: {}", e.getMessage());
            }
        }
    }
}
