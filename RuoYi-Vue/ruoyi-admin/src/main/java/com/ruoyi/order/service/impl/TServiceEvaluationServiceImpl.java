package com.ruoyi.order.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.order.mapper.TServiceEvaluationMapper;
import com.ruoyi.order.domain.TServiceEvaluation;
import com.ruoyi.order.domain.TServiceOrder;
import com.ruoyi.order.service.ITServiceEvaluationService;
import com.ruoyi.order.service.ITServiceOrderService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务评价管理Service业务层处理
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@Service
public class TServiceEvaluationServiceImpl implements ITServiceEvaluationService {
    @Autowired
    private TServiceEvaluationMapper tServiceEvaluationMapper;

    @Autowired
    private ITServiceOrderService tServiceOrderService;

    /**
     * 查询服务评价管理
     * 
     * @param evaluationId 服务评价管理主键
     * @return 服务评价管理
     */
    @Override
    public TServiceEvaluation selectTServiceEvaluationByEvaluationId(Long evaluationId) {
        return tServiceEvaluationMapper.selectTServiceEvaluationByEvaluationId(evaluationId);
    }

    /**
     * 查询服务评价管理列表
     * 
     * @param tServiceEvaluation 服务评价管理
     * @return 服务评价管理
     */
    @Override
    public List<TServiceEvaluation> selectTServiceEvaluationList(TServiceEvaluation tServiceEvaluation) {
        return tServiceEvaluationMapper.selectTServiceEvaluationList(tServiceEvaluation);
    }

    /**
     * 新增服务评价管理
     * 
     * @param tServiceEvaluation 服务评价管理
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertTServiceEvaluation(TServiceEvaluation tServiceEvaluation) {
        tServiceEvaluation.setCreateTime(DateUtils.getNowDate());
        tServiceEvaluation.setEvaluationTime(DateUtils.getNowDate());
        int rows = tServiceEvaluationMapper.insertTServiceEvaluation(tServiceEvaluation);

        // 联动更新订单状态为 5:已评价
        if (rows > 0 && tServiceEvaluation.getOrderId() != null) {
            TServiceOrder order = new TServiceOrder();
            order.setOrderId(tServiceEvaluation.getOrderId());
            order.setOrderStatus(5L);
            tServiceOrderService.updateTServiceOrder(order);
        }

        return rows;
    }

    /**
     * 修改服务评价管理
     * 
     * @param tServiceEvaluation 服务评价管理
     * @return 结果
     */
    @Override
    public int updateTServiceEvaluation(TServiceEvaluation tServiceEvaluation) {
        tServiceEvaluation.setUpdateTime(DateUtils.getNowDate());
        return tServiceEvaluationMapper.updateTServiceEvaluation(tServiceEvaluation);
    }

    /**
     * 批量删除服务评价管理
     * 
     * @param evaluationIds 需要删除的服务评价管理主键
     * @return 结果
     */
    @Override
    public int deleteTServiceEvaluationByEvaluationIds(Long[] evaluationIds) {
        return tServiceEvaluationMapper.deleteTServiceEvaluationByEvaluationIds(evaluationIds);
    }

    /**
     * 删除服务评价管理信息
     * 
     * @param evaluationId 服务评价管理主键
     * @return 结果
     */
    @Override
    public int deleteTServiceEvaluationByEvaluationId(Long evaluationId) {
        return tServiceEvaluationMapper.deleteTServiceEvaluationByEvaluationId(evaluationId);
    }
}
