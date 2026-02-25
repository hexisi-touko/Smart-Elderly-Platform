package com.ruoyi.health.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 健康记录管理对象 t_health_record
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public class THealthRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 记录id */
    @Excel(name = "记录id")
    private Long recordId;

    /** 关联老人ID */
    @Excel(name = "关联老人ID")
    private Long elderlyId;

    /** 关联采集设备ID */
    @Excel(name = "关联采集设备ID")
    private Long deviceId;

    /** 数据采集时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "数据采集时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date collectTime;

    /** 记录类型 */
    @Excel(name = "记录类型")
    private String recordType;

    /** 采集方式 */
    @Excel(name = "采集方式")
    private Long collectMethod;

    /** 收缩压（mmHg），仅血压类型记录填写 */
    @Excel(name = "收缩压", readConverterExp = "m=mHg")
    private Long systolicBp;

    /** 舒张压（mmHg），仅血压类型记录填写 */
    @Excel(name = "舒张压", readConverterExp = "m=mHg")
    private Long diastolicBp;

    /** 血糖值（mmol/L） */
    @Excel(name = "血糖值", readConverterExp = "m=mol/L")
    private BigDecimal bloodSugar;

    /** 心率（次/分钟） */
    @Excel(name = "心率", readConverterExp = "次=/分钟")
    private Long heartRate;

    /** 体温（℃） */
    @Excel(name = "体温", readConverterExp = "℃=")
    private BigDecimal temperature;

    /** 血氧饱和度（%） */
    @Excel(name = "血氧饱和度", readConverterExp = "%=")
    private Long bloodOxygen;

    /** 数据状态 */
    @Excel(name = "数据状态")
    private Long dataStatus;

    /** 医院同步标识 */
    private String thirdPartySyncId;

    /** 备份标记 */
    private Long backupFlag;

    /** 逻辑删除 */
    private Long isDeleted;

    public void setRecordId(Long recordId) 
    {
        this.recordId = recordId;
    }

    public Long getRecordId() 
    {
        return recordId;
    }

    public void setElderlyId(Long elderlyId) 
    {
        this.elderlyId = elderlyId;
    }

    public Long getElderlyId() 
    {
        return elderlyId;
    }

    public void setDeviceId(Long deviceId) 
    {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() 
    {
        return deviceId;
    }

    public void setCollectTime(Date collectTime) 
    {
        this.collectTime = collectTime;
    }

    public Date getCollectTime() 
    {
        return collectTime;
    }

    public void setRecordType(String recordType) 
    {
        this.recordType = recordType;
    }

    public String getRecordType() 
    {
        return recordType;
    }

    public void setCollectMethod(Long collectMethod) 
    {
        this.collectMethod = collectMethod;
    }

    public Long getCollectMethod() 
    {
        return collectMethod;
    }

    public void setSystolicBp(Long systolicBp) 
    {
        this.systolicBp = systolicBp;
    }

    public Long getSystolicBp() 
    {
        return systolicBp;
    }

    public void setDiastolicBp(Long diastolicBp) 
    {
        this.diastolicBp = diastolicBp;
    }

    public Long getDiastolicBp() 
    {
        return diastolicBp;
    }

    public void setBloodSugar(BigDecimal bloodSugar) 
    {
        this.bloodSugar = bloodSugar;
    }

    public BigDecimal getBloodSugar() 
    {
        return bloodSugar;
    }

    public void setHeartRate(Long heartRate) 
    {
        this.heartRate = heartRate;
    }

    public Long getHeartRate() 
    {
        return heartRate;
    }

    public void setTemperature(BigDecimal temperature) 
    {
        this.temperature = temperature;
    }

    public BigDecimal getTemperature() 
    {
        return temperature;
    }

    public void setBloodOxygen(Long bloodOxygen) 
    {
        this.bloodOxygen = bloodOxygen;
    }

    public Long getBloodOxygen() 
    {
        return bloodOxygen;
    }

    public void setDataStatus(Long dataStatus) 
    {
        this.dataStatus = dataStatus;
    }

    public Long getDataStatus() 
    {
        return dataStatus;
    }

    public void setThirdPartySyncId(String thirdPartySyncId) 
    {
        this.thirdPartySyncId = thirdPartySyncId;
    }

    public String getThirdPartySyncId() 
    {
        return thirdPartySyncId;
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
            .append("recordId", getRecordId())
            .append("elderlyId", getElderlyId())
            .append("deviceId", getDeviceId())
            .append("collectTime", getCollectTime())
            .append("recordType", getRecordType())
            .append("collectMethod", getCollectMethod())
            .append("systolicBp", getSystolicBp())
            .append("diastolicBp", getDiastolicBp())
            .append("bloodSugar", getBloodSugar())
            .append("heartRate", getHeartRate())
            .append("temperature", getTemperature())
            .append("bloodOxygen", getBloodOxygen())
            .append("dataStatus", getDataStatus())
            .append("thirdPartySyncId", getThirdPartySyncId())
            .append("backupFlag", getBackupFlag())
            .append("isDeleted", getIsDeleted())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
