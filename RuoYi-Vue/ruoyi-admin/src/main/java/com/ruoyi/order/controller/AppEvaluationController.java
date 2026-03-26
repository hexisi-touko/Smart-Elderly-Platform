package com.ruoyi.order.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.order.domain.TServiceEvaluation;
import com.ruoyi.order.domain.TServiceOrder;
import com.ruoyi.order.service.ITServiceEvaluationService;
import com.ruoyi.order.service.ITServiceOrderService;
import com.ruoyi.elderly.domain.TElderly;
import com.ruoyi.elderly.service.ITElderlyService;
import com.ruoyi.elderly.domain.TGuardian;
import com.ruoyi.elderly.service.ITGuardianService;
import com.ruoyi.elderly.domain.TElderlyGuardian;
import com.ruoyi.elderly.service.ITElderlyGuardianService;

/**
 * App端服务评价接口
 */
@RestController
@RequestMapping("/app/order/evaluation")
public class AppEvaluationController extends BaseController {

    @Autowired
    private ITServiceEvaluationService tServiceEvaluationService;

    @Autowired
    private ITServiceOrderService tServiceOrderService;

    @Autowired
    private ITElderlyService tElderlyService;

    @Autowired
    private ITGuardianService tGuardianService;

    @Autowired
    private ITElderlyGuardianService tElderlyGuardianService;

    /**
     * 提交服务评价
     */
    @PostMapping
    public AjaxResult add(@RequestBody TServiceEvaluation evaluation) {
        Long userId = SecurityUtils.getUserId();
        
        // 1. 识别身份并确认老人ID (逻辑与 AppOrderController 一致)
        Long elderlyId = null;
        TElderly elderlyParams = new TElderly();
        elderlyParams.setUserId(userId);
        List<TElderly> elderlyList = tElderlyService.selectTElderlyList(elderlyParams);

        if (elderlyList != null && !elderlyList.isEmpty()) {
            elderlyId = elderlyList.get(0).getElderlyId();
        } else {
            TGuardian guardianParams = new TGuardian();
            guardianParams.setUserId(userId);
            List<TGuardian> guardianList = tGuardianService.selectTGuardianList(guardianParams);
            if (guardianList != null && !guardianList.isEmpty()) {
                TElderlyGuardian egParams = new TElderlyGuardian();
                egParams.setGuardianId(guardianList.get(0).getGuardianId());
                List<TElderlyGuardian> egList = tElderlyGuardianService.selectTElderlyGuardianList(egParams);
                if (egList != null && !egList.isEmpty()) {
                    elderlyId = egList.get(0).getElderlyId();
                }
            }
        }
        
        if (elderlyId == null) {
            return error("评价失败：未找到您的身份档案");
        }

        // 2. 校验订单归属及状态
        if (evaluation.getOrderId() == null) {
            return error("参数错误：订单ID不能为空");
        }
        TServiceOrder order = tServiceOrderService.selectTServiceOrderByOrderId(evaluation.getOrderId());
        if (order == null || !elderlyId.equals(order.getElderlyId())) {
            return error("评价失败：订单不存在或无权评价");
        }
        if (order.getOrderStatus() != 3L) {
            return error("评价失败：仅“已完成”的订单可以进行评价");
        }

        // 3. 补全评价信息并提交
        evaluation.setElderlyId(elderlyId);
        evaluation.setEvaluationTime(DateUtils.getNowDate());
        evaluation.setIsDeleted(0L);
        
        // 插入评价 (Service内部已实现联动更新订单状态为 5:已评价)
        return toAjax(tServiceEvaluationService.insertTServiceEvaluation(evaluation));
    }
}
