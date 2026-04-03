package com.ruoyi.order.service;

import java.util.List;
import com.ruoyi.order.domain.TServiceOrder;

/**
 * 服务订单Service接口
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public interface ITServiceOrderService 
{
    /**
     * 查询服务订单
     * 
     * @param orderId 服务订单主键
     * @return 服务订单
     */
    public TServiceOrder selectTServiceOrderByOrderId(Long orderId);

    /**
     * 查询服务订单列表
     * 
     * @param tServiceOrder 服务订单
     * @return 服务订单集合
     */
    public List<TServiceOrder> selectTServiceOrderList(TServiceOrder tServiceOrder);

    /**
     * 新增服务订单
     * 
     * @param tServiceOrder 服务订单
     * @return 结果
     */
    public int insertTServiceOrder(TServiceOrder tServiceOrder);

    /**
     * 修改服务订单
     * 
     * @param tServiceOrder 服务订单
     * @return 结果
     */
    public int updateTServiceOrder(TServiceOrder tServiceOrder);

    /**
     * 批量删除服务订单
     * 
     * @param orderIds 需要删除的服务订单主键集合
     * @return 结果
     */
    public int deleteTServiceOrderByOrderIds(Long[] orderIds);

    /**
     * 删除服务订单信息
     * 
     * @param orderId 服务订单主键
     * @return 结果
     */
    public int deleteTServiceOrderByOrderId(Long orderId);

    /**
     * App端查询服务订单列表（包含身份辅助识别逻辑）
     * 
     * @param tServiceOrder 服务订单
     * @param userId 当前登录用户ID
     * @return 服务订单集合
     */
    public List<TServiceOrder> selectAppServiceOrderList(TServiceOrder tServiceOrder, Long userId);

    /**
     * App端新增服务订单（包含身份校验逻辑）
     * 
     * @param tServiceOrder 服务订单
     * @param userId 当前登录用户ID
     * @return 结果
     */
    public int insertAppServiceOrder(TServiceOrder tServiceOrder, Long userId);

    /**
     * App端取消服务订单
     * 
     * @param orderId 订单ID
     * @param userId 当前登录用户ID
     * @return 结果
     */
    public int cancelAppServiceOrder(Long orderId, Long userId);

    // ========== 员工端方法 ==========

    /**
     * 员工接单
     */
    public int acceptOrder(Long orderId, Long userId);

    /**
     * 员工开始服务
     */
    public int startService(Long orderId, Long userId);

    /**
     * 员工完成服务
     */
    public int completeService(Long orderId, Long userId);

    /**
     * 查询员工的待接/进行中订单列表
     */
    public List<TServiceOrder> selectWorkerOrderList(TServiceOrder tServiceOrder, Long userId);
}
