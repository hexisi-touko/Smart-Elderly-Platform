package com.ruoyi.safety.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 安全预警管理对象 t_safety_alert
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public class TSafetyAlert extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 预警id */
    @Excel(name = "预警id")
    private Long alertId;

    /** 关联老人ID */
    @Excel(name = "关联老人ID")
    private Long elderlyId;

    /** 关联触发预警的设备ID */
    @Excel(name = "关联触发预警的设备ID")
    private Long deviceId;

    /** 预警类型 */
    @Excel(name = "预警类型")
    private String alertType;

    /** 预警触发时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预警触发时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date alertTime;

    /** 服务商接单时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "服务商接单时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date responseTime;

    /** 处置完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "处置完成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date completeTime;

    /** 子女一键催促次数 */
    @Excel(name = "子女一键催促次数")
    private Long urgeCount;

    /** 处理人ID */
    @Excel(name = "处理人ID")
    private Long handlerId;

    /** 处理人角色 */
    @Excel(name = "处理人角色")
    private Long handlerRole;

    /** 预警地点经度 */
    private BigDecimal alertLng;

    /** 预警地点纬度 */
    private BigDecimal alertLat;

    /** 预警地点文字描述 */
    @Excel(name = "预警地点文字描述")
    private String alertAddress;

    /** 预警状态 */
    @Excel(name = "预警状态")
    private Long alertStatus;

    /** 处置结果描述 */
    private String handleResult;

    /** 急救系统订单号 */
    private String emergencyOrderId;

    /** 逻辑删除 */
    private Long isDeleted;

    public void setAlertId(Long alertId) 
    {
        this.alertId = alertId;
    }

    public Long getAlertId() 
    {
        return alertId;
    }

    public void setElderlyId(Long elderlyId) 
    {
        this.elderlyId = elderlyId;
    }

    public Long getElderlyId() 
    {
        return elderlyId;
    }

    public void setDeviceId(Long deviceId) 
    {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() 
    {
        return deviceId;
    }

    public void setAlertType(String alertType) 
    {
        this.alertType = alertType;
    }

    public String getAlertType() 
    {
        return alertType;
    }

    public void setAlertTime(Date alertTime) 
    {
        this.alertTime = alertTime;
    }

    public Date getAlertTime() 
    {
        return alertTime;
    }

    public void setResponseTime(Date responseTime) 
    {
        this.responseTime = responseTime;
    }

    public Date getResponseTime() 
    {
        return responseTime;
    }

    public void setCompleteTime(Date completeTime) 
    {
        this.completeTime = completeTime;
    }

    public Date getCompleteTime() 
    {
        return completeTime;
    }

    public void setUrgeCount(Long urgeCount) 
    {
        this.urgeCount = urgeCount;
    }

    public Long getUrgeCount() 
    {
        return urgeCount;
    }

    public void setHandlerId(Long handlerId) 
    {
        this.handlerId = handlerId;
    }

    public Long getHandlerId() 
    {
        return handlerId;
    }

    public void setHandlerRole(Long handlerRole) 
    {
        this.handlerRole = handlerRole;
    }

    public Long getHandlerRole() 
    {
        return handlerRole;
    }

    public void setAlertLng(BigDecimal alertLng) 
    {
        this.alertLng = alertLng;
    }

    public BigDecimal getAlertLng() 
    {
        return alertLng;
    }

    public void setAlertLat(BigDecimal alertLat) 
    {
        this.alertLat = alertLat;
    }

    public BigDecimal getAlertLat() 
    {
        return alertLat;
    }

    public void setAlertAddress(String alertAddress) 
    {
        this.alertAddress = alertAddress;
    }

    public String getAlertAddress() 
    {
        return alertAddress;
    }

    public void setAlertStatus(Long alertStatus) 
    {
        this.alertStatus = alertStatus;
    }

    public Long getAlertStatus() 
    {
        return alertStatus;
    }

    public void setHandleResult(String handleResult) 
    {
        this.handleResult = handleResult;
    }

    public String getHandleResult() 
    {
        return handleResult;
    }

    public void setEmergencyOrderId(String emergencyOrderId) 
    {
        this.emergencyOrderId = emergencyOrderId;
    }

    public String getEmergencyOrderId() 
    {
        return emergencyOrderId;
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
            .append("alertId", getAlertId())
            .append("elderlyId", getElderlyId())
            .append("deviceId", getDeviceId())
            .append("alertType", getAlertType())
            .append("alertTime", getAlertTime())
            .append("responseTime", getResponseTime())
            .append("completeTime", getCompleteTime())
            .append("urgeCount", getUrgeCount())
            .append("handlerId", getHandlerId())
            .append("handlerRole", getHandlerRole())
            .append("alertLng", getAlertLng())
            .append("alertLat", getAlertLat())
            .append("alertAddress", getAlertAddress())
            .append("alertStatus", getAlertStatus())
            .append("handleResult", getHandleResult())
            .append("emergencyOrderId", getEmergencyOrderId())
            .append("isDeleted", getIsDeleted())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
