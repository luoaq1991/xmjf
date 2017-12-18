package com.shsxt.xm.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @RequestMapping("index")
    public String index(HttpServletRequest request){
        request.setAttribute("ctx",request.getContextPath());
        return "index";
    }

    @RequestMapping("login")
    public String login(HttpServletRequest request){
        request.setAttribute("ctx",request.getContextPath());
        return "login";

    }

    //跳转快捷登录页面
    @RequestMapping("quickLoginPage")
    public String quickLoginPage(HttpServletRequest request){
        request.setAttribute("ctx",request.getContextPath());
        return"quick_login";
    }


    @RequestMapping("register")
    public  String register(HttpServletRequest request){
        request.setAttribute("ctx",request.getContextPath());
        return "register";

    }




}
