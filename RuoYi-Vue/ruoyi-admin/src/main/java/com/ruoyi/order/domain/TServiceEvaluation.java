package com.ruoyi.order.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 服务评价管理对象 t_service_evaluation
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public class TServiceEvaluation extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 评价id */
    @Excel(name = "评价id")
    private Long evaluationId;

    /** 关联服务订单ID */
    @Excel(name = "关联服务订单ID")
    private Long orderId;

    /** 评价人ID */
    @Excel(name = "评价人ID")
    private Long elderlyId;

    /** 星级评分 */
    @Excel(name = "星级评分")
    private Long starLevel;

    /** 评价内容 */
    @Excel(name = "评价内容")
    private String evaluationContent;

    /** 照片凭证 */
    @Excel(name = "照片凭证")
    private String proofPhotos;

    /** 评价提交时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "评价提交时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date evaluationTime;

    /** 服务商回复 */
    @Excel(name = "服务商回复")
    private String providerReply;

    /** 回复时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "回复时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date replyTime;

    /** 逻辑删除 */
    private Long isDeleted;

    /** 订单编号（JOIN查询用） */
    @Excel(name = "订单编号")
    private String orderNo;

    /** 老人姓名（JOIN查询用） */
    @Excel(name = "老人姓名")
    private String elderlyName;

    public void setEvaluationId(Long evaluationId) {
        this.evaluationId = evaluationId;
    }

    public Long getEvaluationId() {
        return evaluationId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setElderlyId(Long elderlyId) {
        this.elderlyId = elderlyId;
    }

    public Long getElderlyId() {
        return elderlyId;
    }

    public void setStarLevel(Long starLevel) {
        this.starLevel = starLevel;
    }

    public Long getStarLevel() {
        return starLevel;
    }

    public void setEvaluationContent(String evaluationContent) {
        this.evaluationContent = evaluationContent;
    }

    public String getEvaluationContent() {
        return evaluationContent;
    }

    public void setProofPhotos(String proofPhotos) {
        this.proofPhotos = proofPhotos;
    }

    public String getProofPhotos() {
        return proofPhotos;
    }

    public void setEvaluationTime(Date evaluationTime) {
        this.evaluationTime = evaluationTime;
    }

    public Date getEvaluationTime() {
        return evaluationTime;
    }

    public void setProviderReply(String providerReply) {
        this.providerReply = providerReply;
    }

    public String getProviderReply() {
        return providerReply;
    }

    public void setReplyTime(Date replyTime) {
        this.replyTime = replyTime;
    }

    public Date getReplyTime() {
        return replyTime;
    }

    public void setIsDeleted(Long isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getIsDeleted() {
        return isDeleted;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setElderlyName(String elderlyName) {
        this.elderlyName = elderlyName;
    }

    public String getElderlyName() {
        return elderlyName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("evaluationId", getEvaluationId())
                .append("orderId", getOrderId())
                .append("orderNo", getOrderNo())
                .append("elderlyId", getElderlyId())
                .append("elderlyName", getElderlyName())
                .append("starLevel", getStarLevel())
                .append("evaluationContent", getEvaluationContent())
                .append("proofPhotos", getProofPhotos())
                .append("evaluationTime", getEvaluationTime())
                .append("providerReply", getProviderReply())
                .append("replyTime", getReplyTime())
                .append("isDeleted", getIsDeleted())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
