package com.ruoyi.service.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.service.domain.TServiceItem;
import com.ruoyi.service.service.ITServiceItemService;

/**
 * C端服务项目查询Controller
 */
@RestController
@RequestMapping("/app/service")
public class AppServiceController extends BaseController {

    @Autowired
    private ITServiceItemService tServiceItemService;

    /**
     * 免权限查询上架的服务项目列表
     */
    @GetMapping("/list")
    public TableDataInfo list(TServiceItem tServiceItem) {
        startPage();
        // 强制只查询状态为上架(例如0为上架)的服务
        tServiceItem.setStatus(0L); 
        List<TServiceItem> list = tServiceItemService.selectTServiceItemList(tServiceItem);
        return getDataTable(list);
    }
}
