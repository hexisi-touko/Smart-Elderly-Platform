package com.ruoyi.order.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 支付记录管理对象 t_payment_record
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public class TPaymentRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 支付记录id */
    @Excel(name = "支付记录id")
    private Long paymentId;

    /** 关联服务订单ID */
    @Excel(name = "关联服务订单ID")
    private Long orderId;

    /** 支付金额 */
    @Excel(name = "支付金额")
    private BigDecimal paymentAmount;

    /** 支付时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "支付时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date paymentTime;

    /** 支付方式 */
    @Excel(name = "支付方式")
    private String paymentMethod;

    /** 支付状态 */
    @Excel(name = "支付状态")
    private Long paymentStatus;

    /** 第三方支付流水号 */
    @Excel(name = "第三方支付流水号")
    private String transactionId;

    /** 退款标记 */
    @Excel(name = "退款标记")
    private Long refundFlag;

    /** 退款金额 */
    private BigDecimal refundAmount;

    /** 退款时间 */
    private Date refundTime;

    /** 退款原因 */
    private String refundReason;

    /** 逻辑删除 */
    private Long isDeleted;

    /** 订单编号（JOIN查询用） */
    @Excel(name = "订单编号")
    private String orderNo;

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentStatus(Long paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Long getPaymentStatus() {
        return paymentStatus;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setRefundFlag(Long refundFlag) {
        this.refundFlag = refundFlag;
    }

    public Long getRefundFlag() {
        return refundFlag;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public String getRefundReason() {
        return refundReason;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("paymentId", getPaymentId())
                .append("orderId", getOrderId())
                .append("orderNo", getOrderNo())
                .append("paymentAmount", getPaymentAmount())
                .append("paymentTime", getPaymentTime())
                .append("paymentMethod", getPaymentMethod())
                .append("paymentStatus", getPaymentStatus())
                .append("transactionId", getTransactionId())
                .append("refundFlag", getRefundFlag())
                .append("refundAmount", getRefundAmount())
                .append("refundTime", getRefundTime())
                .append("refundReason", getRefundReason())
                .append("isDeleted", getIsDeleted())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
