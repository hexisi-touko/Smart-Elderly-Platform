package com.ruoyi.order.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.order.mapper.TServiceOrderMapper;
import com.ruoyi.order.domain.TServiceOrder;
import com.ruoyi.order.domain.TOrderStaff;
import com.ruoyi.order.mapper.TOrderStaffMapper;
import com.ruoyi.order.service.ITServiceOrderService;
import com.ruoyi.elderly.domain.TElderly;
import com.ruoyi.elderly.service.ITElderlyService;
import com.ruoyi.elderly.domain.TGuardian;
import com.ruoyi.elderly.service.ITGuardianService;
import com.ruoyi.elderly.domain.TElderlyGuardian;
import com.ruoyi.elderly.service.ITElderlyGuardianService;
import com.ruoyi.service.domain.TServiceStaff;
import com.ruoyi.service.service.ITServiceStaffService;
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
    private TOrderStaffMapper tOrderStaffMapper;

    @Autowired
    private ITElderlyService tElderlyService;

    @Autowired
    private ITGuardianService tGuardianService;

    @Autowired
    private ITElderlyGuardianService tElderlyGuardianService;

    @Autowired
    private ITServiceStaffService tServiceStaffService;

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
                // 3. 识别身份：查是否为服务人员（员工）
                TServiceStaff staffParams = new TServiceStaff();
                staffParams.setUserId(userId);
                List<TServiceStaff> staffList = tServiceStaffService.selectTServiceStaffList(staffParams);
                
                if (staffList != null && !staffList.isEmpty()) {
                    // 是服务人员，查该人员的所有关联订单
                    return tServiceOrderMapper.selectWorkerOwnOrders(staffList.get(0).getStaffId(), tServiceOrder.getOrderStatus());
                } else {
                    // 既不是老人、监护人，也不是服务人员
                    return new ArrayList<>();
                }
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

    // ========== 员工端方法实现 ==========

    /**
     * 根据 userId 获取对应的 staffId
     */
    private Long getStaffIdByUserId(Long userId) {
        TServiceStaff query = new TServiceStaff();
        query.setUserId(userId);
        List<TServiceStaff> staffList = tServiceStaffService.selectTServiceStaffList(query);
        if (staffList == null || staffList.isEmpty()) {
            throw new ServiceException("您不是服务人员，无权执行此操作");
        }
        return staffList.get(0).getStaffId();
    }

    /**
     * 员工接单
     */
    @Override
    public int acceptOrder(Long orderId, Long userId) {
        Long staffId = getStaffIdByUserId(userId);

        TServiceOrder order = tServiceOrderMapper.selectTServiceOrderByOrderId(orderId);
        if (order == null) {
            throw new ServiceException("订单不存在");
        }
        if (order.getOrderStatus() != 1L) {
            throw new ServiceException("该订单当前状态不可接单（仅'待接单'状态可操作）");
        }

        // 更新订单状态 -> 2(服务中)
        order.setOrderStatus(2L);
        order.setAcceptTime(DateUtils.getNowDate());
        order.setUpdateTime(DateUtils.getNowDate());
        int rows = tServiceOrderMapper.updateTServiceOrder(order);

        // 绑定订单-服务人员关联
        TOrderStaff orderStaff = new TOrderStaff();
        orderStaff.setOrderId(orderId);
        orderStaff.setStaffId(staffId);
        orderStaff.setIsPrimary(1L);
        orderStaff.setCreateTime(DateUtils.getNowDate());
        tOrderStaffMapper.insertTOrderStaff(orderStaff);

        return rows;
    }

    /**
     * 员工开始服务
     */
    @Override
    public int startService(Long orderId, Long userId) {
        Long staffId = getStaffIdByUserId(userId);

        TServiceOrder order = tServiceOrderMapper.selectTServiceOrderByOrderId(orderId);
        if (order == null) {
            throw new ServiceException("订单不存在");
        }
        if (order.getOrderStatus() != 2L) {
            throw new ServiceException("该订单当前状态不可开始服务（仅'已接单'状态可操作）");
        }

        // 校验：必须是本人接的单
        verifyStaffOwnership(orderId, staffId);

        // 状态保持为 2 (服务中)，仅记录开始时间
        order.setOrderStatus(2L);
        order.setStartTime(DateUtils.getNowDate());
        order.setUpdateTime(DateUtils.getNowDate());
        return tServiceOrderMapper.updateTServiceOrder(order);
    }

    /**
     * 员工完成服务
     */
    @Override
    public int completeService(Long orderId, Long userId) {
        Long staffId = getStaffIdByUserId(userId);

        TServiceOrder order = tServiceOrderMapper.selectTServiceOrderByOrderId(orderId);
        if (order == null) {
            throw new ServiceException("订单不存在");
        }
        if (order.getOrderStatus() != 2L) {
            throw new ServiceException("该订单当前状态不可完成（仅'服务中'状态可操作）");
        }

        verifyStaffOwnership(orderId, staffId);

        order.setOrderStatus(3L); // 3-已完成
        order.setCompleteTime(DateUtils.getNowDate());
        order.setUpdateTime(DateUtils.getNowDate());
        return tServiceOrderMapper.updateTServiceOrder(order);
    }

    /**
     * 查询员工的待接/进行中订单列表
     */
    @Override
    public List<TServiceOrder> selectWorkerOrderList(TServiceOrder tServiceOrder, Long userId) {
        Long staffId = getStaffIdByUserId(userId);
        // 待接单(status=1)的所有订单都可见 + 自己已接的订单
        // 简化实现：查全部待接单 + 自己关联的订单
        if (tServiceOrder.getOrderStatus() != null && tServiceOrder.getOrderStatus() == 1L) {
            // 查待接单池
            return tServiceOrderMapper.selectTServiceOrderList(tServiceOrder);
        }
        // 其他状态：只查自己的
        return tServiceOrderMapper.selectWorkerOwnOrders(staffId, tServiceOrder.getOrderStatus());
    }

    /**
     * 校验该订单是否由当前员工接的
     */
    private void verifyStaffOwnership(Long orderId, Long staffId) {
        TOrderStaff query = new TOrderStaff();
        query.setOrderId(orderId);
        query.setStaffId(staffId);
        List<TOrderStaff> list = tOrderStaffMapper.selectTOrderStaffList(query);
        if (list == null || list.isEmpty()) {
            throw new ServiceException("您不是该订单的服务人员，无权操作");
        }
    }
}
