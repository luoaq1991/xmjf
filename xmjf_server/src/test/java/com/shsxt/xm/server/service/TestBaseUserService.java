package com.shsxt.xm.server.service;


import com.shsxt.xm.api.po.BaseUser;
import com.shsxt.xm.api.service.IBaseUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
public class TestBaseUserService {

    @Resource
    private IBaseUserService iBaseUserService;

    @Test
    public  void  test01(){
        BaseUser baseUser=iBaseUserService.queryBaseUserById(4);
        System.out.println(baseUser);
    }

    @Test
    public void test02(){
        iBaseUserService.saveBaseUser("13918008890","123456");
    }



}
