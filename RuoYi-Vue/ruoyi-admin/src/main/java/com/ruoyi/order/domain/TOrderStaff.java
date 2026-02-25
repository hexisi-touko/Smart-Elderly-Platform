package com.ruoyi.order.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 订单-服务人员关联对象 t_order_staff
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public class TOrderStaff extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 唯一标识 */
    @Excel(name = "唯一标识")
    private Long id;

    /** 关联订单ID */
    @Excel(name = "关联订单ID")
    private Long orderId;

    /** 关联服务人员ID */
    @Excel(name = "关联服务人员ID")
    private Long staffId;

    /** 是否主要服务人员 */
    @Excel(name = "是否主要服务人员")
    private Long isPrimary;

    /** 逻辑删除 */
    private Long isDeleted;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setOrderId(Long orderId) 
    {
        this.orderId = orderId;
    }

    public Long getOrderId() 
    {
        return orderId;
    }

    public void setStaffId(Long staffId) 
    {
        this.staffId = staffId;
    }

    public Long getStaffId() 
    {
        return staffId;
    }

    public void setIsPrimary(Long isPrimary) 
    {
        this.isPrimary = isPrimary;
    }

    public Long getIsPrimary() 
    {
        return isPrimary;
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
            .append("id", getId())
            .append("orderId", getOrderId())
            .append("staffId", getStaffId())
            .append("isPrimary", getIsPrimary())
            .append("isDeleted", getIsDeleted())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
