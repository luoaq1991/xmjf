package com.shsxt.xm.api.service;

import com.shsxt.xm.api.po.BusItemInvest;
import com.shsxt.xm.api.query.BusItemInvestQuery;
import com.shsxt.xm.api.utils.PageList;

import java.util.List;

public interface IBusItemInvestService {

    public PageList queryBusItemInvestByItemId(BusItemInvestQuery busItemInvestQuery);
}
