package com.shsxt.xm.server.db.dao;

import com.shsxt.xm.api.po.BaseUser;
import com.shsxt.xm.server.base.BaseDao;
import org.apache.ibatis.annotations.Param;

public interface BaseUserDao extends BaseDao<BaseUser>{

    public BaseUser queryBaseUserById(@Param("id") Integer id);

    public BaseUser queryBaseUserByPhone(@Param("phone") String phone);


}