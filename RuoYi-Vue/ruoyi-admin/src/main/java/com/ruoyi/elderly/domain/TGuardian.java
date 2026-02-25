package com.ruoyi.elderly.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 监护人信息对象 t_guardian
 * 
 * @author zhangTing
 * @date 2026-02-24
 */
public class TGuardian extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 监护人id */
    @Excel(name = "监护人id")
    private Long guardianId;

    /** 关联C端用户ID（t_app_user.user_id） */
    @Excel(name = "关联C端用户ID", readConverterExp = "t=_app_user.user_id")
    private Long userId;

    /** 监护人姓名 */
    @Excel(name = "监护人姓名")
    private String name;

    /** 身份证号 */
    private String idCard;

    /** 监护人手机号 */
    @Excel(name = "监护人手机号")
    private String phone;

    /** 与老人关系 */
    @Excel(name = "与老人关系")
    private String relationship;

    /** 是否主要监护人 */
    @Excel(name = "是否主要监护人")
    private Long isPrimary;

    /** 逻辑删除 */
    private Long isDeleted;

    public void setGuardianId(Long guardianId) 
    {
        this.guardianId = guardianId;
    }

    public Long getGuardianId() 
    {
        return guardianId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setIdCard(String idCard) 
    {
        this.idCard = idCard;
    }

    public String getIdCard() 
    {
        return idCard;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }

    public void setRelationship(String relationship) 
    {
        this.relationship = relationship;
    }

    public String getRelationship() 
    {
        return relationship;
    }

    public void setIsPrimary(Long isPrimary) 
    {
        this.isPrimary = isPrimary;
    }

    public Long getIsPrimary() 
    {
        return isPrimary;
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
            .append("guardianId", getGuardianId())
            .append("userId", getUserId())
            .append("name", getName())
            .append("idCard", getIdCard())
            .append("phone", getPhone())
            .append("relationship", getRelationship())
            .append("isPrimary", getIsPrimary())
            .append("isDeleted", getIsDeleted())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
