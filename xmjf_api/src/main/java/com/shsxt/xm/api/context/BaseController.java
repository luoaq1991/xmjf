package com.shsxt.xm.api.context;

import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

public class BaseController {

    @ModelAttribute
    public void preMethod(HttpServletRequest request){
        request.setAttribute("ctx",request.getContextPath());
    }
}
