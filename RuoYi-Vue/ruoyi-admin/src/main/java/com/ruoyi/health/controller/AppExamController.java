package com.ruoyi.health.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.elderly.domain.TElderly;
import com.ruoyi.elderly.service.ITElderlyService;
import com.ruoyi.health.domain.TPhysicalExamReservation;
import com.ruoyi.health.service.ITPhysicalExamReservationService;

/**
 * C端体检预约Controller
 * 为App端老人提供体检预约的查询、预约和取消接口
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/app/exam")
public class AppExamController extends BaseController {

    @Autowired
    private ITPhysicalExamReservationService examReservationService;

    @Autowired
    private ITElderlyService elderlyService;

    /**
     * 查询当前老人的体检预约列表
     */
    @GetMapping("/list")
    public TableDataInfo list(TPhysicalExamReservation query) {
        Long elderlyId = getCurrentElderlyId();
        if (elderlyId == null) {
            return getDataTable(null);
        }
        query.setElderlyId(elderlyId);
        startPage();
        List<TPhysicalExamReservation> list = examReservationService.selectTPhysicalExamReservationList(query);
        return getDataTable(list);
    }

    /**
     * 获取体检预约详细信息
     */
    @GetMapping("/{reservationId}")
    public AjaxResult getInfo(@PathVariable Long reservationId) {
        return success(examReservationService.selectTPhysicalExamReservationByReservationId(reservationId));
    }

    /**
     * 新增体检预约
     */
    @PostMapping
    public AjaxResult add(@RequestBody TPhysicalExamReservation reservation) {
        Long elderlyId = getCurrentElderlyId();
        if (elderlyId == null) {
            return error("未找到关联的老人信息");
        }
        reservation.setElderlyId(elderlyId);
        reservation.setReservationStatus(0L); // 0-待确认
        reservation.setReportStatus(0L);      // 0-未出报告
        return toAjax(examReservationService.insertTPhysicalExamReservation(reservation));
    }

    /**
     * 取消体检预约
     */
    @PutMapping("/cancel/{reservationId}")
    public AjaxResult cancel(@PathVariable Long reservationId) {
        // 归属校验：确保只能取消自己的预约
        Long elderlyId = getCurrentElderlyId();
        if (elderlyId == null) {
            return error("未找到关联的老人信息");
        }
        TPhysicalExamReservation existing = examReservationService.selectTPhysicalExamReservationByReservationId(reservationId);
        if (existing == null) {
            return error("预约记录不存在");
        }
        if (!elderlyId.equals(existing.getElderlyId())) {
            return error("无权取消该预约");
        }
        if (existing.getReservationStatus() != null && existing.getReservationStatus() >= 2L) {
            return error("该预约状态不允许取消");
        }

        TPhysicalExamReservation update = new TPhysicalExamReservation();
        update.setReservationId(reservationId);
        update.setReservationStatus(3L); // 3-已取消
        return toAjax(examReservationService.updateTPhysicalExamReservation(update));
    }

    /**
     * 获取当前登录用户对应的老人ID
     */
    private Long getCurrentElderlyId() {
        Long userId = SecurityUtils.getUserId();
        TElderly params = new TElderly();
        params.setUserId(userId);
        List<TElderly> list = elderlyService.selectTElderlyList(params);
        if (list != null && !list.isEmpty()) {
            return list.get(0).getElderlyId();
        }
        return null;
    }
}
