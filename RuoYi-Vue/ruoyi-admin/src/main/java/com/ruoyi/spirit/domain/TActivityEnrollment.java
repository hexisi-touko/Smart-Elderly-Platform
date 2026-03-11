package com.ruoyi.spirit.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 活动报名管理对象 t_activity_enrollment
 * 
 * @author zhangTing
 * @date 2026-03-11
 */
public class TActivityEnrollment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 唯一标识 */
    @Excel(name = "唯一标识")
    private Long id;

    /** 关联老人ID */
    @Excel(name = "关联老人ID")
    private Long elderlyId;

    /** 关联活动ID */
    @Excel(name = "关联活动ID")
    private Long activityId;

    /** 报名状态 */
    @Excel(name = "报名状态")
    private Long enrollmentStatus;

    /** 签到时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "签到时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date checkInTime;

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

    public void setActivityId(Long activityId) 
    {
        this.activityId = activityId;
    }

    public Long getActivityId() 
    {
        return activityId;
    }

    public void setEnrollmentStatus(Long enrollmentStatus) 
    {
        this.enrollmentStatus = enrollmentStatus;
    }

    public Long getEnrollmentStatus() 
    {
        return enrollmentStatus;
    }

    public void setCheckInTime(Date checkInTime) 
    {
        this.checkInTime = checkInTime;
    }

    public Date getCheckInTime() 
    {
        return checkInTime;
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
            .append("activityId", getActivityId())
            .append("enrollmentStatus", getEnrollmentStatus())
            .append("checkInTime", getCheckInTime())
            .append("isDeleted", getIsDeleted())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
