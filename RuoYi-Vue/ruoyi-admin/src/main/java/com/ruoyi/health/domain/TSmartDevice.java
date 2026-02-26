package com.ruoyi.health.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 智能设备管理对象 t_smart_device
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public class TSmartDevice extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 设备id */
    @Excel(name = "设备id")
    private Long deviceId;

    /** 设备硬件编码 */
    @Excel(name = "设备硬件编码")
    private String deviceCode;

    /** 设备类型 */
    @Excel(name = "设备类型")
    private String deviceType;

    /** 关联老人ID */
    @Excel(name = "关联老人ID")
    private Long elderlyId;

    /** 设备品牌 */
    @Excel(name = "设备品牌")
    private String deviceBrand;

    /** 设备型号 */
    @Excel(name = "设备型号")
    private String deviceModel;

    /** 绑定时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "绑定时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date bindTime;

    /** 上次维护时间 */
    private Date maintenanceTime;

    /** 下次维护时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "下次维护时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date nextMaintenanceTime;

    /** 推送状态 */
    @Excel(name = "推送状态")
    private Long pushStatus;

    /** 设备状态 */
    @Excel(name = "设备状态")
    private Long deviceStatus;

    /** 第三方设备平台ID */
    private String thirdPartyDeviceId;

    /** 逻辑删除 */
    private Long isDeleted;

    /** 关联老人姓名（非数据库字段，JOIN查询用） */
    @Excel(name = "老人姓名")
    private String elderlyName;

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setElderlyId(Long elderlyId) {
        this.elderlyId = elderlyId;
    }

    public Long getElderlyId() {
        return elderlyId;
    }

    public void setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand;
    }

    public String getDeviceBrand() {
        return deviceBrand;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setBindTime(Date bindTime) {
        this.bindTime = bindTime;
    }

    public Date getBindTime() {
        return bindTime;
    }

    public void setMaintenanceTime(Date maintenanceTime) {
        this.maintenanceTime = maintenanceTime;
    }

    public Date getMaintenanceTime() {
        return maintenanceTime;
    }

    public void setNextMaintenanceTime(Date nextMaintenanceTime) {
        this.nextMaintenanceTime = nextMaintenanceTime;
    }

    public Date getNextMaintenanceTime() {
        return nextMaintenanceTime;
    }

    public void setPushStatus(Long pushStatus) {
        this.pushStatus = pushStatus;
    }

    public Long getPushStatus() {
        return pushStatus;
    }

    public void setDeviceStatus(Long deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public Long getDeviceStatus() {
        return deviceStatus;
    }

    public void setThirdPartyDeviceId(String thirdPartyDeviceId) {
        this.thirdPartyDeviceId = thirdPartyDeviceId;
    }

    public String getThirdPartyDeviceId() {
        return thirdPartyDeviceId;
    }

    public void setIsDeleted(Long isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getIsDeleted() {
        return isDeleted;
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
                .append("deviceId", getDeviceId())
                .append("deviceCode", getDeviceCode())
                .append("deviceType", getDeviceType())
                .append("elderlyId", getElderlyId())
                .append("elderlyName", getElderlyName())
                .append("deviceBrand", getDeviceBrand())
                .append("deviceModel", getDeviceModel())
                .append("bindTime", getBindTime())
                .append("maintenanceTime", getMaintenanceTime())
                .append("nextMaintenanceTime", getNextMaintenanceTime())
                .append("pushStatus", getPushStatus())
                .append("deviceStatus", getDeviceStatus())
                .append("thirdPartyDeviceId", getThirdPartyDeviceId())
                .append("isDeleted", getIsDeleted())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
