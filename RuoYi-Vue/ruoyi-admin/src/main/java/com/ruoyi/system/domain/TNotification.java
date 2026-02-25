package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 消息通知管理对象 t_notification
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public class TNotification extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 通知id */
    @Excel(name = "通知id")
    private Long notificationId;

    /** 接收用户ID */
    @Excel(name = "接收用户ID")
    private Long userId;

    /** 用户来源 */
    @Excel(name = "用户来源")
    private String userType;

    /** 通知类型 */
    @Excel(name = "通知类型")
    private String notificationType;

    /** 通知标题 */
    @Excel(name = "通知标题")
    private String title;

    /** 通知内容 */
    private String content;

    /** 优先级 */
    private Long priority;

    /** 是否已读 */
    @Excel(name = "是否已读")
    private Long isRead;

    /** 阅读时间 */
    private Date readTime;

    /** 关联业务ID */
    private Long bizId;

    /** 业务类型 */
    @Excel(name = "业务类型")
    private String bizType;

    /** 逻辑删除（0-未删除、1-已删除） */
    private Long isDeleted;

    public void setNotificationId(Long notificationId) 
    {
        this.notificationId = notificationId;
    }

    public Long getNotificationId() 
    {
        return notificationId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setUserType(String userType) 
    {
        this.userType = userType;
    }

    public String getUserType() 
    {
        return userType;
    }

    public void setNotificationType(String notificationType) 
    {
        this.notificationType = notificationType;
    }

    public String getNotificationType() 
    {
        return notificationType;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setPriority(Long priority) 
    {
        this.priority = priority;
    }

    public Long getPriority() 
    {
        return priority;
    }

    public void setIsRead(Long isRead) 
    {
        this.isRead = isRead;
    }

    public Long getIsRead() 
    {
        return isRead;
    }

    public void setReadTime(Date readTime) 
    {
        this.readTime = readTime;
    }

    public Date getReadTime() 
    {
        return readTime;
    }

    public void setBizId(Long bizId) 
    {
        this.bizId = bizId;
    }

    public Long getBizId() 
    {
        return bizId;
    }

    public void setBizType(String bizType) 
    {
        this.bizType = bizType;
    }

    public String getBizType() 
    {
        return bizType;
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
            .append("notificationId", getNotificationId())
            .append("userId", getUserId())
            .append("userType", getUserType())
            .append("notificationType", getNotificationType())
            .append("title", getTitle())
            .append("content", getContent())
            .append("priority", getPriority())
            .append("isRead", getIsRead())
            .append("readTime", getReadTime())
            .append("bizId", getBizId())
            .append("bizType", getBizType())
            .append("isDeleted", getIsDeleted())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
