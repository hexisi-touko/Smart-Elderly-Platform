package com.ruoyi.service.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 服务商管理对象 t_service_provider
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public class TServiceProvider extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 服务商id */
    @Excel(name = "服务商id")
    private Long providerId;

    /** 关联C端用户ID */
    @Excel(name = "关联C端用户ID")
    private Long userId;

    /** 服务商名称 */
    @Excel(name = "服务商名称")
    private String providerName;

    /** 资质许可证号 */
    @Excel(name = "资质许可证号")
    private String licenseCode;

    /** 联系人姓名 */
    @Excel(name = "联系人姓名")
    private String contactPerson;

    /** 联系人手机号（需 AES 加密） */
    @Excel(name = "联系人手机号", readConverterExp = "需=,A=ES,加=密")
    private String contactPhone;

    /** 服务商办公地址 */
    private String address;

    /** 营业执照编号 */
    private String businessLicense;

    /** 审核状态 */
    @Excel(name = "审核状态")
    private Long auditStatus;

    /** 服务范围 */
    @Excel(name = "服务范围")
    private String serviceScope;

    /** 服务商综合评分 */
    @Excel(name = "服务商综合评分")
    private BigDecimal score;

    /** 结算银行账号 */
    private String bankAccount;

    /** 逻辑删除 */
    private Long isDeleted;

    public void setProviderId(Long providerId) 
    {
        this.providerId = providerId;
    }

    public Long getProviderId() 
    {
        return providerId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setProviderName(String providerName) 
    {
        this.providerName = providerName;
    }

    public String getProviderName() 
    {
        return providerName;
    }

    public void setLicenseCode(String licenseCode) 
    {
        this.licenseCode = licenseCode;
    }

    public String getLicenseCode() 
    {
        return licenseCode;
    }

    public void setContactPerson(String contactPerson) 
    {
        this.contactPerson = contactPerson;
    }

    public String getContactPerson() 
    {
        return contactPerson;
    }

    public void setContactPhone(String contactPhone) 
    {
        this.contactPhone = contactPhone;
    }

    public String getContactPhone() 
    {
        return contactPhone;
    }

    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }

    public void setBusinessLicense(String businessLicense) 
    {
        this.businessLicense = businessLicense;
    }

    public String getBusinessLicense() 
    {
        return businessLicense;
    }

    public void setAuditStatus(Long auditStatus) 
    {
        this.auditStatus = auditStatus;
    }

    public Long getAuditStatus() 
    {
        return auditStatus;
    }

    public void setServiceScope(String serviceScope) 
    {
        this.serviceScope = serviceScope;
    }

    public String getServiceScope() 
    {
        return serviceScope;
    }

    public void setScore(BigDecimal score) 
    {
        this.score = score;
    }

    public BigDecimal getScore() 
    {
        return score;
    }

    public void setBankAccount(String bankAccount) 
    {
        this.bankAccount = bankAccount;
    }

    public String getBankAccount() 
    {
        return bankAccount;
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
            .append("providerId", getProviderId())
            .append("userId", getUserId())
            .append("providerName", getProviderName())
            .append("licenseCode", getLicenseCode())
            .append("contactPerson", getContactPerson())
            .append("contactPhone", getContactPhone())
            .append("address", getAddress())
            .append("businessLicense", getBusinessLicense())
            .append("auditStatus", getAuditStatus())
            .append("serviceScope", getServiceScope())
            .append("score", getScore())
            .append("bankAccount", getBankAccount())
            .append("isDeleted", getIsDeleted())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
