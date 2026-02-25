package com.ruoyi.order.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.order.domain.TServiceEvaluation;
import com.ruoyi.order.service.ITServiceEvaluationService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 服务评价管理Controller
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@RestController
@RequestMapping("/order/evaluation")
public class TServiceEvaluationController extends BaseController
{
    @Autowired
    private ITServiceEvaluationService tServiceEvaluationService;

    /**
     * 查询服务评价管理列表
     */
    @PreAuthorize("@ss.hasPermi('order:evaluation:list')")
    @GetMapping("/list")
    public TableDataInfo list(TServiceEvaluation tServiceEvaluation)
    {
        startPage();
        List<TServiceEvaluation> list = tServiceEvaluationService.selectTServiceEvaluationList(tServiceEvaluation);
        return getDataTable(list);
    }

    /**
     * 导出服务评价管理列表
     */
    @PreAuthorize("@ss.hasPermi('order:evaluation:export')")
    @Log(title = "服务评价管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TServiceEvaluation tServiceEvaluation)
    {
        List<TServiceEvaluation> list = tServiceEvaluationService.selectTServiceEvaluationList(tServiceEvaluation);
        ExcelUtil<TServiceEvaluation> util = new ExcelUtil<TServiceEvaluation>(TServiceEvaluation.class);
        util.exportExcel(response, list, "服务评价管理数据");
    }

    /**
     * 获取服务评价管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('order:evaluation:query')")
    @GetMapping(value = "/{evaluationId}")
    public AjaxResult getInfo(@PathVariable("evaluationId") Long evaluationId)
    {
        return success(tServiceEvaluationService.selectTServiceEvaluationByEvaluationId(evaluationId));
    }

    /**
     * 新增服务评价管理
     */
    @PreAuthorize("@ss.hasPermi('order:evaluation:add')")
    @Log(title = "服务评价管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TServiceEvaluation tServiceEvaluation)
    {
        return toAjax(tServiceEvaluationService.insertTServiceEvaluation(tServiceEvaluation));
    }

    /**
     * 修改服务评价管理
     */
    @PreAuthorize("@ss.hasPermi('order:evaluation:edit')")
    @Log(title = "服务评价管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TServiceEvaluation tServiceEvaluation)
    {
        return toAjax(tServiceEvaluationService.updateTServiceEvaluation(tServiceEvaluation));
    }

    /**
     * 删除服务评价管理
     */
    @PreAuthorize("@ss.hasPermi('order:evaluation:remove')")
    @Log(title = "服务评价管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{evaluationIds}")
    public AjaxResult remove(@PathVariable Long[] evaluationIds)
    {
        return toAjax(tServiceEvaluationService.deleteTServiceEvaluationByEvaluationIds(evaluationIds));
    }
}
