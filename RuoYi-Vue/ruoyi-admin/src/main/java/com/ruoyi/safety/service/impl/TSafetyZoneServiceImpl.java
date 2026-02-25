package com.ruoyi.safety.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.safety.mapper.TSafetyZoneMapper;
import com.ruoyi.safety.domain.TSafetyZone;
import com.ruoyi.safety.service.ITSafetyZoneService;

/**
 * 安全区域/地理围栏Service业务层处理
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@Service
public class TSafetyZoneServiceImpl implements ITSafetyZoneService 
{
    @Autowired
    private TSafetyZoneMapper tSafetyZoneMapper;

    /**
     * 查询安全区域/地理围栏
     * 
     * @param zoneId 安全区域/地理围栏主键
     * @return 安全区域/地理围栏
     */
    @Override
    public TSafetyZone selectTSafetyZoneByZoneId(Long zoneId)
    {
        return tSafetyZoneMapper.selectTSafetyZoneByZoneId(zoneId);
    }

    /**
     * 查询安全区域/地理围栏列表
     * 
     * @param tSafetyZone 安全区域/地理围栏
     * @return 安全区域/地理围栏
     */
    @Override
    public List<TSafetyZone> selectTSafetyZoneList(TSafetyZone tSafetyZone)
    {
        return tSafetyZoneMapper.selectTSafetyZoneList(tSafetyZone);
    }

    /**
     * 新增安全区域/地理围栏
     * 
     * @param tSafetyZone 安全区域/地理围栏
     * @return 结果
     */
    @Override
    public int insertTSafetyZone(TSafetyZone tSafetyZone)
    {
        tSafetyZone.setCreateTime(DateUtils.getNowDate());
        return tSafetyZoneMapper.insertTSafetyZone(tSafetyZone);
    }

    /**
     * 修改安全区域/地理围栏
     * 
     * @param tSafetyZone 安全区域/地理围栏
     * @return 结果
     */
    @Override
    public int updateTSafetyZone(TSafetyZone tSafetyZone)
    {
        tSafetyZone.setUpdateTime(DateUtils.getNowDate());
        return tSafetyZoneMapper.updateTSafetyZone(tSafetyZone);
    }

    /**
     * 批量删除安全区域/地理围栏
     * 
     * @param zoneIds 需要删除的安全区域/地理围栏主键
     * @return 结果
     */
    @Override
    public int deleteTSafetyZoneByZoneIds(Long[] zoneIds)
    {
        return tSafetyZoneMapper.deleteTSafetyZoneByZoneIds(zoneIds);
    }

    /**
     * 删除安全区域/地理围栏信息
     * 
     * @param zoneId 安全区域/地理围栏主键
     * @return 结果
     */
    @Override
    public int deleteTSafetyZoneByZoneId(Long zoneId)
    {
        return tSafetyZoneMapper.deleteTSafetyZoneByZoneId(zoneId);
    }
}
