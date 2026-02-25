package com.ruoyi.health.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用药记录管理对象 t_medication_log
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public class TMedicationLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 记录id */
    @Excel(name = "记录id")
    private Long logId;

    /** 关联用药提醒ID */
    @Excel(name = "关联用药提醒ID")
    private Long reminderId;

    /** 关联老人ID */
    @Excel(name = "关联老人ID")
    private Long elderlyId;

    /** 药品名称 */
    @Excel(name = "药品名称")
    private String medicationName;

    /** 计划服药时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划服药时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date scheduledTime;

    /** 实际服药确认时间 */
    private Date takenTime;

    /** 是否已服药 */
    @Excel(name = "是否已服药")
    private Long isTaken;

    /** 跳过原因 */
    private String skipReason;

    /** 逻辑删除 */
    private Long isDeleted;

    public void setLogId(Long logId) 
    {
        this.logId = logId;
    }

    public Long getLogId() 
    {
        return logId;
    }

    public void setReminderId(Long reminderId) 
    {
        this.reminderId = reminderId;
    }

    public Long getReminderId() 
    {
        return reminderId;
    }

    public void setElderlyId(Long elderlyId) 
    {
        this.elderlyId = elderlyId;
    }

    public Long getElderlyId() 
    {
        return elderlyId;
    }

    public void setMedicationName(String medicationName) 
    {
        this.medicationName = medicationName;
    }

    public String getMedicationName() 
    {
        return medicationName;
    }

    public void setScheduledTime(Date scheduledTime) 
    {
        this.scheduledTime = scheduledTime;
    }

    public Date getScheduledTime() 
    {
        return scheduledTime;
    }

    public void setTakenTime(Date takenTime) 
    {
        this.takenTime = takenTime;
    }

    public Date getTakenTime() 
    {
        return takenTime;
    }

    public void setIsTaken(Long isTaken) 
    {
        this.isTaken = isTaken;
    }

    public Long getIsTaken() 
    {
        return isTaken;
    }

    public void setSkipReason(String skipReason) 
    {
        this.skipReason = skipReason;
    }

    public String getSkipReason() 
    {
        return skipReason;
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
            .append("logId", getLogId())
            .append("reminderId", getReminderId())
            .append("elderlyId", getElderlyId())
            .append("medicationName", getMedicationName())
            .append("scheduledTime", getScheduledTime())
            .append("takenTime", getTakenTime())
            .append("isTaken", getIsTaken())
            .append("skipReason", getSkipReason())
            .append("isDeleted", getIsDeleted())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
