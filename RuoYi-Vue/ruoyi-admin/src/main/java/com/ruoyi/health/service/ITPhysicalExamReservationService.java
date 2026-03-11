package com.ruoyi.health.service;

import java.util.List;
import com.ruoyi.health.domain.TPhysicalExamReservation;

/**
 * 体检预约Service接口
 * 
 * @author zhangTing
 * @date 2026-03-11
 */
public interface ITPhysicalExamReservationService 
{
    /**
     * 查询体检预约
     * 
     * @param reservationId 体检预约主键
     * @return 体检预约
     */
    public TPhysicalExamReservation selectTPhysicalExamReservationByReservationId(Long reservationId);

    /**
     * 查询体检预约列表
     * 
     * @param tPhysicalExamReservation 体检预约
     * @return 体检预约集合
     */
    public List<TPhysicalExamReservation> selectTPhysicalExamReservationList(TPhysicalExamReservation tPhysicalExamReservation);

    /**
     * 新增体检预约
     * 
     * @param tPhysicalExamReservation 体检预约
     * @return 结果
     */
    public int insertTPhysicalExamReservation(TPhysicalExamReservation tPhysicalExamReservation);

    /**
     * 修改体检预约
     * 
     * @param tPhysicalExamReservation 体检预约
     * @return 结果
     */
    public int updateTPhysicalExamReservation(TPhysicalExamReservation tPhysicalExamReservation);

    /**
     * 批量删除体检预约
     * 
     * @param reservationIds 需要删除的体检预约主键集合
     * @return 结果
     */
    public int deleteTPhysicalExamReservationByReservationIds(Long[] reservationIds);

    /**
     * 删除体检预约信息
     * 
     * @param reservationId 体检预约主键
     * @return 结果
     */
    public int deleteTPhysicalExamReservationByReservationId(Long reservationId);
}
