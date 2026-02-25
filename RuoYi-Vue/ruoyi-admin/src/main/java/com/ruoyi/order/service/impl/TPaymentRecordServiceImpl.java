package com.ruoyi.order.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.order.mapper.TPaymentRecordMapper;
import com.ruoyi.order.domain.TPaymentRecord;
import com.ruoyi.order.service.ITPaymentRecordService;

/**
 * 支付记录管理Service业务层处理
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@Service
public class TPaymentRecordServiceImpl implements ITPaymentRecordService 
{
    @Autowired
    private TPaymentRecordMapper tPaymentRecordMapper;

    /**
     * 查询支付记录管理
     * 
     * @param paymentId 支付记录管理主键
     * @return 支付记录管理
     */
    @Override
    public TPaymentRecord selectTPaymentRecordByPaymentId(Long paymentId)
    {
        return tPaymentRecordMapper.selectTPaymentRecordByPaymentId(paymentId);
    }

    /**
     * 查询支付记录管理列表
     * 
     * @param tPaymentRecord 支付记录管理
     * @return 支付记录管理
     */
    @Override
    public List<TPaymentRecord> selectTPaymentRecordList(TPaymentRecord tPaymentRecord)
    {
        return tPaymentRecordMapper.selectTPaymentRecordList(tPaymentRecord);
    }

    /**
     * 新增支付记录管理
     * 
     * @param tPaymentRecord 支付记录管理
     * @return 结果
     */
    @Override
    public int insertTPaymentRecord(TPaymentRecord tPaymentRecord)
    {
        tPaymentRecord.setCreateTime(DateUtils.getNowDate());
        return tPaymentRecordMapper.insertTPaymentRecord(tPaymentRecord);
    }

    /**
     * 修改支付记录管理
     * 
     * @param tPaymentRecord 支付记录管理
     * @return 结果
     */
    @Override
    public int updateTPaymentRecord(TPaymentRecord tPaymentRecord)
    {
        tPaymentRecord.setUpdateTime(DateUtils.getNowDate());
        return tPaymentRecordMapper.updateTPaymentRecord(tPaymentRecord);
    }

    /**
     * 批量删除支付记录管理
     * 
     * @param paymentIds 需要删除的支付记录管理主键
     * @return 结果
     */
    @Override
    public int deleteTPaymentRecordByPaymentIds(Long[] paymentIds)
    {
        return tPaymentRecordMapper.deleteTPaymentRecordByPaymentIds(paymentIds);
    }

    /**
     * 删除支付记录管理信息
     * 
     * @param paymentId 支付记录管理主键
     * @return 结果
     */
    @Override
    public int deleteTPaymentRecordByPaymentId(Long paymentId)
    {
        return tPaymentRecordMapper.deleteTPaymentRecordByPaymentId(paymentId);
    }
}
