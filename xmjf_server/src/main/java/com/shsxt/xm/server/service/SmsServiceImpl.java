package com.shsxt.xm.server.service;

import com.alibaba.fastjson.JSON;
import com.shsxt.xm.api.po.BaseUser;
import com.shsxt.xm.api.service.IBaseUserService;
import com.shsxt.xm.api.service.ISmsService;
import com.shsxt.xm.api.utils.AssertUtil;
import com.shsxt.xm.server.SmsType;
import com.shsxt.xm.api.constant.TaoBaoConstant;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.internal.util.TaobaoContext;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class SmsServiceImpl implements ISmsService {

    @Resource
    private IBaseUserService baseUserService;

    @Override
    public void sendPhoneSms(String phone, String code, Integer type) {

        AssertUtil.isTrue(StringUtils.isBlank(phone), "手机号不能为空!");
        //正则判断
        /*String phoneModel = "\"^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\\\d{8}$\"; ";
        AssertUtil.isTrue(!phoneModel.equals(phone), "手机号非法!");*/

        //验证码判断
        AssertUtil.isTrue(StringUtils.isBlank(code), "验证码不能为空");

        //type判断,匹配枚举
        AssertUtil.isTrue(null == type, "短信验证码类型不匹配");
        AssertUtil.isTrue(!type.equals(SmsType.NOTIFY.getType())
                && !type.equals(SmsType.REGISTER.getType()), "短信类型不匹配");

        if (type.equals(SmsType.REGISTER.getType())) {

            //注册时手机号检查,调用数据库
            BaseUser baseUser = baseUserService.queryBaseUserByPhone(phone);
            AssertUtil.isTrue(null != baseUser, "该手机已注册");
            //发送注册短信模版
            //doSend(phone, code, TaoBaoConstant.SMS_TEMATE_CODE_REGISTER);


        }

        if (type.equals(SmsType.NOTIFY.getType())) {
            //doSend(phone, code,TaoBaoConstant.SMS_TEMATE_CODE_LOGIN);

        }


    }

    public void doSend(String phone, String code,String templateCode) {
        /*TaobaoClient client = new DefaultTaobaoClient(TaoBaoConstant.SERVER_URL,
                TaoBaoConstant.APP_KEY, TaoBaoConstant.APP_SECRET);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend("");
        req.setSmsType(TaoBaoConstant.SMS_TYPE);
        req.setSmsFreeSignName(TaoBaoConstant.SMS_FREE_SIGN_NAME);
        Map<String, String> map = new HashMap<String, String>();
        map.put("code", code);
        req.setSmsParamString(JSON.toJSONString(map));
        req.setRecNum(phone);
        req.setSmsTemplateCode(templateCode);
        AlibabaAliqinFcSmsNumSendResponse rsp = null;
        try {
            rsp = client.execute(req);
            AssertUtil.isTrue(!rsp.isSuccess(),"短信发送失败,请稍后再试");
        } catch (ApiException e) {
            e.printStackTrace();
        }*/

    }
}
