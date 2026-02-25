package com.ruoyi.safety.service;

import java.util.List;
import com.ruoyi.safety.domain.TSafetyZone;

/**
 * 安全区域/地理围栏Service接口
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public interface ITSafetyZoneService 
{
    /**
     * 查询安全区域/地理围栏
     * 
     * @param zoneId 安全区域/地理围栏主键
     * @return 安全区域/地理围栏
     */
    public TSafetyZone selectTSafetyZoneByZoneId(Long zoneId);

    /**
     * 查询安全区域/地理围栏列表
     * 
     * @param tSafetyZone 安全区域/地理围栏
     * @return 安全区域/地理围栏集合
     */
    public List<TSafetyZone> selectTSafetyZoneList(TSafetyZone tSafetyZone);

    /**
     * 新增安全区域/地理围栏
     * 
     * @param tSafetyZone 安全区域/地理围栏
     * @return 结果
     */
    public int insertTSafetyZone(TSafetyZone tSafetyZone);

    /**
     * 修改安全区域/地理围栏
     * 
     * @param tSafetyZone 安全区域/地理围栏
     * @return 结果
     */
    public int updateTSafetyZone(TSafetyZone tSafetyZone);

    /**
     * 批量删除安全区域/地理围栏
     * 
     * @param zoneIds 需要删除的安全区域/地理围栏主键集合
     * @return 结果
     */
    public int deleteTSafetyZoneByZoneIds(Long[] zoneIds);

    /**
     * 删除安全区域/地理围栏信息
     * 
     * @param zoneId 安全区域/地理围栏主键
     * @return 结果
     */
    public int deleteTSafetyZoneByZoneId(Long zoneId);
}
