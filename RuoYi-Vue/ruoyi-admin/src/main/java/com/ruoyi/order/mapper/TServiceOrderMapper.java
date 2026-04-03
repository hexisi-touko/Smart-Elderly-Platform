package com.ruoyi.order.mapper;

import java.util.List;
import com.ruoyi.order.domain.TServiceOrder;
import org.apache.ibatis.annotations.Param;

/**
 * 服务订单Mapper接口
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public interface TServiceOrderMapper 
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
     * 删除服务订单
     * 
     * @param orderId 服务订单主键
     * @return 结果
     */
    public int deleteTServiceOrderByOrderId(Long orderId);

    /**
     * 批量删除服务订单
     * 
     * @param orderIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTServiceOrderByOrderIds(Long[] orderIds);

    /**
     * 查询员工自己关联的订单（通过 t_order_staff 关联）
     *
     * @param staffId 员工ID
     * @param orderStatus 订单状态（可为null）
     * @return 订单列表
     */
    public List<TServiceOrder> selectWorkerOwnOrders(@Param("staffId") Long staffId, @Param("orderStatus") Long orderStatus);
}
