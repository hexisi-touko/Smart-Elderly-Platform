package com.ruoyi.safety.mapper;

import java.util.List;
import com.ruoyi.safety.domain.TSafetyZone;

/**
 * 安全区域/地理围栏Mapper接口
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public interface TSafetyZoneMapper 
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
     * 删除安全区域/地理围栏
     * 
     * @param zoneId 安全区域/地理围栏主键
     * @return 结果
     */
    public int deleteTSafetyZoneByZoneId(Long zoneId);

    /**
     * 批量删除安全区域/地理围栏
     * 
     * @param zoneIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTSafetyZoneByZoneIds(Long[] zoneIds);
}
