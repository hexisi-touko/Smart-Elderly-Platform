package com.ruoyi.health.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.health.mapper.TSmartDeviceMapper;
import com.ruoyi.health.domain.TSmartDevice;
import com.ruoyi.health.service.ITSmartDeviceService;

/**
 * 智能设备管理Service业务层处理
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@Service
public class TSmartDeviceServiceImpl implements ITSmartDeviceService 
{
    @Autowired
    private TSmartDeviceMapper tSmartDeviceMapper;

    /**
     * 查询智能设备管理
     * 
     * @param deviceId 智能设备管理主键
     * @return 智能设备管理
     */
    @Override
    public TSmartDevice selectTSmartDeviceByDeviceId(Long deviceId)
    {
        return tSmartDeviceMapper.selectTSmartDeviceByDeviceId(deviceId);
    }

    /**
     * 查询智能设备管理列表
     * 
     * @param tSmartDevice 智能设备管理
     * @return 智能设备管理
     */
    @Override
    public List<TSmartDevice> selectTSmartDeviceList(TSmartDevice tSmartDevice)
    {
        return tSmartDeviceMapper.selectTSmartDeviceList(tSmartDevice);
    }

    /**
     * 新增智能设备管理
     * 
     * @param tSmartDevice 智能设备管理
     * @return 结果
     */
    @Override
    public int insertTSmartDevice(TSmartDevice tSmartDevice)
    {
        tSmartDevice.setCreateTime(DateUtils.getNowDate());
        return tSmartDeviceMapper.insertTSmartDevice(tSmartDevice);
    }

    /**
     * 修改智能设备管理
     * 
     * @param tSmartDevice 智能设备管理
     * @return 结果
     */
    @Override
    public int updateTSmartDevice(TSmartDevice tSmartDevice)
    {
        tSmartDevice.setUpdateTime(DateUtils.getNowDate());
        return tSmartDeviceMapper.updateTSmartDevice(tSmartDevice);
    }

    /**
     * 批量删除智能设备管理
     * 
     * @param deviceIds 需要删除的智能设备管理主键
     * @return 结果
     */
    @Override
    public int deleteTSmartDeviceByDeviceIds(Long[] deviceIds)
    {
        return tSmartDeviceMapper.deleteTSmartDeviceByDeviceIds(deviceIds);
    }

    /**
     * 删除智能设备管理信息
     * 
     * @param deviceId 智能设备管理主键
     * @return 结果
     */
    @Override
    public int deleteTSmartDeviceByDeviceId(Long deviceId)
    {
        return tSmartDeviceMapper.deleteTSmartDeviceByDeviceId(deviceId);
    }
}
