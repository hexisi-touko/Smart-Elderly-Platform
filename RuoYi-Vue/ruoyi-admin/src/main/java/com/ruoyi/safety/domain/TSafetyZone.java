package com.ruoyi.safety.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 安全区域/地理围栏对象 t_safety_zone
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public class TSafetyZone extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 安全区域id */
    @Excel(name = "安全区域id")
    private Long zoneId;

    /** 关联老人ID */
    @Excel(name = "关联老人ID")
    private Long elderlyId;

    /** 区域名称 */
    @Excel(name = "区域名称")
    private String zoneName;

    /** 区域中心经度 */
    @Excel(name = "区域中心经度")
    private BigDecimal centerLng;

    /** 区域中心纬度 */
    @Excel(name = "区域中心纬度")
    private BigDecimal centerLat;

    /** 区域半径（米） */
    @Excel(name = "区域半径", readConverterExp = "米=")
    private Long radius;

    /** 区域地址描述 */
    @Excel(name = "区域地址描述")
    private String zoneAddress;

    /** 是否启用 */
    @Excel(name = "是否启用")
    private Long isActive;

    /** 逻辑删除 */
    private Long isDeleted;

    public void setZoneId(Long zoneId) 
    {
        this.zoneId = zoneId;
    }

    public Long getZoneId() 
    {
        return zoneId;
    }

    public void setElderlyId(Long elderlyId) 
    {
        this.elderlyId = elderlyId;
    }

    public Long getElderlyId() 
    {
        return elderlyId;
    }

    public void setZoneName(String zoneName) 
    {
        this.zoneName = zoneName;
    }

    public String getZoneName() 
    {
        return zoneName;
    }

    public void setCenterLng(BigDecimal centerLng) 
    {
        this.centerLng = centerLng;
    }

    public BigDecimal getCenterLng() 
    {
        return centerLng;
    }

    public void setCenterLat(BigDecimal centerLat) 
    {
        this.centerLat = centerLat;
    }

    public BigDecimal getCenterLat() 
    {
        return centerLat;
    }

    public void setRadius(Long radius) 
    {
        this.radius = radius;
    }

    public Long getRadius() 
    {
        return radius;
    }

    public void setZoneAddress(String zoneAddress) 
    {
        this.zoneAddress = zoneAddress;
    }

    public String getZoneAddress() 
    {
        return zoneAddress;
    }

    public void setIsActive(Long isActive) 
    {
        this.isActive = isActive;
    }

    public Long getIsActive() 
    {
        return isActive;
    }

    public void setIsDeleted(Long isDeleted) 
    {
        this.isDeleted = isDeleted;
    }

    public Long getIsDeleted() 
    {
        return isDeleted;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("zoneId", getZoneId())
            .append("elderlyId", getElderlyId())
            .append("zoneName", getZoneName())
            .append("centerLng", getCenterLng())
            .append("centerLat", getCenterLat())
            .append("radius", getRadius())
            .append("zoneAddress", getZoneAddress())
            .append("isActive", getIsActive())
            .append("isDeleted", getIsDeleted())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
