package com.shsxt.xm.web.controller;

import com.shsxt.xm.api.exceptions.ParamsExcetion;
import com.shsxt.xm.api.model.ResultInfo;
import com.shsxt.xm.api.constant.P2PConstant;
import com.shsxt.xm.api.service.ISmsService;
import com.shsxt.xm.api.utils.RandomCodesUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@RequestMapping("sms")
public class SmsController {

    @Resource
    private ISmsService smsService;

    //注册:前台收到手机验证码后校验
    @RequestMapping("sendPhoneSms")
    @ResponseBody
    public ResultInfo sendPhoneSms(String phone, String picCode, Integer type, HttpSession session){

        ResultInfo resultInfo=new ResultInfo();

        String sessionPicCode=(String)session.getAttribute(P2PConstant.PICTURE_VERIFY_CODE);

        //若超时,则不必对
        if (StringUtils.isBlank(sessionPicCode)){
            resultInfo.setCode(P2PConstant.OPS_FAILED_CODE);
            resultInfo.setMsg("验证码已失效");
            return resultInfo;
        }

        if (!picCode.equals(sessionPicCode)){
            resultInfo.setCode(P2PConstant.OPS_FAILED_CODE);
            resultInfo.setMsg("验证码不匹配");
            return resultInfo;
        }

        try {
            String code=RandomCodesUtils.createRandom(true,4);
            //smsService.sendPhoneSms(phone, code,type);

            //手机验证码,存入session
            session.setAttribute(P2PConstant.PHONE_VERIFY_CODE+phone,code);
            //发送验证码的时间存入session
            session.setAttribute(P2PConstant.PHONE_VERIFY_CODE_EXPIRE_TIME+phone,new Date());

        } catch (ParamsExcetion e) {
            e.printStackTrace();
            resultInfo.setCode(P2PConstant.OPS_FAILED_CODE);
            resultInfo.setMsg(P2PConstant.OPS_FAILED_MSG);
        } catch (Exception e){
            e.printStackTrace();
            resultInfo.setCode(P2PConstant.OPS_FAILED_CODE);
            resultInfo.setMsg(P2PConstant.OPS_FAILED_MSG);
        }

        return resultInfo;

    }
}
