package com.shsxt.xm.web.controller;

import com.shsxt.xm.api.po.BusItemInvest;
import com.shsxt.xm.api.query.BusItemInvestQuery;
import com.shsxt.xm.api.service.IBusItemInvestService;
import com.shsxt.xm.api.utils.PageList;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("busItemInvest")
public class BusItemInvestController {

    @Resource
    private IBusItemInvestService busItemInvestService;

    //将查询参数包装后放入方法
    @RequestMapping("queryBusItemInvestByItemId")
    @ResponseBody
    public PageList queryBusItemInvestByItemId(BusItemInvestQuery busItemInvestQuery) {
        return busItemInvestService.queryBusItemInvestByItemId(busItemInvestQuery);
    }
}
