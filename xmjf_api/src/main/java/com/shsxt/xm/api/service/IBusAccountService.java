package com.shsxt.xm.api.service;

import com.shsxt.xm.api.dto.PayDto;
import com.shsxt.xm.api.po.BusAccount;

import java.math.BigDecimal;

public interface IBusAccountService {
    public BusAccount queryBusAccountByUserId(Integer userId);

    //构建支付请求的参数,返回paydto类型的参数
    public PayDto addRechargeRequestInfo(BigDecimal amount, String businessPassword, Integer userId);


}
