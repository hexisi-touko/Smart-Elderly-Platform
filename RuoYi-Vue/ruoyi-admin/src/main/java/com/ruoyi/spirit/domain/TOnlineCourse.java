package com.ruoyi.spirit.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 线上课程管理对象 t_online_course
 * 
 * @author zhangTing
 * @date 2026-03-11
 */
public class TOnlineCourse extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 课程id */
    @Excel(name = "课程id")
    private Long courseId;

    /** 课程名称 */
    @Excel(name = "课程名称")
    private String courseName;

    /** 课程类型 */
    @Excel(name = "课程类型")
    private String courseType;

    /** 课程描述 */
    private String courseDescription;

    /** 讲师姓名 */
    @Excel(name = "讲师姓名")
    private String instructor;

    /** 课程封面图片URL */
    private String courseCover;

    /** 课程视频URL */
    private String videoUrl;

    /** 课程时长（分钟） */
    @Excel(name = "课程时长", readConverterExp = "分=钟")
    private Long duration;

    /** 最大报名人数 */
    @Excel(name = "最大报名人数")
    private Long maxEnrollment;

    /** 当前报名人数 */
    @Excel(name = "当前报名人数")
    private Long currentEnrollment;

    /** 开课时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开课时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 结课时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结课时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 课程状态 */
    @Excel(name = "课程状态")
    private Long status;

    /** 逻辑删除 */
    private Long isDeleted;

    public void setCourseId(Long courseId) 
    {
        this.courseId = courseId;
    }

    public Long getCourseId() 
    {
        return courseId;
    }

    public void setCourseName(String courseName) 
    {
        this.courseName = courseName;
    }

    public String getCourseName() 
    {
        return courseName;
    }

    public void setCourseType(String courseType) 
    {
        this.courseType = courseType;
    }

    public String getCourseType() 
    {
        return courseType;
    }

    public void setCourseDescription(String courseDescription) 
    {
        this.courseDescription = courseDescription;
    }

    public String getCourseDescription() 
    {
        return courseDescription;
    }

    public void setInstructor(String instructor) 
    {
        this.instructor = instructor;
    }

    public String getInstructor() 
    {
        return instructor;
    }

    public void setCourseCover(String courseCover) 
    {
        this.courseCover = courseCover;
    }

    public String getCourseCover() 
    {
        return courseCover;
    }

    public void setVideoUrl(String videoUrl) 
    {
        this.videoUrl = videoUrl;
    }

    public String getVideoUrl() 
    {
        return videoUrl;
    }

    public void setDuration(Long duration) 
    {
        this.duration = duration;
    }

    public Long getDuration() 
    {
        return duration;
    }

    public void setMaxEnrollment(Long maxEnrollment) 
    {
        this.maxEnrollment = maxEnrollment;
    }

    public Long getMaxEnrollment() 
    {
        return maxEnrollment;
    }

    public void setCurrentEnrollment(Long currentEnrollment) 
    {
        this.currentEnrollment = currentEnrollment;
    }

    public Long getCurrentEnrollment() 
    {
        return currentEnrollment;
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
            .append("courseId", getCourseId())
            .append("courseName", getCourseName())
            .append("courseType", getCourseType())
            .append("courseDescription", getCourseDescription())
            .append("instructor", getInstructor())
            .append("courseCover", getCourseCover())
            .append("videoUrl", getVideoUrl())
            .append("duration", getDuration())
            .append("maxEnrollment", getMaxEnrollment())
            .append("currentEnrollment", getCurrentEnrollment())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("status", getStatus())
            .append("isDeleted", getIsDeleted())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
