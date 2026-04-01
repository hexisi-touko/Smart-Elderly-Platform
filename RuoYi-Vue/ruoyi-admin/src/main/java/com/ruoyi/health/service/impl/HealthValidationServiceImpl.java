package com.ruoyi.health.service.impl;

import java.math.BigDecimal;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.health.domain.THealthRecord;
import com.ruoyi.health.service.IHealthCacheService;
import com.ruoyi.health.service.IHealthValidationService;

/**
 * 健康数据验证服务实现类
 */
@Service
public class HealthValidationServiceImpl implements IHealthValidationService {

    @Autowired
    private IHealthCacheService healthCacheService;

    private ThreadLocal<String> validationMsg = new ThreadLocal<>();

    @Override
    public int validateRecord(THealthRecord record) {
        Long elderlyId = record.getElderlyId();
        if (elderlyId == null) return 0;

        Map<String, Object> threshold = healthCacheService.getThreshold(elderlyId);
        if (threshold == null || threshold.isEmpty()) return 0;

        int status = 0; // 0-正常, 1-警告, 2-严重/突变
        StringBuilder msg = new StringBuilder();

        // 1. 血压校验 (核心：收缩压)
        if (record.getSystolicBp() != null && record.getSystolicBp() > 0) {
            Long sysMax = toLong(threshold.get("sysBpMax"));
            Long sysMin = toLong(threshold.get("sysBpMin"));
            BigDecimal avg = toBigDecimal(threshold.get("avgSysBp"));
            
            // 绝对值校验
            if (record.getSystolicBp() > sysMax || record.getSystolicBp() < sysMin) {
                status = 2;
                msg.append("收缩压超出范围(").append(record.getSystolicBp()).append("); ");
            }
            
            // 动态基线偏离校验 (Deviation > 0.2)
            if (avg != null && avg.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal current = new BigDecimal(record.getSystolicBp());
                BigDecimal diff = current.subtract(avg).abs();
                BigDecimal deviation = diff.divide(avg, 4, BigDecimal.ROUND_HALF_UP);
                
                if (deviation.compareTo(new BigDecimal("0.2")) > 0) {
                    status = 2;
                    msg.append("收缩压发生剧烈突变(偏离度").append(deviation.multiply(new BigDecimal(100)).setScale(1, java.math.RoundingMode.HALF_UP)).append("%); ");
                }
            }
        }

        // 2. 心率校验
        if (record.getHeartRate() != null && record.getHeartRate() > 0) {
            Long hrMax = toLong(threshold.get("heartRateMax"));
            Long hrMin = toLong(threshold.get("heartRateMin"));
            if (record.getHeartRate() > hrMax || record.getHeartRate() < hrMin) {
                status = Math.max(status, 1);
                msg.append("心率异常(").append(record.getHeartRate()).append("); ");
            }
        }

        // 3. 血糖校验
        if (record.getBloodSugar() != null && record.getBloodSugar().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal bsMax = toBigDecimal(threshold.get("bloodSugarMax"));
            BigDecimal bsMin = toBigDecimal(threshold.get("bloodSugarMin"));
            if (record.getBloodSugar().compareTo(bsMax) > 0 || record.getBloodSugar().compareTo(bsMin) < 0) {
                status = Math.max(status, 1);
                msg.append("血糖异常(").append(record.getBloodSugar()).append("); ");
            }
        }

        // 4. 体温校验
        if (record.getTemperature() != null && record.getTemperature().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal tMax = toBigDecimal(threshold.get("temperatureMax"));
            BigDecimal tMin = toBigDecimal(threshold.get("temperatureMin"));
            if (record.getTemperature().compareTo(tMax) > 0 || record.getTemperature().compareTo(tMin) < 0) {
                status = Math.max(status, 1);
                msg.append("体温异常(").append(record.getTemperature()).append("); ");
            }
        }

        validationMsg.set(msg.toString());
        return status;
    }

    private BigDecimal toBigDecimal(Object val) {
        if (val == null) return null;
        if (val instanceof BigDecimal) return (BigDecimal) val;
        return new BigDecimal(val.toString());
    }

    private Long toLong(Object val) {
        if (val == null) return 0L;
        if (val instanceof Number) return ((Number) val).longValue();
        return Long.valueOf(val.toString());
    }

    @Override
    public String getValidationMessage() {
        return validationMsg.get();
    }
}
