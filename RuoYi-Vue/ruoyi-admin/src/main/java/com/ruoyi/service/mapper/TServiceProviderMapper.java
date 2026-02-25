package com.ruoyi.service.mapper;

import java.util.List;
import com.ruoyi.service.domain.TServiceProvider;

/**
 * 服务商管理Mapper接口
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public interface TServiceProviderMapper 
{
    /**
     * 查询服务商管理
     * 
     * @param providerId 服务商管理主键
     * @return 服务商管理
     */
    public TServiceProvider selectTServiceProviderByProviderId(Long providerId);

    /**
     * 查询服务商管理列表
     * 
     * @param tServiceProvider 服务商管理
     * @return 服务商管理集合
     */
    public List<TServiceProvider> selectTServiceProviderList(TServiceProvider tServiceProvider);

    /**
     * 新增服务商管理
     * 
     * @param tServiceProvider 服务商管理
     * @return 结果
     */
    public int insertTServiceProvider(TServiceProvider tServiceProvider);

    /**
     * 修改服务商管理
     * 
     * @param tServiceProvider 服务商管理
     * @return 结果
     */
    public int updateTServiceProvider(TServiceProvider tServiceProvider);

    /**
     * 删除服务商管理
     * 
     * @param providerId 服务商管理主键
     * @return 结果
     */
    public int deleteTServiceProviderByProviderId(Long providerId);

    /**
     * 批量删除服务商管理
     * 
     * @param providerIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTServiceProviderByProviderIds(Long[] providerIds);
}
