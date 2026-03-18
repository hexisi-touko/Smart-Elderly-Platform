package com.ruoyi.health.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
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

    /**
     * 查询当前老人的健康记录列表（支持按类型过滤）
     */
    @GetMapping("/record/list")
    public TableDataInfo listRecords(
            @RequestParam(required = false) String recordType,
            @RequestParam(required = false) String beginTime,
            @RequestParam(required = false) String endTime) {
        
        Long userId = SecurityUtils.getUserId();
        
        // 1. 识别身份并获取 elderlyId (逻辑复用)
        Long currentElderlyId = null;
        
        TElderly elderlyParams = new TElderly();
        elderlyParams.setUserId(userId);
        List<TElderly> elderlyList = tElderlyService.selectTElderlyList(elderlyParams);
        
        if (elderlyList != null && !elderlyList.isEmpty()) {
            currentElderlyId = elderlyList.get(0).getElderlyId();
        } else {
            TGuardian guardianParams = new TGuardian();
            guardianParams.setUserId(userId);
            List<TGuardian> guardianList = tGuardianService.selectTGuardianList(guardianParams);

            if (guardianList != null && !guardianList.isEmpty()) {
                TElderlyGuardian egParams = new TElderlyGuardian();
                egParams.setGuardianId(guardianList.get(0).getGuardianId());
                List<TElderlyGuardian> egList = tElderlyGuardianService.selectTElderlyGuardianList(egParams);
                if (egList != null && !egList.isEmpty()) {
                    currentElderlyId = egList.get(0).getElderlyId();
                }
            }
        }

        if (currentElderlyId == null) {
            return getDataTable(null);
        }

        // 2. 构造查询条件
        THealthRecord query = new THealthRecord();
        query.setElderlyId(currentElderlyId);
        query.setRecordType(recordType);
        query.getParams().put("beginCollectTime", beginTime);
        query.getParams().put("endCollectTime", endTime);

        // 3. 查询
        startPage();
        List<THealthRecord> list = tHealthRecordService.selectTHealthRecordList(query);
        return getDataTable(list);
    }
}
