package com.shsxt.xm.api.service;

import com.shsxt.xm.api.po.BaseUser;

public interface IBaseUserService {

    //查询,是否已有用户
    public BaseUser queryBaseUserById(Integer id);
    //快捷登录查询用户是否存在
    public BaseUser queryBaseUserByPhone(String phone);

    //注册,保存用户
    public  void saveBaseUser(String phone,String password);

    //传统手机号+密码登录
    public BaseUser userLogin(String phone,String passsword);

    //快捷登录方法
    public BaseUser quickLogin(String phone);
}
