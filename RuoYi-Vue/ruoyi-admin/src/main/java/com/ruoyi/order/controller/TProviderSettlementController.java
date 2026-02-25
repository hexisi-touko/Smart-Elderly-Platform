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
import com.ruoyi.order.domain.TProviderSettlement;
import com.ruoyi.order.service.ITProviderSettlementService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 服务商结算管理Controller
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@RestController
@RequestMapping("/order/settlement")
public class TProviderSettlementController extends BaseController
{
    @Autowired
    private ITProviderSettlementService tProviderSettlementService;

    /**
     * 查询服务商结算管理列表
     */
    @PreAuthorize("@ss.hasPermi('order:settlement:list')")
    @GetMapping("/list")
    public TableDataInfo list(TProviderSettlement tProviderSettlement)
    {
        startPage();
        List<TProviderSettlement> list = tProviderSettlementService.selectTProviderSettlementList(tProviderSettlement);
        return getDataTable(list);
    }

    /**
     * 导出服务商结算管理列表
     */
    @PreAuthorize("@ss.hasPermi('order:settlement:export')")
    @Log(title = "服务商结算管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TProviderSettlement tProviderSettlement)
    {
        List<TProviderSettlement> list = tProviderSettlementService.selectTProviderSettlementList(tProviderSettlement);
        ExcelUtil<TProviderSettlement> util = new ExcelUtil<TProviderSettlement>(TProviderSettlement.class);
        util.exportExcel(response, list, "服务商结算管理数据");
    }

    /**
     * 获取服务商结算管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('order:settlement:query')")
    @GetMapping(value = "/{settlementId}")
    public AjaxResult getInfo(@PathVariable("settlementId") Long settlementId)
    {
        return success(tProviderSettlementService.selectTProviderSettlementBySettlementId(settlementId));
    }

    /**
     * 新增服务商结算管理
     */
    @PreAuthorize("@ss.hasPermi('order:settlement:add')")
    @Log(title = "服务商结算管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TProviderSettlement tProviderSettlement)
    {
        return toAjax(tProviderSettlementService.insertTProviderSettlement(tProviderSettlement));
    }

    /**
     * 修改服务商结算管理
     */
    @PreAuthorize("@ss.hasPermi('order:settlement:edit')")
    @Log(title = "服务商结算管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TProviderSettlement tProviderSettlement)
    {
        return toAjax(tProviderSettlementService.updateTProviderSettlement(tProviderSettlement));
    }

    /**
     * 删除服务商结算管理
     */
    @PreAuthorize("@ss.hasPermi('order:settlement:remove')")
    @Log(title = "服务商结算管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{settlementIds}")
    public AjaxResult remove(@PathVariable Long[] settlementIds)
    {
        return toAjax(tProviderSettlementService.deleteTProviderSettlementBySettlementIds(settlementIds));
    }
}
