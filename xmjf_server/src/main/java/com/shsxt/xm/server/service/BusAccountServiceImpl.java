package com.shsxt.xm.server.service;

import com.shsxt.xm.api.dto.PayDto;
import com.shsxt.xm.api.po.BusAccount;
import com.shsxt.xm.api.service.IBusAccountService;
import com.shsxt.xm.server.db.dao.BusAccountDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class BusAccountServiceImpl implements IBusAccountService {


    @Resource
    private BusAccountDao busAccountDao;

    @Override
    public BusAccount queryBusAccountByUserId(Integer userId) {
        return busAccountDao.queryBusAccountByUserId(userId);
    }

    @Override
    public PayDto addRechargeRequestInfo(BigDecimal amount, String businessPassword, Integer userId) {
        //验证参数
        checkParams(amount,businessPassword,userId);
        
        

        return null;
    }

    //验证充值页面的参数
    private void checkParams(BigDecimal amount, String businessPassword, Integer userId) {
    }
}
