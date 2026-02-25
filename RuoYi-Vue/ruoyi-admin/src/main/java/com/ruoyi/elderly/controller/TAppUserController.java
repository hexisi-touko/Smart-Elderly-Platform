package com.ruoyi.elderly.controller;

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
import com.ruoyi.elderly.domain.TAppUser;
import com.ruoyi.elderly.service.ITAppUserService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * C端用户账号Controller
 * 
 * @author zhangTing
 * @date 2026-02-24
 */
@RestController
@RequestMapping("/elderly/user")
public class TAppUserController extends BaseController
{
    @Autowired
    private ITAppUserService tAppUserService;

    /**
     * 查询C端用户账号列表
     */
    @PreAuthorize("@ss.hasPermi('elderly:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(TAppUser tAppUser)
    {
        startPage();
        List<TAppUser> list = tAppUserService.selectTAppUserList(tAppUser);
        return getDataTable(list);
    }

    /**
     * 导出C端用户账号列表
     */
    @PreAuthorize("@ss.hasPermi('elderly:user:export')")
    @Log(title = "C端用户账号", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TAppUser tAppUser)
    {
        List<TAppUser> list = tAppUserService.selectTAppUserList(tAppUser);
        ExcelUtil<TAppUser> util = new ExcelUtil<TAppUser>(TAppUser.class);
        util.exportExcel(response, list, "C端用户账号数据");
    }

    /**
     * 获取C端用户账号详细信息
     */
    @PreAuthorize("@ss.hasPermi('elderly:user:query')")
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable("userId") Long userId)
    {
        return success(tAppUserService.selectTAppUserByUserId(userId));
    }

    /**
     * 新增C端用户账号
     */
    @PreAuthorize("@ss.hasPermi('elderly:user:add')")
    @Log(title = "C端用户账号", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TAppUser tAppUser)
    {
        return toAjax(tAppUserService.insertTAppUser(tAppUser));
    }

    /**
     * 修改C端用户账号
     */
    @PreAuthorize("@ss.hasPermi('elderly:user:edit')")
    @Log(title = "C端用户账号", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TAppUser tAppUser)
    {
        return toAjax(tAppUserService.updateTAppUser(tAppUser));
    }

    /**
     * 删除C端用户账号
     */
    @PreAuthorize("@ss.hasPermi('elderly:user:remove')")
    @Log(title = "C端用户账号", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(tAppUserService.deleteTAppUserByUserIds(userIds));
    }
}
