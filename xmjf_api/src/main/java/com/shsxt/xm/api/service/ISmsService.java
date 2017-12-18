package com.shsxt.xm.api.service;

public interface ISmsService {
    //发送手机短信.

    public void sendPhoneSms(String phone,String code,Integer type);
}
