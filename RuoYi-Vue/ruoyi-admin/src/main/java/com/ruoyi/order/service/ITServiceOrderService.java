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
}
