package com.ruoyi.elderly.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 老人慢病关联管理对象 t_elderly_chronic
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public class TElderlyChronic extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 唯一标识 */
    @Excel(name = "唯一标识")
    private Long id;

    /** 关联老人ID */
    @Excel(name = "关联老人ID")
    private Long elderlyId;

    /** 慢病类型 */
    @Excel(name = "慢病类型")
    private String chronicType;

    /** 确诊日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "确诊日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date diagnosisDate;

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

    public void setChronicType(String chronicType) 
    {
        this.chronicType = chronicType;
    }

    public String getChronicType() 
    {
        return chronicType;
    }

    public void setDiagnosisDate(Date diagnosisDate) 
    {
        this.diagnosisDate = diagnosisDate;
    }

    public Date getDiagnosisDate() 
    {
        return diagnosisDate;
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
            .append("chronicType", getChronicType())
            .append("diagnosisDate", getDiagnosisDate())
            .append("isDeleted", getIsDeleted())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
