package com.ruoyi.service.service;

import java.util.List;
import com.ruoyi.service.domain.TServiceItem;

/**
 * 服务项目管理Service接口
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public interface ITServiceItemService 
{
    /**
     * 查询服务项目管理
     * 
     * @param itemId 服务项目管理主键
     * @return 服务项目管理
     */
    public TServiceItem selectTServiceItemByItemId(Long itemId);

    /**
     * 查询服务项目管理列表
     * 
     * @param tServiceItem 服务项目管理
     * @return 服务项目管理集合
     */
    public List<TServiceItem> selectTServiceItemList(TServiceItem tServiceItem);

    /**
     * 新增服务项目管理
     * 
     * @param tServiceItem 服务项目管理
     * @return 结果
     */
    public int insertTServiceItem(TServiceItem tServiceItem);

    /**
     * 修改服务项目管理
     * 
     * @param tServiceItem 服务项目管理
     * @return 结果
     */
    public int updateTServiceItem(TServiceItem tServiceItem);

    /**
     * 批量删除服务项目管理
     * 
     * @param itemIds 需要删除的服务项目管理主键集合
     * @return 结果
     */
    public int deleteTServiceItemByItemIds(Long[] itemIds);

    /**
     * 删除服务项目管理信息
     * 
     * @param itemId 服务项目管理主键
     * @return 结果
     */
    public int deleteTServiceItemByItemId(Long itemId);
}
