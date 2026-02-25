package com.ruoyi.order.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.order.mapper.TProviderSettlementMapper;
import com.ruoyi.order.domain.TProviderSettlement;
import com.ruoyi.order.service.ITProviderSettlementService;

/**
 * 服务商结算管理Service业务层处理
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@Service
public class TProviderSettlementServiceImpl implements ITProviderSettlementService 
{
    @Autowired
    private TProviderSettlementMapper tProviderSettlementMapper;

    /**
     * 查询服务商结算管理
     * 
     * @param settlementId 服务商结算管理主键
     * @return 服务商结算管理
     */
    @Override
    public TProviderSettlement selectTProviderSettlementBySettlementId(Long settlementId)
    {
        return tProviderSettlementMapper.selectTProviderSettlementBySettlementId(settlementId);
    }

    /**
     * 查询服务商结算管理列表
     * 
     * @param tProviderSettlement 服务商结算管理
     * @return 服务商结算管理
     */
    @Override
    public List<TProviderSettlement> selectTProviderSettlementList(TProviderSettlement tProviderSettlement)
    {
        return tProviderSettlementMapper.selectTProviderSettlementList(tProviderSettlement);
    }

    /**
     * 新增服务商结算管理
     * 
     * @param tProviderSettlement 服务商结算管理
     * @return 结果
     */
    @Override
    public int insertTProviderSettlement(TProviderSettlement tProviderSettlement)
    {
        tProviderSettlement.setCreateTime(DateUtils.getNowDate());
        return tProviderSettlementMapper.insertTProviderSettlement(tProviderSettlement);
    }

    /**
     * 修改服务商结算管理
     * 
     * @param tProviderSettlement 服务商结算管理
     * @return 结果
     */
    @Override
    public int updateTProviderSettlement(TProviderSettlement tProviderSettlement)
    {
        tProviderSettlement.setUpdateTime(DateUtils.getNowDate());
        return tProviderSettlementMapper.updateTProviderSettlement(tProviderSettlement);
    }

    /**
     * 批量删除服务商结算管理
     * 
     * @param settlementIds 需要删除的服务商结算管理主键
     * @return 结果
     */
    @Override
    public int deleteTProviderSettlementBySettlementIds(Long[] settlementIds)
    {
        return tProviderSettlementMapper.deleteTProviderSettlementBySettlementIds(settlementIds);
    }

    /**
     * 删除服务商结算管理信息
     * 
     * @param settlementId 服务商结算管理主键
     * @return 结果
     */
    @Override
    public int deleteTProviderSettlementBySettlementId(Long settlementId)
    {
        return tProviderSettlementMapper.deleteTProviderSettlementBySettlementId(settlementId);
    }
}
