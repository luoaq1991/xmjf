package com.shsxt.xm.web.controller;

import com.shsxt.xm.api.constant.P2PConstant;
import com.shsxt.xm.api.context.BaseController;
import com.shsxt.xm.api.service.IBusAccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@Controller
@RequestMapping("account")
public class BusAccountController extends BaseController{

    @Resource
    private IBusAccountService busAccountService;

    //跳转账户充值页面
    @RequestMapping("rechargePage")
    public String toRechargePage(){
        return "user/recharge";
    }

    //页面点击确认充值后,跳转云通付支付页面,组装数据.
    @RequestMapping("doAccountRechargeToRechargePage")
    public String doAccountRechargeToRechargePage(BigDecimal amount, String picCode,String bussinessPassword,
                                                  HttpServletRequest request, Model model){
        model.addAttribute("ctx",request.getContextPath());
        //获取系统生成的图片验证码以比对
        String sessionPicCode=(String) request.getSession().getAttribute(P2PConstant.PICTURE_VERIFY_CODE);
        //判断
        if (StringUtils.isBlank(sessionPicCode)){
            System.out.println("验证码已失效");
            return "user/pay";
        }
        if (!picCode.equals(sessionPicCode)){
            System.out.println("验证码不匹配");
            return "user/pay";
        }

        return "";

    }


    //付款后回调
    @RequestMapping("callback")
    public String callBack(){
        //回调后续处理.

        return "";

    }




}
