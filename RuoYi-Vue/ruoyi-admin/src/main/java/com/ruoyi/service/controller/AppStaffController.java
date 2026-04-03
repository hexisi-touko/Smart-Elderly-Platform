package com.ruoyi.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.service.domain.TServiceStaff;
import com.ruoyi.service.service.ITServiceStaffService;

/**
 * 员工端（服务人员）资料完善Controller
 * 
 * @author antigravity
 */
@RestController
@RequestMapping("/app/staff")
public class AppStaffController extends BaseController {

    @Autowired
    private ITServiceStaffService tServiceStaffService;

    /**
     * 服务人员完善资料
     */
    @PostMapping("/setup")
    public AjaxResult setup(@RequestBody TServiceStaff tServiceStaff) {
        // 绑定当前用户ID
        tServiceStaff.setUserId(SecurityUtils.getUserId());
        // 设置默认状态为 1-在岗
        tServiceStaff.setStatus(1L);
        // 模拟提供商ID (通常应该是后台分配，这里为演示可以默认或由前端选)
        if (tServiceStaff.getProviderId() == null) {
            tServiceStaff.setProviderId(1L); 
        }
        return toAjax(tServiceStaffService.insertTServiceStaff(tServiceStaff));
    }
}
