package com.ruoyi.order.service;

import java.util.List;
import com.ruoyi.order.domain.TPaymentRecord;

/**
 * 支付记录管理Service接口
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public interface ITPaymentRecordService 
{
    /**
     * 查询支付记录管理
     * 
     * @param paymentId 支付记录管理主键
     * @return 支付记录管理
     */
    public TPaymentRecord selectTPaymentRecordByPaymentId(Long paymentId);

    /**
     * 查询支付记录管理列表
     * 
     * @param tPaymentRecord 支付记录管理
     * @return 支付记录管理集合
     */
    public List<TPaymentRecord> selectTPaymentRecordList(TPaymentRecord tPaymentRecord);

    /**
     * 新增支付记录管理
     * 
     * @param tPaymentRecord 支付记录管理
     * @return 结果
     */
    public int insertTPaymentRecord(TPaymentRecord tPaymentRecord);

    /**
     * 修改支付记录管理
     * 
     * @param tPaymentRecord 支付记录管理
     * @return 结果
     */
    public int updateTPaymentRecord(TPaymentRecord tPaymentRecord);

    /**
     * 批量删除支付记录管理
     * 
     * @param paymentIds 需要删除的支付记录管理主键集合
     * @return 结果
     */
    public int deleteTPaymentRecordByPaymentIds(Long[] paymentIds);

    /**
     * 删除支付记录管理信息
     * 
     * @param paymentId 支付记录管理主键
     * @return 结果
     */
    public int deleteTPaymentRecordByPaymentId(Long paymentId);
}
