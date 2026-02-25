package com.ruoyi.elderly.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 老人-监护人关联对象 t_elderly_guardian
 * 
 * @author zhangTing
 * @date 2026-02-24
 */
public class TElderlyGuardian extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 唯一标识 */
    private Long id;

    /** 关联老人ID */
    @Excel(name = "关联老人ID")
    private Long elderlyId;

    /** 关联监护人ID */
    @Excel(name = "关联监护人ID")
    private Long guardianId;

    /** 授权状态 */
    @Excel(name = "授权状态")
    private Long authorizationStatus;

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

    public void setElderlyId(Long elderlyId) 
    {
        this.elderlyId = elderlyId;
    }

    public Long getElderlyId() 
    {
        return elderlyId;
    }

    public void setGuardianId(Long guardianId) 
    {
        this.guardianId = guardianId;
    }

    public Long getGuardianId() 
    {
        return guardianId;
    }

    public void setAuthorizationStatus(Long authorizationStatus) 
    {
        this.authorizationStatus = authorizationStatus;
    }

    public Long getAuthorizationStatus() 
    {
        return authorizationStatus;
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
            .append("elderlyId", getElderlyId())
            .append("guardianId", getGuardianId())
            .append("authorizationStatus", getAuthorizationStatus())
            .append("isDeleted", getIsDeleted())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
