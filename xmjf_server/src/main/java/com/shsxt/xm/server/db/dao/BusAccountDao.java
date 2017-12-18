package com.shsxt.xm.server.db.dao;

import com.shsxt.xm.api.po.BusAccount;
import com.shsxt.xm.server.base.BaseDao;
import com.shsxt.xm.server.providers.BusAccountProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

public interface BusAccountDao extends BaseDao<BusAccount> {

    //立即投资页面下,个人账户信息查询


    @SelectProvider(type = BusAccountProvider.class,method = "getQueryBusAccountByUserIdSql")
    public BusAccount queryBusAccountByUserId(@Param("userId") Integer userId);

}