package com.ruoyi.order.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 服务订单对象 t_service_order
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public class TServiceOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订单id */
    @Excel(name = "订单id")
    private Long orderId;

    /** 关联老人ID */
    private Long elderlyId;

    /** 关联服务商ID */
    private Long providerId;

    /** 关联服务项目ID */
    private Long serviceItemId;

    /** 订单编号 */
    @Excel(name = "订单编号")
    private String orderNo;

    /** 订单金额 */
    private BigDecimal orderAmount;

    /** 约定服务时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "约定服务时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date serviceTime;

    /** 服务地址 */
    @Excel(name = "服务地址")
    private String serviceAddress;

    /** 服务要求说明 */
    @Excel(name = "服务要求说明")
    private String serviceRequirements;

    /** 订单状态 */
    @Excel(name = "订单状态")
    private Long orderStatus;

    /** 接单时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "接单时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date acceptTime;

    /** 服务开始时间 */
    private Date startTime;

    /** 服务完成时间 */
    private Date completeTime;

    /** 取消原因 */
    private String cancelReason;

    /** 逻辑删除 */
    private Long isDeleted;

    public void setOrderId(Long orderId) 
    {
        this.orderId = orderId;
    }

    public Long getOrderId() 
    {
        return orderId;
    }

    public void setElderlyId(Long elderlyId) 
    {
        this.elderlyId = elderlyId;
    }

    public Long getElderlyId() 
    {
        return elderlyId;
    }

    public void setProviderId(Long providerId) 
    {
        this.providerId = providerId;
    }

    public Long getProviderId() 
    {
        return providerId;
    }

    public void setServiceItemId(Long serviceItemId) 
    {
        this.serviceItemId = serviceItemId;
    }

    public Long getServiceItemId() 
    {
        return serviceItemId;
    }

    public void setOrderNo(String orderNo) 
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo() 
    {
        return orderNo;
    }

    public void setOrderAmount(BigDecimal orderAmount) 
    {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getOrderAmount() 
    {
        return orderAmount;
    }

    public void setServiceTime(Date serviceTime) 
    {
        this.serviceTime = serviceTime;
    }

    public Date getServiceTime() 
    {
        return serviceTime;
    }

    public void setServiceAddress(String serviceAddress) 
    {
        this.serviceAddress = serviceAddress;
    }

    public String getServiceAddress() 
    {
        return serviceAddress;
    }

    public void setServiceRequirements(String serviceRequirements) 
    {
        this.serviceRequirements = serviceRequirements;
    }

    public String getServiceRequirements() 
    {
        return serviceRequirements;
    }

    public void setOrderStatus(Long orderStatus) 
    {
        this.orderStatus = orderStatus;
    }

    public Long getOrderStatus() 
    {
        return orderStatus;
    }

    public void setAcceptTime(Date acceptTime) 
    {
        this.acceptTime = acceptTime;
    }

    public Date getAcceptTime() 
    {
        return acceptTime;
    }

    public void setStartTime(Date startTime) 
    {
        this.startTime = startTime;
    }

    public Date getStartTime() 
    {
        return startTime;
    }

    public void setCompleteTime(Date completeTime) 
    {
        this.completeTime = completeTime;
    }

    public Date getCompleteTime() 
    {
        return completeTime;
    }

    public void setCancelReason(String cancelReason) 
    {
        this.cancelReason = cancelReason;
    }

    public String getCancelReason() 
    {
        return cancelReason;
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
            .append("orderId", getOrderId())
            .append("elderlyId", getElderlyId())
            .append("providerId", getProviderId())
            .append("serviceItemId", getServiceItemId())
            .append("orderNo", getOrderNo())
            .append("orderAmount", getOrderAmount())
            .append("serviceTime", getServiceTime())
            .append("serviceAddress", getServiceAddress())
            .append("serviceRequirements", getServiceRequirements())
            .append("orderStatus", getOrderStatus())
            .append("acceptTime", getAcceptTime())
            .append("startTime", getStartTime())
            .append("completeTime", getCompleteTime())
            .append("cancelReason", getCancelReason())
            .append("isDeleted", getIsDeleted())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
