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
import com.ruoyi.order.domain.TPaymentRecord;
import com.ruoyi.order.service.ITPaymentRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 支付记录管理Controller
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@RestController
@RequestMapping("/order/paymentRecord")
public class TPaymentRecordController extends BaseController
{
    @Autowired
    private ITPaymentRecordService tPaymentRecordService;

    /**
     * 查询支付记录管理列表
     */
    @PreAuthorize("@ss.hasPermi('order:paymentRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(TPaymentRecord tPaymentRecord)
    {
        startPage();
        List<TPaymentRecord> list = tPaymentRecordService.selectTPaymentRecordList(tPaymentRecord);
        return getDataTable(list);
    }

    /**
     * 导出支付记录管理列表
     */
    @PreAuthorize("@ss.hasPermi('order:paymentRecord:export')")
    @Log(title = "支付记录管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TPaymentRecord tPaymentRecord)
    {
        List<TPaymentRecord> list = tPaymentRecordService.selectTPaymentRecordList(tPaymentRecord);
        ExcelUtil<TPaymentRecord> util = new ExcelUtil<TPaymentRecord>(TPaymentRecord.class);
        util.exportExcel(response, list, "支付记录管理数据");
    }

    /**
     * 获取支付记录管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('order:paymentRecord:query')")
    @GetMapping(value = "/{paymentId}")
    public AjaxResult getInfo(@PathVariable("paymentId") Long paymentId)
    {
        return success(tPaymentRecordService.selectTPaymentRecordByPaymentId(paymentId));
    }

    /**
     * 新增支付记录管理
     */
    @PreAuthorize("@ss.hasPermi('order:paymentRecord:add')")
    @Log(title = "支付记录管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TPaymentRecord tPaymentRecord)
    {
        return toAjax(tPaymentRecordService.insertTPaymentRecord(tPaymentRecord));
    }

    /**
     * 修改支付记录管理
     */
    @PreAuthorize("@ss.hasPermi('order:paymentRecord:edit')")
    @Log(title = "支付记录管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TPaymentRecord tPaymentRecord)
    {
        return toAjax(tPaymentRecordService.updateTPaymentRecord(tPaymentRecord));
    }

    /**
     * 删除支付记录管理
     */
    @PreAuthorize("@ss.hasPermi('order:paymentRecord:remove')")
    @Log(title = "支付记录管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{paymentIds}")
    public AjaxResult remove(@PathVariable Long[] paymentIds)
    {
        return toAjax(tPaymentRecordService.deleteTPaymentRecordByPaymentIds(paymentIds));
    }
}
