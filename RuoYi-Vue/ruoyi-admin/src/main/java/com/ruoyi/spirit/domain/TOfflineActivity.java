package com.ruoyi.spirit.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 线下活动管理对象 t_offline_activity
 * 
 * @author zhangTing
 * @date 2026-03-11
 */
public class TOfflineActivity extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 活动id */
    @Excel(name = "活动id")
    private Long activityId;

    /** 活动名称 */
    @Excel(name = "活动名称")
    private String activityName;

    /** 活动类型 */
    @Excel(name = "活动类型")
    private String activityType;

    /** 活动描述 */
    private String activityDescription;

    /** 主办方 */
    @Excel(name = "主办方")
    private String organizer;

    /** 活动封面图片 */
    private String activityCover;

    /** 活动地点 */
    @Excel(name = "活动地点")
    private String activityLocation;

    /** 活动地点经度 */
    private BigDecimal activityLng;

    /** 活动地点纬度 */
    private BigDecimal activityLat;

    /** 最大参与人数 */
    @Excel(name = "最大参与人数")
    private Long maxParticipants;

    /** 当前报名人数 */
    @Excel(name = "当前报名人数")
    private Long currentParticipants;

    /** 活动开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "活动开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 活动结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "活动结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 活动状态 */
    @Excel(name = "活动状态")
    private Long status;

    /** 逻辑删除 */
    private Long isDeleted;

    public void setActivityId(Long activityId) 
    {
        this.activityId = activityId;
    }

    public Long getActivityId() 
    {
        return activityId;
    }

    public void setActivityName(String activityName) 
    {
        this.activityName = activityName;
    }

    public String getActivityName() 
    {
        return activityName;
    }

    public void setActivityType(String activityType) 
    {
        this.activityType = activityType;
    }

    public String getActivityType() 
    {
        return activityType;
    }

    public void setActivityDescription(String activityDescription) 
    {
        this.activityDescription = activityDescription;
    }

    public String getActivityDescription() 
    {
        return activityDescription;
    }

    public void setOrganizer(String organizer) 
    {
        this.organizer = organizer;
    }

    public String getOrganizer() 
    {
        return organizer;
    }

    public void setActivityCover(String activityCover) 
    {
        this.activityCover = activityCover;
    }

    public String getActivityCover() 
    {
        return activityCover;
    }

    public void setActivityLocation(String activityLocation) 
    {
        this.activityLocation = activityLocation;
    }

    public String getActivityLocation() 
    {
        return activityLocation;
    }

    public void setActivityLng(BigDecimal activityLng) 
    {
        this.activityLng = activityLng;
    }

    public BigDecimal getActivityLng() 
    {
        return activityLng;
    }

    public void setActivityLat(BigDecimal activityLat) 
    {
        this.activityLat = activityLat;
    }

    public BigDecimal getActivityLat() 
    {
        return activityLat;
    }

    public void setMaxParticipants(Long maxParticipants) 
    {
        this.maxParticipants = maxParticipants;
    }

    public Long getMaxParticipants() 
    {
        return maxParticipants;
    }

    public void setCurrentParticipants(Long currentParticipants) 
    {
        this.currentParticipants = currentParticipants;
    }

    public Long getCurrentParticipants() 
    {
        return currentParticipants;
    }

    public void setStartTime(Date startTime) 
    {
        this.startTime = startTime;
    }

    public Date getStartTime() 
    {
        return startTime;
    }

    public void setEndTime(Date endTime) 
    {
        this.endTime = endTime;
    }

    public Date getEndTime() 
    {
        return endTime;
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
            .append("activityId", getActivityId())
            .append("activityName", getActivityName())
            .append("activityType", getActivityType())
            .append("activityDescription", getActivityDescription())
            .append("organizer", getOrganizer())
            .append("activityCover", getActivityCover())
            .append("activityLocation", getActivityLocation())
            .append("activityLng", getActivityLng())
            .append("activityLat", getActivityLat())
            .append("maxParticipants", getMaxParticipants())
            .append("currentParticipants", getCurrentParticipants())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("status", getStatus())
            .append("isDeleted", getIsDeleted())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
