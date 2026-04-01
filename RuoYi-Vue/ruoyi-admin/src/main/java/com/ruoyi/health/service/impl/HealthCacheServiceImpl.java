package com.ruoyi.health.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Calendar;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.health.domain.THealthThreshold;
import com.ruoyi.health.service.ITHealthThresholdService;
import com.ruoyi.elderly.domain.TElderlyChronic;
import com.ruoyi.elderly.service.ITElderlyChronicService;
import com.ruoyi.health.domain.THealthRecord;
import com.ruoyi.health.service.ITHealthRecordService;
import com.ruoyi.health.service.IHealthCacheService;

/**
 * 健康数据缓存服务实现类
 */
@Service
public class HealthCacheServiceImpl implements IHealthCacheService {

    private static final String THRESHOLD_KEY_PREFIX = "elderly:threshold:";

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ITHealthThresholdService tHealthThresholdService;

    @Autowired
    private ITElderlyChronicService tElderlyChronicService;

    @Autowired
    private ITHealthRecordService tHealthRecordService;

    @Override
    public Map<String, Object> getThreshold(Long elderlyId) {
        String key = THRESHOLD_KEY_PREFIX + elderlyId;
        Map<String, Object> cacheMap = redisCache.getCacheMap(key);
        
        if (cacheMap != null && !cacheMap.isEmpty()) {
            return cacheMap;
        }

        // 缓存失效，执行预热逻辑
        refreshThreshold(elderlyId);
        return redisCache.getCacheMap(key);
    }

    @Override
    public void refreshThreshold(Long elderlyId) {
        Map<String, Object> thresholdMap = new HashMap<>();
        
        // --- Layer P3: Default Standards ---
        thresholdMap.put("sysBpMax", 140L);
        thresholdMap.put("sysBpMin", 90L);
        thresholdMap.put("diaBpMax", 90L);
        thresholdMap.put("diaBpMin", 60L);
        thresholdMap.put("heartRateMax", 100L);
        thresholdMap.put("heartRateMin", 60L);
        thresholdMap.put("bloodSugarMax", new BigDecimal("6.1"));
        thresholdMap.put("bloodSugarMin", new BigDecimal("3.9"));
        thresholdMap.put("temperatureMax", new BigDecimal("37.3"));
        thresholdMap.put("temperatureMin", new BigDecimal("36.0"));
        thresholdMap.put("chronicTags", "");

        // --- Layer P2: Chronic Correction ---
        TElderlyChronic chronicParams = new TElderlyChronic();
        chronicParams.setElderlyId(elderlyId);
        List<TElderlyChronic> chronicList = tElderlyChronicService.selectTElderlyChronicList(chronicParams);
        StringBuilder tags = new StringBuilder();
        boolean isHypertension = false;
        if (chronicList != null) {
            for (TElderlyChronic c : chronicList) {
                tags.append(c.getChronicType()).append(",");
                if ("高血压".equals(c.getChronicType())) {
                    isHypertension = true;
                }
            }
        }
        thresholdMap.put("chronicTags", tags.toString());
        if (isHypertension) {
            thresholdMap.put("sysBpMax", 155L); // 慢病修正：上限放宽至 155
        }

        // --- Layer P1: Individual Settings (Override) ---
        THealthThreshold thresholdParams = new THealthThreshold();
        thresholdParams.setElderlyId(elderlyId);
        List<THealthThreshold> p1List = tHealthThresholdService.selectTHealthThresholdList(thresholdParams);
        if (p1List != null && !p1List.isEmpty()) {
            THealthThreshold p1 = p1List.get(0);
            if (p1.getSysBpMax() != null) thresholdMap.put("sysBpMax", p1.getSysBpMax());
            if (p1.getSysBpMin() != null) thresholdMap.put("sysBpMin", p1.getSysBpMin());
            if (p1.getDiaBpMax() != null) thresholdMap.put("diaBpMax", p1.getDiaBpMax());
            if (p1.getDiaBpMin() != null) thresholdMap.put("diaBpMin", p1.getDiaBpMin());
            if (p1.getHeartRateMax() != null) thresholdMap.put("heartRateMax", p1.getHeartRateMax());
            if (p1.getHeartRateMin() != null) thresholdMap.put("heartRateMin", p1.getHeartRateMin());
            if (p1.getBloodSugarMax() != null) thresholdMap.put("bloodSugarMax", p1.getBloodSugarMax());
            if (p1.getBloodSugarMin() != null) thresholdMap.put("bloodSugarMin", p1.getBloodSugarMin());
            if (p1.getTemperatureMax() != null) thresholdMap.put("temperatureMax", p1.getTemperatureMax());
            if (p1.getTemperatureMin() != null) thresholdMap.put("temperatureMin", p1.getTemperatureMin());
        }

        // --- Dynamic Baseline: Moving Average (7 Days) ---
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        Date beginTime = cal.getTime();
        
        THealthRecord recordQuery = new THealthRecord();
        recordQuery.setElderlyId(elderlyId);
        recordQuery.getParams().put("beginCollectTime", beginTime);
        List<THealthRecord> records = tHealthRecordService.selectTHealthRecordList(recordQuery);
        
        BigDecimal sumSysBp = BigDecimal.ZERO;
        int count = 0;
        if (records != null) {
            for (THealthRecord r : records) {
                if (r.getSystolicBp() != null && r.getSystolicBp() > 0) {
                    sumSysBp = sumSysBp.add(new BigDecimal(r.getSystolicBp()));
                    count++;
                }
            }
        }
        
        if (count > 0) {
            BigDecimal avg = sumSysBp.divide(new BigDecimal(count), 1, RoundingMode.HALF_UP);
            thresholdMap.put("avgSysBp", avg);
        } else {
            thresholdMap.put("avgSysBp", new BigDecimal(thresholdMap.get("sysBpMax").toString())); // 兜底使用上限
        }

        // 写入缓存
        redisCache.setCacheMap(THRESHOLD_KEY_PREFIX + elderlyId, thresholdMap);
    }

    @Override
    public void updateDynamicBaseline(Long elderlyId) {
        // 此处逻辑可直接复用 refreshThreshold 中的基线计算部分，
        // 考虑到性能，也可以只刷新 avgSysBp 字段
        refreshThreshold(elderlyId);
    }
}
