package com.ruoyi.service.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.service.mapper.TServiceItemMapper;
import com.ruoyi.service.domain.TServiceItem;
import com.ruoyi.service.service.ITServiceItemService;

/**
 * 服务项目管理Service业务层处理
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@Service
public class TServiceItemServiceImpl implements ITServiceItemService 
{
    @Autowired
    private TServiceItemMapper tServiceItemMapper;

    /**
     * 查询服务项目管理
     * 
     * @param itemId 服务项目管理主键
     * @return 服务项目管理
     */
    @Override
    public TServiceItem selectTServiceItemByItemId(Long itemId)
    {
        return tServiceItemMapper.selectTServiceItemByItemId(itemId);
    }

    /**
     * 查询服务项目管理列表
     * 
     * @param tServiceItem 服务项目管理
     * @return 服务项目管理
     */
    @Override
    public List<TServiceItem> selectTServiceItemList(TServiceItem tServiceItem)
    {
        return tServiceItemMapper.selectTServiceItemList(tServiceItem);
    }

    /**
     * 新增服务项目管理
     * 
     * @param tServiceItem 服务项目管理
     * @return 结果
     */
    @Override
    public int insertTServiceItem(TServiceItem tServiceItem)
    {
        tServiceItem.setCreateTime(DateUtils.getNowDate());
        return tServiceItemMapper.insertTServiceItem(tServiceItem);
    }

    /**
     * 修改服务项目管理
     * 
     * @param tServiceItem 服务项目管理
     * @return 结果
     */
    @Override
    public int updateTServiceItem(TServiceItem tServiceItem)
    {
        tServiceItem.setUpdateTime(DateUtils.getNowDate());
        return tServiceItemMapper.updateTServiceItem(tServiceItem);
    }

    /**
     * 批量删除服务项目管理
     * 
     * @param itemIds 需要删除的服务项目管理主键
     * @return 结果
     */
    @Override
    public int deleteTServiceItemByItemIds(Long[] itemIds)
    {
        return tServiceItemMapper.deleteTServiceItemByItemIds(itemIds);
    }

    /**
     * 删除服务项目管理信息
     * 
     * @param itemId 服务项目管理主键
     * @return 结果
     */
    @Override
    public int deleteTServiceItemByItemId(Long itemId)
    {
        return tServiceItemMapper.deleteTServiceItemByItemId(itemId);
    }
}
