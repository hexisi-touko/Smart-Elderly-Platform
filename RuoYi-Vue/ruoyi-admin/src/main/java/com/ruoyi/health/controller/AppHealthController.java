package com.ruoyi.health.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.elderly.domain.TGuardian;
import com.ruoyi.elderly.domain.TElderly;
import com.ruoyi.elderly.domain.TElderlyGuardian;
import com.ruoyi.elderly.service.ITElderlyService;
import com.ruoyi.elderly.service.ITGuardianService;
import com.ruoyi.elderly.service.ITElderlyGuardianService;
import com.ruoyi.health.domain.THealthRecord;
import com.ruoyi.health.service.ITHealthRecordService;
import com.ruoyi.health.service.IHealthAsyncService;
import com.ruoyi.health.service.IHealthValidationService;

/**
 * App端健康记录Controller
 * 自动根据登录身份识别对应的老人健康数据
 */
@RestController
@RequestMapping("/app/health")
public class AppHealthController extends BaseController {

    @Autowired
    private ITHealthRecordService tHealthRecordService;

    @Autowired
    private ITElderlyService tElderlyService;

    @Autowired
    private ITGuardianService tGuardianService;

    @Autowired
    private ITElderlyGuardianService tElderlyGuardianService;

    @Autowired
    private IHealthValidationService healthValidationService;

    @Autowired
    private IHealthAsyncService healthAsyncService;

    /**
     * 上报健康数据（设备上报/小程序录入）
     * 采用双层过滤算法与异步入库
     */
    @PostMapping("/report")
    public AjaxResult report(@RequestBody THealthRecord record) {
        Long elderlyId = getCurrentElderlyId();
        if (elderlyId == null) {
            return error("未找到关联的老人信息，无法上报数据");
        }
        
        record.setElderlyId(elderlyId);
        
        // 1. 执行双层校验算法 (P1-P2-P3 + 动态基线)
        int dataStatus = healthValidationService.validateRecord(record);
        record.setDataStatus(Long.valueOf(dataStatus));
        // 设置处理结果/备注（如果是异常的话）
        if (dataStatus > 0) {
            record.setRemark(healthValidationService.getValidationMessage());
        }

        // 2. 推入异步队列处理（高并发支撑）
        healthAsyncService.pushRecord(record);

        return success(dataStatus == 0 ? "上报成功" : "数据异常已记录: " + healthValidationService.getValidationMessage());
    }

    /**
     * 查询当前老人的健康记录列表（支持按类型过滤）
     */
    @GetMapping("/record/list")
    public TableDataInfo listRecords(
            @RequestParam(required = false) String recordType,
            @RequestParam(required = false) String beginTime,
            @RequestParam(required = false) String endTime) {

        Long currentElderlyId = getCurrentElderlyId();

        if (currentElderlyId == null) {
            return getDataTable(null);
        }

        // 2. 构造查询条件
        THealthRecord query = new THealthRecord();
        query.setElderlyId(currentElderlyId);
        query.setRecordType(recordType);
        
        // 只有当时间参数不为空时才加入查询
        if (beginTime != null && !beginTime.trim().isEmpty()) {
            query.getParams().put("beginCollectTime", beginTime);
        }
        if (endTime != null && !endTime.trim().isEmpty()) {
            query.getParams().put("endCollectTime", endTime);
        }

        // 3. 查询
        startPage();
        List<THealthRecord> list = tHealthRecordService.selectTHealthRecordList(query);
        return getDataTable(list);
    }

    /**
     * 获取当前登录用户对应的老人ID
     */
    private Long getCurrentElderlyId() {
        Long userId = SecurityUtils.getUserId();
        TElderly elderlyParams = new TElderly();
        elderlyParams.setUserId(userId);
        List<TElderly> elderlyList = tElderlyService.selectTElderlyList(elderlyParams);

        if (elderlyList != null && !elderlyList.isEmpty()) {
            return elderlyList.get(0).getElderlyId();
        } else {
            TGuardian guardianParams = new TGuardian();
            guardianParams.setUserId(userId);
            List<TGuardian> guardianList = tGuardianService.selectTGuardianList(guardianParams);

            if (guardianList != null && !guardianList.isEmpty()) {
                TElderlyGuardian egParams = new TElderlyGuardian();
                egParams.setGuardianId(guardianList.get(0).getGuardianId());
                List<TElderlyGuardian> egList = tElderlyGuardianService.selectTElderlyGuardianList(egParams);
                if (egList != null && !egList.isEmpty()) {
                    return egList.get(0).getElderlyId();
                }
            }
        }
        return null;
    }
}
