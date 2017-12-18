package com.shsxt.xm.server.providers;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.jdbc.SQL;

/**
 * 通过@selectProvider,产生查询用的sql语句
 */
public class BusAccountProvider {
    public  String getQueryBusAccountByUserIdSql(@Param("userId")Integer userId){
        String sql=new SQL(){{
            SELECT("id,user_id as userId,total,usable,cash,frozen,wait,repay");
            FROM("bus_account");
            WHERE("user_id=#{userId}");
        }}.toString();
        //采用
       return sql;
    }
}
