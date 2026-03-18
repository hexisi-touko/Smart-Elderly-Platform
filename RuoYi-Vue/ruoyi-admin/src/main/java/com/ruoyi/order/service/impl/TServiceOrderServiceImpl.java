package com.ruoyi.order.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.order.mapper.TServiceOrderMapper;
import com.ruoyi.order.domain.TServiceOrder;
import com.ruoyi.order.service.ITServiceOrderService;
import com.ruoyi.elderly.domain.TElderly;
import com.ruoyi.elderly.service.ITElderlyService;
import com.ruoyi.elderly.domain.TGuardian;
import com.ruoyi.elderly.service.ITGuardianService;
import com.ruoyi.elderly.domain.TElderlyGuardian;
import com.ruoyi.elderly.service.ITElderlyGuardianService;
import java.util.ArrayList;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.common.exception.ServiceException;

/**
 * 服务订单Service业务层处理
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@Service
public class TServiceOrderServiceImpl implements ITServiceOrderService {
    @Autowired
    private TServiceOrderMapper tServiceOrderMapper;

    @Autowired
    private ITElderlyService tElderlyService;

    @Autowired
    private ITGuardianService tGuardianService;

    @Autowired
    private ITElderlyGuardianService tElderlyGuardianService;

    /**
     * 查询服务订单
     * 
     * @param orderId 服务订单主键
     * @return 服务订单
     */
    @Override
    public TServiceOrder selectTServiceOrderByOrderId(Long orderId) {
        return tServiceOrderMapper.selectTServiceOrderByOrderId(orderId);
    }

    /**
     * 查询服务订单列表
     * 
     * @param tServiceOrder 服务订单
     * @return 服务订单
     */
    @Override
    public List<TServiceOrder> selectTServiceOrderList(TServiceOrder tServiceOrder) {
        return tServiceOrderMapper.selectTServiceOrderList(tServiceOrder);
    }

    /**
     * 新增服务订单
     * 
     * @param tServiceOrder 服务订单
     * @return 结果
     */
    @Override
    public int insertTServiceOrder(TServiceOrder tServiceOrder) {
        tServiceOrder.setCreateTime(DateUtils.getNowDate());
        return tServiceOrderMapper.insertTServiceOrder(tServiceOrder);
    }

    /**
     * 修改服务订单
     * 
     * @param tServiceOrder 服务订单
     * @return 结果
     */
    @Override
    public int updateTServiceOrder(TServiceOrder tServiceOrder) {
        tServiceOrder.setUpdateTime(DateUtils.getNowDate());
        return tServiceOrderMapper.updateTServiceOrder(tServiceOrder);
    }

    /**
     * 批量删除服务订单
     * 
     * @param orderIds 需要删除的服务订单主键
     * @return 结果
     */
    @Override
    public int deleteTServiceOrderByOrderIds(Long[] orderIds) {
        return tServiceOrderMapper.deleteTServiceOrderByOrderIds(orderIds);
    }

    /**
     * 删除服务订单信息
     * 
     * @param orderId 服务订单主键
     * @return 结果
     */
    @Override
    public int deleteTServiceOrderByOrderId(Long orderId) {
        return tServiceOrderMapper.deleteTServiceOrderByOrderId(orderId);
    }

    /**
     * App端查询服务订单列表（包含身份辅助识别逻辑）
     */
    @Override
    public List<TServiceOrder> selectAppServiceOrderList(TServiceOrder tServiceOrder, Long userId) {
        // 1. 识别身份：先查是否为老人
        TElderly elderlyParams = new TElderly();
        elderlyParams.setUserId(userId);
        List<TElderly> elderlyList = tElderlyService.selectTElderlyList(elderlyParams);

        if (elderlyList != null && !elderlyList.isEmpty()) {
            // 是老人，直接查该老人的订单
            tServiceOrder.setElderlyId(elderlyList.get(0).getElderlyId());
        } else {
            // 2. 识别身份：再查是否为监护人
            TGuardian guardianParams = new TGuardian();
            guardianParams.setUserId(userId);
            List<TGuardian> guardianList = tGuardianService.selectTGuardianList(guardianParams);

            if (guardianList != null && !guardianList.isEmpty()) {
                // 是监护人，查其名下关联老人的订单（默认取第一个老人）
                TElderlyGuardian egParams = new TElderlyGuardian();
                egParams.setGuardianId(guardianList.get(0).getGuardianId());
                List<TElderlyGuardian> egList = tElderlyGuardianService.selectTElderlyGuardianList(egParams);
                if (egList != null && !egList.isEmpty()) {
                    tServiceOrder.setElderlyId(egList.get(0).getElderlyId());
                } else {
                    return new ArrayList<>();
                }
            } else {
                // 既不是老人也不是监护人（理论上不应该，除非没完善资料）
                return new ArrayList<>();
            }
        }

        return tServiceOrderMapper.selectTServiceOrderList(tServiceOrder);
    }

    /**
     * App端新增服务订单（包含身份校验逻辑）
     */
    @Override
    public int insertAppServiceOrder(TServiceOrder tServiceOrder, Long userId) {
        // 1. 识别身份并校验
        TElderly elderlyParams = new TElderly();
        elderlyParams.setUserId(userId);
        List<TElderly> elderlyList = tElderlyService.selectTElderlyList(elderlyParams);

        if (elderlyList != null && !elderlyList.isEmpty()) {
            tServiceOrder.setElderlyId(elderlyList.get(0).getElderlyId());
        } else {
            TGuardian guardianParams = new TGuardian();
            guardianParams.setUserId(userId);
            List<TGuardian> guardianList = tGuardianService.selectTGuardianList(guardianParams);
            if (guardianList != null && !guardianList.isEmpty()) {
                if (tServiceOrder.getElderlyId() == null) {
                    throw new ServiceException("监护人帮老人下单失败：未指定老人ID");
                }
            } else {
                throw new ServiceException("下单失败：未找到您的角色档案，请先完善身份资料");
            }
        }

        // 2. 补全订单基本信息
        tServiceOrder.setOrderNo(IdUtils.fastSimpleUUID().substring(0, 16).toUpperCase());
        tServiceOrder.setOrderStatus(1L); // 1-待接单 (根据SRS 5.3.3)
        tServiceOrder.setCreateTime(DateUtils.getNowDate());

        return tServiceOrderMapper.insertTServiceOrder(tServiceOrder);
    }

    /**
     * App端取消服务订单
     */
    @Override
    public int cancelAppServiceOrder(Long orderId, Long userId) {
        // 1. 获取该订单详情
        TServiceOrder order = tServiceOrderMapper.selectTServiceOrderByOrderId(orderId);
        if (order == null) {
            throw new ServiceException("订单不存在");
        }

        // 2. 校验权限：只能取消自己的（或自己关联老人的）订单
        // 逻辑与查询一致，先拿当前用户的 elderlyId
        TElderly elderlyParams = new TElderly();
        elderlyParams.setUserId(userId);
        List<TElderly> elderlyList = tElderlyService.selectTElderlyList(elderlyParams);

        Long currentElderlyId = null;
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

        if (currentElderlyId == null || !currentElderlyId.equals(order.getElderlyId())) {
            throw new ServiceException("无权取消该订单");
        }

        // 3. 执行取消
        if (order.getOrderStatus() != 0 && order.getOrderStatus() != 1) {
            throw new ServiceException("当前状态不可取消（仅待支付或待接单订单可取消）");
        }

        order.setOrderStatus(4L); // 4-已取消 (符合SRS规范)
        order.setUpdateTime(DateUtils.getNowDate());
        return tServiceOrderMapper.updateTServiceOrder(order);
    }
}
