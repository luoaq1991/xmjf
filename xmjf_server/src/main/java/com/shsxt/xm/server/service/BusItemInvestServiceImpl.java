package com.shsxt.xm.server.service;

import com.github.pagehelper.PageHelper;
import com.shsxt.xm.api.po.BusItemInvest;
import com.shsxt.xm.api.query.BusItemInvestQuery;
import com.shsxt.xm.api.service.IBusItemInvestService;
import com.shsxt.xm.api.utils.PageList;
import com.shsxt.xm.server.db.dao.BusItemInvestDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BusItemInvestServiceImpl implements IBusItemInvestService {

    @Resource
    private BusItemInvestDao busItemInvestDao;


    @Override
    public PageList queryBusItemInvestByItemId(BusItemInvestQuery busItemInvestQuery) {
        PageHelper.startPage(busItemInvestQuery.getPageNum(),busItemInvestQuery.getPageSize());
        List<BusItemInvest> list=busItemInvestDao.queryForPage(busItemInvestQuery);
        return new PageList(list);

    }
}
