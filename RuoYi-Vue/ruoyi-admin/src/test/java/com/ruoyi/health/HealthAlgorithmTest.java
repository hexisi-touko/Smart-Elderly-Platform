package com.ruoyi.health;

import com.ruoyi.RuoYiApplication;
import com.ruoyi.health.domain.THealthRecord;
import com.ruoyi.health.service.ITHealthRecordService;
import com.ruoyi.health.service.IHealthValidationService;
import com.ruoyi.health.service.IHealthCacheService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 健康阈值算法 2.0 单元测试
 * 路径：src/test/java/com/ruoyi/health/HealthAlgorithmTest.java
 */
@SpringBootTest(classes = RuoYiApplication.class)
public class HealthAlgorithmTest {

    @Autowired
    private ITHealthRecordService tHealthRecordService;

    @Autowired
    private IHealthValidationService healthValidationService;

    @Autowired
    private IHealthCacheService healthCacheService;

    private final Long TEST_ELDERLY_ID = 1L;

    @Test
    public void testHealthAlgorithmFlow() {
        System.out.println(">>> 开始健康算法 2.0 流程测试...");

        // 1. 清理历史数据 (确保测试环境干净)
        THealthRecord query = new THealthRecord();
        query.setElderlyId(TEST_ELDERLY_ID);
        List<THealthRecord> existing = tHealthRecordService.selectTHealthRecordList(query);
        for (THealthRecord r : existing) {
            tHealthRecordService.deleteTHealthRecordByRecordId(r.getRecordId());
        }
        System.out.println("1. 已清理老人 ID:" + TEST_ELDERLY_ID + " 的历史数据");

        // 2. 构造 7 天平稳基线 (约 125 mmHg)
        Calendar cal = Calendar.getInstance();
        for (int i = 7; i >= 1; i--) {
            cal.setTime(new Date());
            cal.add(Calendar.DATE, -i);
            
            THealthRecord record = new THealthRecord();
            record.setElderlyId(TEST_ELDERLY_ID);
            record.setSystolicBp(120L + (long)(Math.random() * 10)); // 120-130
            record.setDiastolicBp(80L);
            record.setRecordType("blood_pressure");
            record.setCollectMethod(0L); // 智能设备
            record.setCollectTime(cal.getTime());
            record.setDataStatus(1L); // 正常
            tHealthRecordService.insertTHealthRecord(record);
        }
        
        // 强制刷新缓存中的动态基线
        healthCacheService.refreshThreshold(TEST_ELDERLY_ID);
        System.out.println("2. 7天基线数据已注入，Redis 缓存映射已更新");

        // 3. 模拟突变数据校验 (165 mmHg)
        THealthRecord mutationRecord = new THealthRecord();
        mutationRecord.setElderlyId(TEST_ELDERLY_ID);
        mutationRecord.setSystolicBp(165L);
        mutationRecord.setDiastolicBp(95L);
        mutationRecord.setRecordType("blood_pressure");
        mutationRecord.setCollectMethod(0L);
        mutationRecord.setCollectTime(new Date());

        // 执行算法校验
        int status = healthValidationService.validateRecord(mutationRecord);
        String msg = healthValidationService.getValidationMessage();
        
        System.out.println("3. 校验突变记录 (165 mmHg): 状态=" + status + ", 详情=" + msg);

        // 断言验证
        // 165 与 125 相比，偏离度约 32% (> 20%)，应返回状态 2 (严重)
        assertEquals(2, status, "突变数据应触发状态 2 (严重/突变)");
        assertTrue(msg.contains("收缩压发生剧烈突变"), "错误消息应包含突变关键词");
        
        System.out.println(">>> 健康算法 2.0 流程测试通过！");
    }
}
