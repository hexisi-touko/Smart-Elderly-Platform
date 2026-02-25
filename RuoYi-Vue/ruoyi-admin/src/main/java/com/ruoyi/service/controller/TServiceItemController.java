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
import com.ruoyi.service.domain.TServiceItem;
import com.ruoyi.service.service.ITServiceItemService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 服务项目管理Controller
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@RestController
@RequestMapping("/service/serviceItem")
public class TServiceItemController extends BaseController
{
    @Autowired
    private ITServiceItemService tServiceItemService;

    /**
     * 查询服务项目管理列表
     */
    @PreAuthorize("@ss.hasPermi('service:serviceItem:list')")
    @GetMapping("/list")
    public TableDataInfo list(TServiceItem tServiceItem)
    {
        startPage();
        List<TServiceItem> list = tServiceItemService.selectTServiceItemList(tServiceItem);
        return getDataTable(list);
    }

    /**
     * 导出服务项目管理列表
     */
    @PreAuthorize("@ss.hasPermi('service:serviceItem:export')")
    @Log(title = "服务项目管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TServiceItem tServiceItem)
    {
        List<TServiceItem> list = tServiceItemService.selectTServiceItemList(tServiceItem);
        ExcelUtil<TServiceItem> util = new ExcelUtil<TServiceItem>(TServiceItem.class);
        util.exportExcel(response, list, "服务项目管理数据");
    }

    /**
     * 获取服务项目管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('service:serviceItem:query')")
    @GetMapping(value = "/{itemId}")
    public AjaxResult getInfo(@PathVariable("itemId") Long itemId)
    {
        return success(tServiceItemService.selectTServiceItemByItemId(itemId));
    }

    /**
     * 新增服务项目管理
     */
    @PreAuthorize("@ss.hasPermi('service:serviceItem:add')")
    @Log(title = "服务项目管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TServiceItem tServiceItem)
    {
        return toAjax(tServiceItemService.insertTServiceItem(tServiceItem));
    }

    /**
     * 修改服务项目管理
     */
    @PreAuthorize("@ss.hasPermi('service:serviceItem:edit')")
    @Log(title = "服务项目管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TServiceItem tServiceItem)
    {
        return toAjax(tServiceItemService.updateTServiceItem(tServiceItem));
    }

    /**
     * 删除服务项目管理
     */
    @PreAuthorize("@ss.hasPermi('service:serviceItem:remove')")
    @Log(title = "服务项目管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{itemIds}")
    public AjaxResult remove(@PathVariable Long[] itemIds)
    {
        return toAjax(tServiceItemService.deleteTServiceItemByItemIds(itemIds));
    }
}
