package com.ruoyi.spirit.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 老人-课程报名对象 t_elderly_course
 * 
 * @author zhangTing
 * @date 2026-03-11
 */
public class TElderlyCourse extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 唯一标识 */
    private Long id;

    /** 关联老人ID */
    @Excel(name = "关联老人ID")
    private Long elderlyId;

    /** 关联课程ID */
    @Excel(name = "关联课程ID")
    private Long courseId;

    /** 报名状态 */
    @Excel(name = "报名状态")
    private Long enrollmentStatus;

    /** 学习进度 */
    @Excel(name = "学习进度")
    private Long learningProgress;

    /** 报名时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "报名时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date enrollmentTime;

    /** 完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "完成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date completionTime;

    /** 逻辑删除 */
    private Long isDeleted;

    /** 老人姓名（关联查询） */
    @Excel(name = "老人姓名")
    private String elderlyName;

    /** 课程名称（关联查询） */
    @Excel(name = "课程名称")
    private String courseName;

    public String getElderlyName() { return elderlyName; }
    public void setElderlyName(String elderlyName) { this.elderlyName = elderlyName; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

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

    public void setCourseId(Long courseId) 
    {
        this.courseId = courseId;
    }

    public Long getCourseId() 
    {
        return courseId;
    }

    public void setEnrollmentStatus(Long enrollmentStatus) 
    {
        this.enrollmentStatus = enrollmentStatus;
    }

    public Long getEnrollmentStatus() 
    {
        return enrollmentStatus;
    }

    public void setLearningProgress(Long learningProgress) 
    {
        this.learningProgress = learningProgress;
    }

    public Long getLearningProgress() 
    {
        return learningProgress;
    }

    public void setEnrollmentTime(Date enrollmentTime) 
    {
        this.enrollmentTime = enrollmentTime;
    }

    public Date getEnrollmentTime() 
    {
        return enrollmentTime;
    }

    public void setCompletionTime(Date completionTime) 
    {
        this.completionTime = completionTime;
    }

    public Date getCompletionTime() 
    {
        return completionTime;
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
            .append("courseId", getCourseId())
            .append("enrollmentStatus", getEnrollmentStatus())
            .append("learningProgress", getLearningProgress())
            .append("enrollmentTime", getEnrollmentTime())
            .append("completionTime", getCompletionTime())
            .append("isDeleted", getIsDeleted())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
