package com.ruoyi.health.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 体检预约对象 t_physical_exam_reservation
 * 
 * @author zhangTing
 * @date 2026-03-11
 */
public class TPhysicalExamReservation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 预约id */
    @Excel(name = "预约id")
    private Long reservationId;

    /** 关联老人ID */
    @Excel(name = "关联老人ID")
    private Long elderlyId;

    /** 体检类型 */
    @Excel(name = "体检类型")
    private String examType;

    /** 体检机构名称 */
    @Excel(name = "体检机构名称")
    private String examInstitution;

    /** 第三方体检机构ID */
    private String thirdPartyOrgId;

    /** 预约时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预约时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date reservationTime;

    /** 体检地址 */
    @Excel(name = "体检地址")
    private String examAddress;

    /** 体检项目清单 */
    private String examItems;

    /** 预约状态 */
    @Excel(name = "预约状态")
    private Long reservationStatus;

    /** 报告状态 */
    @Excel(name = "报告状态")
    private Long reportStatus;

    /** 体检报告PDF链接 */
    private String reportUrl;

    /** 报告解读内容 */
    private String reportInterpret;

    /** 逻辑删除 */
    private Long isDeleted;

    public void setReservationId(Long reservationId) 
    {
        this.reservationId = reservationId;
    }

    public Long getReservationId() 
    {
        return reservationId;
    }

    public void setElderlyId(Long elderlyId) 
    {
        this.elderlyId = elderlyId;
    }

    public Long getElderlyId() 
    {
        return elderlyId;
    }

    public void setExamType(String examType) 
    {
        this.examType = examType;
    }

    public String getExamType() 
    {
        return examType;
    }

    public void setExamInstitution(String examInstitution) 
    {
        this.examInstitution = examInstitution;
    }

    public String getExamInstitution() 
    {
        return examInstitution;
    }

    public void setThirdPartyOrgId(String thirdPartyOrgId) 
    {
        this.thirdPartyOrgId = thirdPartyOrgId;
    }

    public String getThirdPartyOrgId() 
    {
        return thirdPartyOrgId;
    }

    public void setReservationTime(Date reservationTime) 
    {
        this.reservationTime = reservationTime;
    }

    public Date getReservationTime() 
    {
        return reservationTime;
    }

    public void setExamAddress(String examAddress) 
    {
        this.examAddress = examAddress;
    }

    public String getExamAddress() 
    {
        return examAddress;
    }

    public void setExamItems(String examItems) 
    {
        this.examItems = examItems;
    }

    public String getExamItems() 
    {
        return examItems;
    }

    public void setReservationStatus(Long reservationStatus) 
    {
        this.reservationStatus = reservationStatus;
    }

    public Long getReservationStatus() 
    {
        return reservationStatus;
    }

    public void setReportStatus(Long reportStatus) 
    {
        this.reportStatus = reportStatus;
    }

    public Long getReportStatus() 
    {
        return reportStatus;
    }

    public void setReportUrl(String reportUrl) 
    {
        this.reportUrl = reportUrl;
    }

    public String getReportUrl() 
    {
        return reportUrl;
    }

    public void setReportInterpret(String reportInterpret) 
    {
        this.reportInterpret = reportInterpret;
    }

    public String getReportInterpret() 
    {
        return reportInterpret;
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
            .append("reservationId", getReservationId())
            .append("elderlyId", getElderlyId())
            .append("examType", getExamType())
            .append("examInstitution", getExamInstitution())
            .append("thirdPartyOrgId", getThirdPartyOrgId())
            .append("reservationTime", getReservationTime())
            .append("examAddress", getExamAddress())
            .append("examItems", getExamItems())
            .append("reservationStatus", getReservationStatus())
            .append("reportStatus", getReportStatus())
            .append("reportUrl", getReportUrl())
            .append("reportInterpret", getReportInterpret())
            .append("isDeleted", getIsDeleted())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
