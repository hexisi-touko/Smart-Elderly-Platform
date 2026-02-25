package com.ruoyi.health.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 健康阈值管理对象 t_health_threshold
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public class THealthThreshold extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 阈值id */
    @Excel(name = "阈值id")
    private Long thresholdId;

    /** 关联老人ID */
    @Excel(name = "关联老人ID")
    private Long elderlyId;

    /** 收缩压上限阈值（mmHg） */
    @Excel(name = "收缩压上限阈值", readConverterExp = "m=mHg")
    private Long sysBpMax;

    /** 收缩压下限阈值（mmHg） */
    @Excel(name = "收缩压下限阈值", readConverterExp = "m=mHg")
    private Long sysBpMin;

    /** 舒张压上限阈值（mmHg） */
    @Excel(name = "舒张压上限阈值", readConverterExp = "m=mHg")
    private Long diaBpMax;

    /** 舒张压下限阈值（mmHg） */
    @Excel(name = "舒张压下限阈值", readConverterExp = "m=mHg")
    private Long diaBpMin;

    /** 心率上限阈值（次/分钟） */
    @Excel(name = "心率上限阈值", readConverterExp = "次=/分钟")
    private Long heartRateMax;

    /** 心率下限阈值（次/分钟） */
    @Excel(name = "心率下限阈值", readConverterExp = "次=/分钟")
    private Long heartRateMin;

    /** 血糖上限阈值（mmol/L） */
    @Excel(name = "血糖上限阈值", readConverterExp = "m=mol/L")
    private BigDecimal bloodSugarMax;

    /** 血糖下限阈值（mmol/L） */
    @Excel(name = "血糖下限阈值", readConverterExp = "m=mol/L")
    private BigDecimal bloodSugarMin;

    /** 体温上限阈值（℃） */
    @Excel(name = "体温上限阈值", readConverterExp = "℃=")
    private BigDecimal temperatureMax;

    /** 体温下限阈值（℃） */
    @Excel(name = "体温下限阈值", readConverterExp = "℃=")
    private BigDecimal temperatureMin;

    /** 逻辑删除 */
    private Long isDeleted;

    public void setThresholdId(Long thresholdId) 
    {
        this.thresholdId = thresholdId;
    }

    public Long getThresholdId() 
    {
        return thresholdId;
    }

    public void setElderlyId(Long elderlyId) 
    {
        this.elderlyId = elderlyId;
    }

    public Long getElderlyId() 
    {
        return elderlyId;
    }

    public void setSysBpMax(Long sysBpMax) 
    {
        this.sysBpMax = sysBpMax;
    }

    public Long getSysBpMax() 
    {
        return sysBpMax;
    }

    public void setSysBpMin(Long sysBpMin) 
    {
        this.sysBpMin = sysBpMin;
    }

    public Long getSysBpMin() 
    {
        return sysBpMin;
    }

    public void setDiaBpMax(Long diaBpMax) 
    {
        this.diaBpMax = diaBpMax;
    }

    public Long getDiaBpMax() 
    {
        return diaBpMax;
    }

    public void setDiaBpMin(Long diaBpMin) 
    {
        this.diaBpMin = diaBpMin;
    }

    public Long getDiaBpMin() 
    {
        return diaBpMin;
    }

    public void setHeartRateMax(Long heartRateMax) 
    {
        this.heartRateMax = heartRateMax;
    }

    public Long getHeartRateMax() 
    {
        return heartRateMax;
    }

    public void setHeartRateMin(Long heartRateMin) 
    {
        this.heartRateMin = heartRateMin;
    }

    public Long getHeartRateMin() 
    {
        return heartRateMin;
    }

    public void setBloodSugarMax(BigDecimal bloodSugarMax) 
    {
        this.bloodSugarMax = bloodSugarMax;
    }

    public BigDecimal getBloodSugarMax() 
    {
        return bloodSugarMax;
    }

    public void setBloodSugarMin(BigDecimal bloodSugarMin) 
    {
        this.bloodSugarMin = bloodSugarMin;
    }

    public BigDecimal getBloodSugarMin() 
    {
        return bloodSugarMin;
    }

    public void setTemperatureMax(BigDecimal temperatureMax) 
    {
        this.temperatureMax = temperatureMax;
    }

    public BigDecimal getTemperatureMax() 
    {
        return temperatureMax;
    }

    public void setTemperatureMin(BigDecimal temperatureMin) 
    {
        this.temperatureMin = temperatureMin;
    }

    public BigDecimal getTemperatureMin() 
    {
        return temperatureMin;
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
            .append("thresholdId", getThresholdId())
            .append("elderlyId", getElderlyId())
            .append("sysBpMax", getSysBpMax())
            .append("sysBpMin", getSysBpMin())
            .append("diaBpMax", getDiaBpMax())
            .append("diaBpMin", getDiaBpMin())
            .append("heartRateMax", getHeartRateMax())
            .append("heartRateMin", getHeartRateMin())
            .append("bloodSugarMax", getBloodSugarMax())
            .append("bloodSugarMin", getBloodSugarMin())
            .append("temperatureMax", getTemperatureMax())
            .append("temperatureMin", getTemperatureMin())
            .append("isDeleted", getIsDeleted())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
