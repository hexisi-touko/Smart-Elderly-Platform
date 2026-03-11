package com.ruoyi.health.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.health.mapper.TPhysicalExamReservationMapper;
import com.ruoyi.health.domain.TPhysicalExamReservation;
import com.ruoyi.health.service.ITPhysicalExamReservationService;

/**
 * 体检预约Service业务层处理
 * 
 * @author zhangTing
 * @date 2026-03-11
 */
@Service
public class TPhysicalExamReservationServiceImpl implements ITPhysicalExamReservationService 
{
    @Autowired
    private TPhysicalExamReservationMapper tPhysicalExamReservationMapper;

    /**
     * 查询体检预约
     * 
     * @param reservationId 体检预约主键
     * @return 体检预约
     */
    @Override
    public TPhysicalExamReservation selectTPhysicalExamReservationByReservationId(Long reservationId)
    {
        return tPhysicalExamReservationMapper.selectTPhysicalExamReservationByReservationId(reservationId);
    }

    /**
     * 查询体检预约列表
     * 
     * @param tPhysicalExamReservation 体检预约
     * @return 体检预约
     */
    @Override
    public List<TPhysicalExamReservation> selectTPhysicalExamReservationList(TPhysicalExamReservation tPhysicalExamReservation)
    {
        return tPhysicalExamReservationMapper.selectTPhysicalExamReservationList(tPhysicalExamReservation);
    }

    /**
     * 新增体检预约
     * 
     * @param tPhysicalExamReservation 体检预约
     * @return 结果
     */
    @Override
    public int insertTPhysicalExamReservation(TPhysicalExamReservation tPhysicalExamReservation)
    {
        tPhysicalExamReservation.setCreateTime(DateUtils.getNowDate());
        return tPhysicalExamReservationMapper.insertTPhysicalExamReservation(tPhysicalExamReservation);
    }

    /**
     * 修改体检预约
     * 
     * @param tPhysicalExamReservation 体检预约
     * @return 结果
     */
    @Override
    public int updateTPhysicalExamReservation(TPhysicalExamReservation tPhysicalExamReservation)
    {
        tPhysicalExamReservation.setUpdateTime(DateUtils.getNowDate());
        return tPhysicalExamReservationMapper.updateTPhysicalExamReservation(tPhysicalExamReservation);
    }

    /**
     * 批量删除体检预约
     * 
     * @param reservationIds 需要删除的体检预约主键
     * @return 结果
     */
    @Override
    public int deleteTPhysicalExamReservationByReservationIds(Long[] reservationIds)
    {
        return tPhysicalExamReservationMapper.deleteTPhysicalExamReservationByReservationIds(reservationIds);
    }

    /**
     * 删除体检预约信息
     * 
     * @param reservationId 体检预约主键
     * @return 结果
     */
    @Override
    public int deleteTPhysicalExamReservationByReservationId(Long reservationId)
    {
        return tPhysicalExamReservationMapper.deleteTPhysicalExamReservationByReservationId(reservationId);
    }
}
