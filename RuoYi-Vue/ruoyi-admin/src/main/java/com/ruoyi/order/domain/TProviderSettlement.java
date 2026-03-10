package com.ruoyi.order.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 服务商结算管理对象 t_provider_settlement
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public class TProviderSettlement extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 结算id */
    @Excel(name = "结算id")
    private Long settlementId;

    /** 关联服务商ID */
    @Excel(name = "关联服务商ID")
    private Long providerId;

    /** 结算周期 */
    @Excel(name = "结算周期")
    private String settlementPeriod;

    /** 订单数量 */
    @Excel(name = "订单数量")
    private Long orderCount;

    /** 结算总金额 */
    @Excel(name = "结算总金额")
    private BigDecimal totalAmount;

    /** 平台服务费 */
    @Excel(name = "平台服务费")
    private BigDecimal platformFee;

    /** 实际结算金额 */
    @Excel(name = "实际结算金额")
    private BigDecimal actualAmount;

    /** 结算状态 */
    @Excel(name = "结算状态")
    private Long settlementStatus;

    /** 提现申请时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "提现申请时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date applyTime;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date approveTime;

    /** 转账时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "转账时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date transferTime;

    /** 转账凭证URL */
    @Excel(name = "转账凭证URL")
    private String transferVoucher;

    /** 逻辑删除 */
    private Long isDeleted;

    /** 服务商名称（JOIN查询用） */
    @Excel(name = "服务商名称")
    private String providerName;

    public void setSettlementId(Long settlementId) {
        this.settlementId = settlementId;
    }

    public Long getSettlementId() {
        return settlementId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setSettlementPeriod(String settlementPeriod) {
        this.settlementPeriod = settlementPeriod;
    }

    public String getSettlementPeriod() {
        return settlementPeriod;
    }

    public void setOrderCount(Long orderCount) {
        this.orderCount = orderCount;
    }

    public Long getOrderCount() {
        return orderCount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setPlatformFee(BigDecimal platformFee) {
        this.platformFee = platformFee;
    }

    public BigDecimal getPlatformFee() {
        return platformFee;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setSettlementStatus(Long settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public Long getSettlementStatus() {
        return settlementStatus;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApproveTime(Date approveTime) {
        this.approveTime = approveTime;
    }

    public Date getApproveTime() {
        return approveTime;
    }

    public void setTransferTime(Date transferTime) {
        this.transferTime = transferTime;
    }

    public Date getTransferTime() {
        return transferTime;
    }

    public void setTransferVoucher(String transferVoucher) {
        this.transferVoucher = transferVoucher;
    }

    public String getTransferVoucher() {
        return transferVoucher;
    }

    public void setIsDeleted(Long isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getIsDeleted() {
        return isDeleted;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderName() {
        return providerName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("settlementId", getSettlementId())
                .append("providerId", getProviderId())
                .append("providerName", getProviderName())
                .append("settlementPeriod", getSettlementPeriod())
                .append("orderCount", getOrderCount())
                .append("totalAmount", getTotalAmount())
                .append("platformFee", getPlatformFee())
                .append("actualAmount", getActualAmount())
                .append("settlementStatus", getSettlementStatus())
                .append("applyTime", getApplyTime())
                .append("approveTime", getApproveTime())
                .append("transferTime", getTransferTime())
                .append("transferVoucher", getTransferVoucher())
                .append("isDeleted", getIsDeleted())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
