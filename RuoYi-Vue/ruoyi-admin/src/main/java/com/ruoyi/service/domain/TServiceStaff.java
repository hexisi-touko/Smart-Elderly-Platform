package com.ruoyi.service.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 服务人员管理对象 t_service_staff
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public class TServiceStaff extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 服务人员id */
    @Excel(name = "服务人员id")
    private Long staffId;

    /** 服务人员姓名 */
    @Excel(name = "服务人员姓名")
    private String staffName;

    /** 关联所属服务商ID */
    @Excel(name = "关联所属服务商ID")
    private Long providerId;

    /** 服务人员手机号 */
    @Excel(name = "服务人员手机号")
    private String phone;

    /** 身份证号 */
    private String idCard;

    /** 职业资格证书编号 */
    @Excel(name = "职业资格证书编号")
    private String certificate;

    /** 人员类型 */
    @Excel(name = "人员类型")
    private String staffType;

    /** 工作状态 */
    @Excel(name = "工作状态")
    private Long status;

    /** 逻辑删除 */
    private Long isDeleted;

    /** 服务商名称（非数据库字段，JOIN查询用） */
    @Excel(name = "服务商名称")
    private String providerName;

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setStaffType(String staffType) {
        this.staffType = staffType;
    }

    public String getStaffType() {
        return staffType;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getStatus() {
        return status;
    }

    public void setIsDeleted(Long isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getIsDeleted() {
        return isDeleted;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderName() {
        return providerName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("staffId", getStaffId())
                .append("staffName", getStaffName())
                .append("providerId", getProviderId())
                .append("providerName", getProviderName())
                .append("phone", getPhone())
                .append("idCard", getIdCard())
                .append("certificate", getCertificate())
                .append("staffType", getStaffType())
                .append("status", getStatus())
                .append("isDeleted", getIsDeleted())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
