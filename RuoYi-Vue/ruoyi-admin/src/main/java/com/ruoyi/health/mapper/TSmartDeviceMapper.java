package com.ruoyi.health.mapper;

import java.util.List;
import com.ruoyi.health.domain.TSmartDevice;

/**
 * 智能设备管理Mapper接口
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public interface TSmartDeviceMapper 
{
    /**
     * 查询智能设备管理
     * 
     * @param deviceId 智能设备管理主键
     * @return 智能设备管理
     */
    public TSmartDevice selectTSmartDeviceByDeviceId(Long deviceId);

    /**
     * 查询智能设备管理列表
     * 
     * @param tSmartDevice 智能设备管理
     * @return 智能设备管理集合
     */
    public List<TSmartDevice> selectTSmartDeviceList(TSmartDevice tSmartDevice);

    /**
     * 新增智能设备管理
     * 
     * @param tSmartDevice 智能设备管理
     * @return 结果
     */
    public int insertTSmartDevice(TSmartDevice tSmartDevice);

    /**
     * 修改智能设备管理
     * 
     * @param tSmartDevice 智能设备管理
     * @return 结果
     */
    public int updateTSmartDevice(TSmartDevice tSmartDevice);

    /**
     * 删除智能设备管理
     * 
     * @param deviceId 智能设备管理主键
     * @return 结果
     */
    public int deleteTSmartDeviceByDeviceId(Long deviceId);

    /**
     * 批量删除智能设备管理
     * 
     * @param deviceIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTSmartDeviceByDeviceIds(Long[] deviceIds);
}
