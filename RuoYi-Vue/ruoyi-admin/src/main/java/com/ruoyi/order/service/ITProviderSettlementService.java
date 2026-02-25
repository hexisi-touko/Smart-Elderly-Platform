package com.ruoyi.order.service;

import java.util.List;
import com.ruoyi.order.domain.TProviderSettlement;

/**
 * 服务商结算管理Service接口
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public interface ITProviderSettlementService 
{
    /**
     * 查询服务商结算管理
     * 
     * @param settlementId 服务商结算管理主键
     * @return 服务商结算管理
     */
    public TProviderSettlement selectTProviderSettlementBySettlementId(Long settlementId);

    /**
     * 查询服务商结算管理列表
     * 
     * @param tProviderSettlement 服务商结算管理
     * @return 服务商结算管理集合
     */
    public List<TProviderSettlement> selectTProviderSettlementList(TProviderSettlement tProviderSettlement);

    /**
     * 新增服务商结算管理
     * 
     * @param tProviderSettlement 服务商结算管理
     * @return 结果
     */
    public int insertTProviderSettlement(TProviderSettlement tProviderSettlement);

    /**
     * 修改服务商结算管理
     * 
     * @param tProviderSettlement 服务商结算管理
     * @return 结果
     */
    public int updateTProviderSettlement(TProviderSettlement tProviderSettlement);

    /**
     * 批量删除服务商结算管理
     * 
     * @param settlementIds 需要删除的服务商结算管理主键集合
     * @return 结果
     */
    public int deleteTProviderSettlementBySettlementIds(Long[] settlementIds);

    /**
     * 删除服务商结算管理信息
     * 
     * @param settlementId 服务商结算管理主键
     * @return 结果
     */
    public int deleteTProviderSettlementBySettlementId(Long settlementId);
}
