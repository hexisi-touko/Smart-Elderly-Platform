package com.ruoyi.order.mapper;

import java.util.List;
import com.ruoyi.order.domain.TServiceEvaluation;

/**
 * 服务评价管理Mapper接口
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public interface TServiceEvaluationMapper 
{
    /**
     * 查询服务评价管理
     * 
     * @param evaluationId 服务评价管理主键
     * @return 服务评价管理
     */
    public TServiceEvaluation selectTServiceEvaluationByEvaluationId(Long evaluationId);

    /**
     * 查询服务评价管理列表
     * 
     * @param tServiceEvaluation 服务评价管理
     * @return 服务评价管理集合
     */
    public List<TServiceEvaluation> selectTServiceEvaluationList(TServiceEvaluation tServiceEvaluation);

    /**
     * 新增服务评价管理
     * 
     * @param tServiceEvaluation 服务评价管理
     * @return 结果
     */
    public int insertTServiceEvaluation(TServiceEvaluation tServiceEvaluation);

    /**
     * 修改服务评价管理
     * 
     * @param tServiceEvaluation 服务评价管理
     * @return 结果
     */
    public int updateTServiceEvaluation(TServiceEvaluation tServiceEvaluation);

    /**
     * 删除服务评价管理
     * 
     * @param evaluationId 服务评价管理主键
     * @return 结果
     */
    public int deleteTServiceEvaluationByEvaluationId(Long evaluationId);

    /**
     * 批量删除服务评价管理
     * 
     * @param evaluationIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTServiceEvaluationByEvaluationIds(Long[] evaluationIds);
}
