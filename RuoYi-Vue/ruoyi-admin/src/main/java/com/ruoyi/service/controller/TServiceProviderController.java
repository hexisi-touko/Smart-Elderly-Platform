package com.ruoyi.service.controller;

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
import com.ruoyi.service.domain.TServiceProvider;
import com.ruoyi.service.service.ITServiceProviderService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 服务商管理Controller
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@RestController
@RequestMapping("/service/serviceProvider")
public class TServiceProviderController extends BaseController
{
    @Autowired
    private ITServiceProviderService tServiceProviderService;

    /**
     * 查询服务商管理列表
     */
    @PreAuthorize("@ss.hasPermi('service:serviceProvider:list')")
    @GetMapping("/list")
    public TableDataInfo list(TServiceProvider tServiceProvider)
    {
        startPage();
        List<TServiceProvider> list = tServiceProviderService.selectTServiceProviderList(tServiceProvider);
        return getDataTable(list);
    }

    /**
     * 导出服务商管理列表
     */
    @PreAuthorize("@ss.hasPermi('service:serviceProvider:export')")
    @Log(title = "服务商管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TServiceProvider tServiceProvider)
    {
        List<TServiceProvider> list = tServiceProviderService.selectTServiceProviderList(tServiceProvider);
        ExcelUtil<TServiceProvider> util = new ExcelUtil<TServiceProvider>(TServiceProvider.class);
        util.exportExcel(response, list, "服务商管理数据");
    }

    /**
     * 获取服务商管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('service:serviceProvider:query')")
    @GetMapping(value = "/{providerId}")
    public AjaxResult getInfo(@PathVariable("providerId") Long providerId)
    {
        return success(tServiceProviderService.selectTServiceProviderByProviderId(providerId));
    }

    /**
     * 新增服务商管理
     */
    @PreAuthorize("@ss.hasPermi('service:serviceProvider:add')")
    @Log(title = "服务商管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TServiceProvider tServiceProvider)
    {
        return toAjax(tServiceProviderService.insertTServiceProvider(tServiceProvider));
    }

    /**
     * 修改服务商管理
     */
    @PreAuthorize("@ss.hasPermi('service:serviceProvider:edit')")
    @Log(title = "服务商管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TServiceProvider tServiceProvider)
    {
        return toAjax(tServiceProviderService.updateTServiceProvider(tServiceProvider));
    }

    /**
     * 删除服务商管理
     */
    @PreAuthorize("@ss.hasPermi('service:serviceProvider:remove')")
    @Log(title = "服务商管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{providerIds}")
    public AjaxResult remove(@PathVariable Long[] providerIds)
    {
        return toAjax(tServiceProviderService.deleteTServiceProviderByProviderIds(providerIds));
    }
}
