package com.ruoyi.health.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.ruoyi.health.domain.TMedicationReminder;
import com.ruoyi.health.service.ITMedicationReminderService;

/**
 * C端用药提醒Controller
 * 为App端老人提供用药提醒的增删改查接口
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/app/medication")
public class AppMedicationController extends BaseController {

    @Autowired
    private ITMedicationReminderService medicationReminderService;

    @Autowired
    private ITElderlyService elderlyService;

    /**
     * 查询当前老人的用药提醒列表
     */
    @GetMapping("/list")
    public TableDataInfo list(TMedicationReminder query) {
        Long elderlyId = getCurrentElderlyId();
        if (elderlyId == null) {
            return getDataTable(null);
        }
        query.setElderlyId(elderlyId);
        startPage();
        List<TMedicationReminder> list = medicationReminderService.selectTMedicationReminderList(query);
        return getDataTable(list);
    }

    /**
     * 获取用药提醒详细信息
     */
    @GetMapping("/{reminderId}")
    public AjaxResult getInfo(@PathVariable Long reminderId) {
        return success(medicationReminderService.selectTMedicationReminderByReminderId(reminderId));
    }

    /**
     * 新增用药提醒
     */
    @PostMapping
    public AjaxResult add(@RequestBody TMedicationReminder reminder) {
        Long elderlyId = getCurrentElderlyId();
        if (elderlyId == null) {
            return error("未找到关联的老人信息");
        }
        reminder.setElderlyId(elderlyId);
        reminder.setStatus(1L); // 1-启用
        return toAjax(medicationReminderService.insertTMedicationReminder(reminder));
    }

    /**
     * 修改用药提醒
     */
    @PutMapping
    public AjaxResult edit(@RequestBody TMedicationReminder reminder) {
        Long elderlyId = getCurrentElderlyId();
        if (elderlyId == null) {
            return error("未找到关联的老人信息");
        }
        reminder.setElderlyId(elderlyId);
        return toAjax(medicationReminderService.updateTMedicationReminder(reminder));
    }

    /**
     * 删除用药提醒
     */
    @DeleteMapping("/{reminderIds}")
    public AjaxResult remove(@PathVariable Long[] reminderIds) {
        return toAjax(medicationReminderService.deleteTMedicationReminderByReminderIds(reminderIds));
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
