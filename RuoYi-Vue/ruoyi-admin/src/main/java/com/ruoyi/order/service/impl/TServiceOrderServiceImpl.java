package com.ruoyi.order.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.order.mapper.TServiceOrderMapper;
import com.ruoyi.order.domain.TServiceOrder;
import com.ruoyi.order.service.ITServiceOrderService;

/**
 * 服务订单Service业务层处理
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@Service
public class TServiceOrderServiceImpl implements ITServiceOrderService 
{
    @Autowired
    private TServiceOrderMapper tServiceOrderMapper;

    /**
     * 查询服务订单
     * 
     * @param orderId 服务订单主键
     * @return 服务订单
     */
    @Override
    public TServiceOrder selectTServiceOrderByOrderId(Long orderId)
    {
        return tServiceOrderMapper.selectTServiceOrderByOrderId(orderId);
    }

    /**
     * 查询服务订单列表
     * 
     * @param tServiceOrder 服务订单
     * @return 服务订单
     */
    @Override
    public List<TServiceOrder> selectTServiceOrderList(TServiceOrder tServiceOrder)
    {
        return tServiceOrderMapper.selectTServiceOrderList(tServiceOrder);
    }

    /**
     * 新增服务订单
     * 
     * @param tServiceOrder 服务订单
     * @return 结果
     */
    @Override
    public int insertTServiceOrder(TServiceOrder tServiceOrder)
    {
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
    public int updateTServiceOrder(TServiceOrder tServiceOrder)
    {
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
    public int deleteTServiceOrderByOrderIds(Long[] orderIds)
    {
        return tServiceOrderMapper.deleteTServiceOrderByOrderIds(orderIds);
    }

    /**
     * 删除服务订单信息
     * 
     * @param orderId 服务订单主键
     * @return 结果
     */
    @Override
    public int deleteTServiceOrderByOrderId(Long orderId)
    {
        return tServiceOrderMapper.deleteTServiceOrderByOrderId(orderId);
    }
}
