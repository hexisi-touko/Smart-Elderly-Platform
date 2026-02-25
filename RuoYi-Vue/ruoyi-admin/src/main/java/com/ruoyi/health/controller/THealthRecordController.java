package com.ruoyi.health.controller;

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
import com.ruoyi.health.domain.THealthRecord;
import com.ruoyi.health.service.ITHealthRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 健康记录管理Controller
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@RestController
@RequestMapping("/health/healthRecord")
public class THealthRecordController extends BaseController
{
    @Autowired
    private ITHealthRecordService tHealthRecordService;

    /**
     * 查询健康记录管理列表
     */
    @PreAuthorize("@ss.hasPermi('health:healthRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(THealthRecord tHealthRecord)
    {
        startPage();
        List<THealthRecord> list = tHealthRecordService.selectTHealthRecordList(tHealthRecord);
        return getDataTable(list);
    }

    /**
     * 导出健康记录管理列表
     */
    @PreAuthorize("@ss.hasPermi('health:healthRecord:export')")
    @Log(title = "健康记录管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, THealthRecord tHealthRecord)
    {
        List<THealthRecord> list = tHealthRecordService.selectTHealthRecordList(tHealthRecord);
        ExcelUtil<THealthRecord> util = new ExcelUtil<THealthRecord>(THealthRecord.class);
        util.exportExcel(response, list, "健康记录管理数据");
    }

    /**
     * 获取健康记录管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('health:healthRecord:query')")
    @GetMapping(value = "/{recordId}")
    public AjaxResult getInfo(@PathVariable("recordId") Long recordId)
    {
        return success(tHealthRecordService.selectTHealthRecordByRecordId(recordId));
    }

    /**
     * 新增健康记录管理
     */
    @PreAuthorize("@ss.hasPermi('health:healthRecord:add')")
    @Log(title = "健康记录管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody THealthRecord tHealthRecord)
    {
        return toAjax(tHealthRecordService.insertTHealthRecord(tHealthRecord));
    }

    /**
     * 修改健康记录管理
     */
    @PreAuthorize("@ss.hasPermi('health:healthRecord:edit')")
    @Log(title = "健康记录管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody THealthRecord tHealthRecord)
    {
        return toAjax(tHealthRecordService.updateTHealthRecord(tHealthRecord));
    }

    /**
     * 删除健康记录管理
     */
    @PreAuthorize("@ss.hasPermi('health:healthRecord:remove')")
    @Log(title = "健康记录管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{recordIds}")
    public AjaxResult remove(@PathVariable Long[] recordIds)
    {
        return toAjax(tHealthRecordService.deleteTHealthRecordByRecordIds(recordIds));
    }
}
