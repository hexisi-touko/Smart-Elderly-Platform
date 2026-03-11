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
import com.ruoyi.health.domain.TPhysicalExamReservation;
import com.ruoyi.health.service.ITPhysicalExamReservationService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 体检预约Controller
 * 
 * @author zhangTing
 * @date 2026-03-11
 */
@RestController
@RequestMapping("/health/physicalExamReservation")
public class TPhysicalExamReservationController extends BaseController
{
    @Autowired
    private ITPhysicalExamReservationService tPhysicalExamReservationService;

    /**
     * 查询体检预约列表
     */
    @PreAuthorize("@ss.hasPermi('health:physicalExamReservation:list')")
    @GetMapping("/list")
    public TableDataInfo list(TPhysicalExamReservation tPhysicalExamReservation)
    {
        startPage();
        List<TPhysicalExamReservation> list = tPhysicalExamReservationService.selectTPhysicalExamReservationList(tPhysicalExamReservation);
        return getDataTable(list);
    }

    /**
     * 导出体检预约列表
     */
    @PreAuthorize("@ss.hasPermi('health:physicalExamReservation:export')")
    @Log(title = "体检预约", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TPhysicalExamReservation tPhysicalExamReservation)
    {
        List<TPhysicalExamReservation> list = tPhysicalExamReservationService.selectTPhysicalExamReservationList(tPhysicalExamReservation);
        ExcelUtil<TPhysicalExamReservation> util = new ExcelUtil<TPhysicalExamReservation>(TPhysicalExamReservation.class);
        util.exportExcel(response, list, "体检预约数据");
    }

    /**
     * 获取体检预约详细信息
     */
    @PreAuthorize("@ss.hasPermi('health:physicalExamReservation:query')")
    @GetMapping(value = "/{reservationId}")
    public AjaxResult getInfo(@PathVariable("reservationId") Long reservationId)
    {
        return success(tPhysicalExamReservationService.selectTPhysicalExamReservationByReservationId(reservationId));
    }

    /**
     * 新增体检预约
     */
    @PreAuthorize("@ss.hasPermi('health:physicalExamReservation:add')")
    @Log(title = "体检预约", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TPhysicalExamReservation tPhysicalExamReservation)
    {
        return toAjax(tPhysicalExamReservationService.insertTPhysicalExamReservation(tPhysicalExamReservation));
    }

    /**
     * 修改体检预约
     */
    @PreAuthorize("@ss.hasPermi('health:physicalExamReservation:edit')")
    @Log(title = "体检预约", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TPhysicalExamReservation tPhysicalExamReservation)
    {
        return toAjax(tPhysicalExamReservationService.updateTPhysicalExamReservation(tPhysicalExamReservation));
    }

    /**
     * 删除体检预约
     */
    @PreAuthorize("@ss.hasPermi('health:physicalExamReservation:remove')")
    @Log(title = "体检预约", businessType = BusinessType.DELETE)
	@DeleteMapping("/{reservationIds}")
    public AjaxResult remove(@PathVariable Long[] reservationIds)
    {
        return toAjax(tPhysicalExamReservationService.deleteTPhysicalExamReservationByReservationIds(reservationIds));
    }
}
