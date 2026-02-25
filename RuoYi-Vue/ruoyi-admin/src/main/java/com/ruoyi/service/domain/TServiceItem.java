package com.ruoyi.service.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 服务项目管理对象 t_service_item
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public class TServiceItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 服务项目id */
    @Excel(name = "服务项目id")
    private Long itemId;

    /** 关联服务商ID */
    @Excel(name = "关联服务商ID")
    private Long providerId;

    /** 服务项目名称 */
    @Excel(name = "服务项目名称")
    private String itemName;

    /** 服务类别 */
    @Excel(name = "服务类别")
    private String itemCategory;

    /** 服务项目描述 */
    private String itemDescription;

    /** 标准价格（元） */
    @Excel(name = "标准价格", readConverterExp = "元=")
    private BigDecimal standardPrice;

    /** 服务时长（分钟） */
    @Excel(name = "服务时长", readConverterExp = "分=钟")
    private Long serviceDuration;

    /** 服务单位（次、小时、天等） */
    @Excel(name = "服务单位", readConverterExp = "次=、小时、天等")
    private String serviceUnit;

    /** 上架状态 */
    @Excel(name = "上架状态")
    private Long status;

    /** 逻辑删除 */
    private Long isDeleted;

    public void setItemId(Long itemId) 
    {
        this.itemId = itemId;
    }

    public Long getItemId() 
    {
        return itemId;
    }

    public void setProviderId(Long providerId) 
    {
        this.providerId = providerId;
    }

    public Long getProviderId() 
    {
        return providerId;
    }

    public void setItemName(String itemName) 
    {
        this.itemName = itemName;
    }

    public String getItemName() 
    {
        return itemName;
    }

    public void setItemCategory(String itemCategory) 
    {
        this.itemCategory = itemCategory;
    }

    public String getItemCategory() 
    {
        return itemCategory;
    }

    public void setItemDescription(String itemDescription) 
    {
        this.itemDescription = itemDescription;
    }

    public String getItemDescription() 
    {
        return itemDescription;
    }

    public void setStandardPrice(BigDecimal standardPrice) 
    {
        this.standardPrice = standardPrice;
    }

    public BigDecimal getStandardPrice() 
    {
        return standardPrice;
    }

    public void setServiceDuration(Long serviceDuration) 
    {
        this.serviceDuration = serviceDuration;
    }

    public Long getServiceDuration() 
    {
        return serviceDuration;
    }

    public void setServiceUnit(String serviceUnit) 
    {
        this.serviceUnit = serviceUnit;
    }

    public String getServiceUnit() 
    {
        return serviceUnit;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
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
            .append("itemId", getItemId())
            .append("providerId", getProviderId())
            .append("itemName", getItemName())
            .append("itemCategory", getItemCategory())
            .append("itemDescription", getItemDescription())
            .append("standardPrice", getStandardPrice())
            .append("serviceDuration", getServiceDuration())
            .append("serviceUnit", getServiceUnit())
            .append("status", getStatus())
            .append("isDeleted", getIsDeleted())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
