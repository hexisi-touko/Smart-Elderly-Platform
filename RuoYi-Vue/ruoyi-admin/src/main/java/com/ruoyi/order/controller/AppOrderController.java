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
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.order.domain.TServiceOrder;
import com.ruoyi.order.service.ITServiceOrderService;
import com.ruoyi.elderly.domain.TElderly;
import com.ruoyi.elderly.mapper.TElderlyMapper;
import com.ruoyi.elderly.domain.TGuardian;
import com.ruoyi.elderly.mapper.TGuardianMapper;
import com.ruoyi.elderly.domain.TElderlyGuardian;
import com.ruoyi.elderly.mapper.TElderlyGuardianMapper;

/**
 * C端服务订单Controller
 */
@RestController
@RequestMapping("/app/order")
public class AppOrderController extends BaseController {

    @Autowired
    private ITServiceOrderService tServiceOrderService;

    @Autowired
    private TElderlyMapper tElderlyMapper;

    @Autowired
    private TGuardianMapper tGuardianMapper;

    /**
     * 免权限下订单
     */
    @PostMapping
    public AjaxResult addOrder(@RequestBody TServiceOrder tServiceOrder) {
        Long userId = SecurityUtils.getUserId();
        
        // 查询当前登录用户的 elderlyId
        TElderly queryElderly = new TElderly();
        queryElderly.setUserId(userId);
        List<TElderly> elderlyList = tElderlyMapper.selectTElderlyList(queryElderly);
        
        if (elderlyList != null && !elderlyList.isEmpty()) {
            tServiceOrder.setElderlyId(elderlyList.get(0).getElderlyId());
        } else {
            // 如果不是老人，可能是监护人代下单
            TGuardian queryGuardian = new TGuardian();
            queryGuardian.setUserId(userId);
            List<TGuardian> guardianList = tGuardianMapper.selectTGuardianList(queryGuardian);
            if (guardianList != null && !guardianList.isEmpty()) {
                // TODO: 监护人应该能选择为哪个老人下单。为简化，如果你有 elderly_guardian 表，可以联查。但此时强制要求携带 elderlyId 或抛错。
                if (tServiceOrder.getElderlyId() == null) {
                    throw new ServiceException("监护人帮老人下单失败：未指定老人ID");
                }
            } else {
                throw new ServiceException("下单失败：未找到您的老人档案，请先完善身份资料");
            }
        }

        // 自动生成订单号
        tServiceOrder.setOrderNo(IdUtils.fastSimpleUUID().substring(0, 16).toUpperCase());
        // 订单状态 0待接单
        tServiceOrder.setOrderStatus(0L);
        tServiceOrder.setCreateTime(DateUtils.getNowDate());
        
        return toAjax(tServiceOrderService.insertTServiceOrder(tServiceOrder));
    }
}
