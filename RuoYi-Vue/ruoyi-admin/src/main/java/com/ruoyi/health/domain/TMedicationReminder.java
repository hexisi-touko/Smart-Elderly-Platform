package com.ruoyi.health.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用药提醒管理对象 t_medication_reminder
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public class TMedicationReminder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 提醒id */
    @Excel(name = "提醒id")
    private Long reminderId;

    /** 关联老人ID */
    @Excel(name = "关联老人ID")
    private Long elderlyId;

    /** 药品名称 */
    @Excel(name = "药品名称")
    private String medicationName;

    /** 药品类型 */
    @Excel(name = "药品类型")
    private String medicationType;

    /** 用药剂量 */
    @Excel(name = "用药剂量")
    private String dosage;

    /** 用药频率 */
    @Excel(name = "用药频率")
    private String frequency;

    /** 提醒时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "提醒时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date reminderTime;

    /** 开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startDate;

    /** 结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endDate;

    /** 备注说明 */
    private String note;

    /** 提醒状态 */
    @Excel(name = "提醒状态")
    private Long status;

    /** 逻辑删除 */
    private Long isDeleted;

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

    public void setMedicationType(String medicationType) 
    {
        this.medicationType = medicationType;
    }

    public String getMedicationType() 
    {
        return medicationType;
    }

    public void setDosage(String dosage) 
    {
        this.dosage = dosage;
    }

    public String getDosage() 
    {
        return dosage;
    }

    public void setFrequency(String frequency) 
    {
        this.frequency = frequency;
    }

    public String getFrequency() 
    {
        return frequency;
    }

    public void setReminderTime(Date reminderTime) 
    {
        this.reminderTime = reminderTime;
    }

    public Date getReminderTime() 
    {
        return reminderTime;
    }

    public void setStartDate(Date startDate) 
    {
        this.startDate = startDate;
    }

    public Date getStartDate() 
    {
        return startDate;
    }

    public void setEndDate(Date endDate) 
    {
        this.endDate = endDate;
    }

    public Date getEndDate() 
    {
        return endDate;
    }

    public void setNote(String note) 
    {
        this.note = note;
    }

    public String getNote() 
    {
        return note;
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
            .append("reminderId", getReminderId())
            .append("elderlyId", getElderlyId())
            .append("medicationName", getMedicationName())
            .append("medicationType", getMedicationType())
            .append("dosage", getDosage())
            .append("frequency", getFrequency())
            .append("reminderTime", getReminderTime())
            .append("startDate", getStartDate())
            .append("endDate", getEndDate())
            .append("note", getNote())
            .append("status", getStatus())
            .append("isDeleted", getIsDeleted())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
