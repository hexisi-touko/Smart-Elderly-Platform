package com.ruoyi.elderly.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 老人基础信息对象 t_elderly
 * 
 * @author zhangTing
 * @date 2026-02-24
 */
public class TElderly extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 老人id */
    @Excel(name = "老人id")
    private Long elderlyId;

    /** 关联C端用户ID */
    @Excel(name = "关联C端用户ID")
    private Long userId;

    /** 老人姓名 */
    @Excel(name = "老人姓名")
    private String name;

    /** 身份证号 */
    private String idCard;

    /** 老人手机号 */
    @Excel(name = "老人手机号")
    private String phone;

    /** 性别 */
    @Excel(name = "性别")
    private Long gender;

    /** 出生日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出生日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date birthDate;

    /** 健康状态 */
    @Excel(name = "健康状态")
    private Long healthStatus;

    /** 居住地址 */
    @Excel(name = "居住地址")
    private String address;

    /** 紧急联系人姓名 */
    @Excel(name = "紧急联系人姓名")
    private String emergencyContactName;

    /** 紧急联系人电话 */
    @Excel(name = "紧急联系人电话")
    private String emergencyContactPhone;

    /** 备份标记 */
    private Long backupFlag;

    /** 逻辑删除 */
    private Long isDeleted;

    public void setElderlyId(Long elderlyId) 
    {
        this.elderlyId = elderlyId;
    }

    public Long getElderlyId() 
    {
        return elderlyId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setIdCard(String idCard) 
    {
        this.idCard = idCard;
    }

    public String getIdCard() 
    {
        return idCard;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }

    public void setGender(Long gender) 
    {
        this.gender = gender;
    }

    public Long getGender() 
    {
        return gender;
    }

    public void setBirthDate(Date birthDate) 
    {
        this.birthDate = birthDate;
    }

    public Date getBirthDate() 
    {
        return birthDate;
    }

    public void setHealthStatus(Long healthStatus) 
    {
        this.healthStatus = healthStatus;
    }

    public Long getHealthStatus() 
    {
        return healthStatus;
    }

    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }

    public void setEmergencyContactName(String emergencyContactName) 
    {
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmergencyContactName() 
    {
        return emergencyContactName;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) 
    {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public String getEmergencyContactPhone() 
    {
        return emergencyContactPhone;
    }

    public void setBackupFlag(Long backupFlag) 
    {
        this.backupFlag = backupFlag;
    }

    public Long getBackupFlag() 
    {
        return backupFlag;
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
            .append("elderlyId", getElderlyId())
            .append("userId", getUserId())
            .append("name", getName())
            .append("idCard", getIdCard())
            .append("phone", getPhone())
            .append("gender", getGender())
            .append("birthDate", getBirthDate())
            .append("healthStatus", getHealthStatus())
            .append("address", getAddress())
            .append("emergencyContactName", getEmergencyContactName())
            .append("emergencyContactPhone", getEmergencyContactPhone())
            .append("backupFlag", getBackupFlag())
            .append("isDeleted", getIsDeleted())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
