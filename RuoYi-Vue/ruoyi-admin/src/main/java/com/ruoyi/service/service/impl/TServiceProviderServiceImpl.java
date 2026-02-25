package com.ruoyi.service.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.service.mapper.TServiceProviderMapper;
import com.ruoyi.service.domain.TServiceProvider;
import com.ruoyi.service.service.ITServiceProviderService;

/**
 * 服务商管理Service业务层处理
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@Service
public class TServiceProviderServiceImpl implements ITServiceProviderService 
{
    @Autowired
    private TServiceProviderMapper tServiceProviderMapper;

    /**
     * 查询服务商管理
     * 
     * @param providerId 服务商管理主键
     * @return 服务商管理
     */
    @Override
    public TServiceProvider selectTServiceProviderByProviderId(Long providerId)
    {
        return tServiceProviderMapper.selectTServiceProviderByProviderId(providerId);
    }

    /**
     * 查询服务商管理列表
     * 
     * @param tServiceProvider 服务商管理
     * @return 服务商管理
     */
    @Override
    public List<TServiceProvider> selectTServiceProviderList(TServiceProvider tServiceProvider)
    {
        return tServiceProviderMapper.selectTServiceProviderList(tServiceProvider);
    }

    /**
     * 新增服务商管理
     * 
     * @param tServiceProvider 服务商管理
     * @return 结果
     */
    @Override
    public int insertTServiceProvider(TServiceProvider tServiceProvider)
    {
        tServiceProvider.setCreateTime(DateUtils.getNowDate());
        return tServiceProviderMapper.insertTServiceProvider(tServiceProvider);
    }

    /**
     * 修改服务商管理
     * 
     * @param tServiceProvider 服务商管理
     * @return 结果
     */
    @Override
    public int updateTServiceProvider(TServiceProvider tServiceProvider)
    {
        tServiceProvider.setUpdateTime(DateUtils.getNowDate());
        return tServiceProviderMapper.updateTServiceProvider(tServiceProvider);
    }

    /**
     * 批量删除服务商管理
     * 
     * @param providerIds 需要删除的服务商管理主键
     * @return 结果
     */
    @Override
    public int deleteTServiceProviderByProviderIds(Long[] providerIds)
    {
        return tServiceProviderMapper.deleteTServiceProviderByProviderIds(providerIds);
    }

    /**
     * 删除服务商管理信息
     * 
     * @param providerId 服务商管理主键
     * @return 结果
     */
    @Override
    public int deleteTServiceProviderByProviderId(Long providerId)
    {
        return tServiceProviderMapper.deleteTServiceProviderByProviderId(providerId);
    }
}
