package com.shsxt.xm.web.controller;

import com.shsxt.xm.api.constant.P2PConstant;
import com.shsxt.xm.api.exceptions.ParamsExcetion;
import com.shsxt.xm.api.model.ResultInfo;
import com.shsxt.xm.api.po.BaseUser;
import com.shsxt.xm.api.service.IBasUserSecurityService;
import com.shsxt.xm.api.service.IBaseUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@RequestMapping("user")
public class BaseUserController {

    @Resource
    private IBaseUserService baseUserService;

    @Resource
    private IBasUserSecurityService basUserSecurityService;

    @ResponseBody
    @RequestMapping("queryBaseUserById")
    public BaseUser queryBaseUserById(Integer id) {
        return baseUserService.queryBaseUserById(id);

    }

    @RequestMapping("register")
    @ResponseBody
    public ResultInfo userRegister(String phone, String picCode, String code, String password, HttpSession session) {

        ResultInfo resultInfo = new ResultInfo();
        //图片验证码校验
        String sessionPicCode = (String) session.getAttribute(P2PConstant.PICTURE_VERIFY_CODE);
        if (StringUtils.isBlank(sessionPicCode)) {
            resultInfo.setCode(P2PConstant.OPS_FAILED_CODE);
            resultInfo.setMsg("验证码已失效");
            return resultInfo;
        }

        if (!picCode.equals(sessionPicCode)) {
            resultInfo.setCode(P2PConstant.OPS_FAILED_CODE);
            resultInfo.setMsg("验证码不匹配");
            return resultInfo;
        }

        //手机验证码校验
        //取得系统声称校验码的时间
        Date sessionTime = (Date) session.getAttribute(P2PConstant.PHONE_VERIFY_CODE_EXPIRE_TIME + phone);
        //是否失效判定
        if (null == sessionTime) {
            resultInfo.setCode(P2PConstant.OPS_FAILED_CODE);
            resultInfo.setMsg("手机验证码失效");
            return resultInfo;

        }
        Date currTime = new Date();
        long time = currTime.getTime() - sessionTime.getTime() / 1000;
        if (time > 180) {
            resultInfo.setCode(P2PConstant.OPS_FAILED_CODE);
            resultInfo.setMsg("手机验证码失效");
            return resultInfo;
        }

        //比对手机验证码参数
        String sessionCode = (String) session.getAttribute(P2PConstant.PHONE_VERIFY_CODE + phone);
        if (!sessionCode.equals(code)) {
            resultInfo.setCode(P2PConstant.OPS_FAILED_CODE);
            resultInfo.setMsg("手机验证码不正确");
            return resultInfo;
        }

        try {
            baseUserService.saveBaseUser(phone, password);
            //清理session值
            session.removeAttribute(P2PConstant.PICTURE_VERIFY_CODE);
            session.removeAttribute(P2PConstant.PHONE_VERIFY_CODE + phone);
            session.removeAttribute(P2PConstant.PHONE_VERIFY_CODE_EXPIRE_TIME + phone);


        } catch (ParamsExcetion e) {
            e.printStackTrace();
            resultInfo.setCode(P2PConstant.OPS_FAILED_CODE);
            resultInfo.setMsg(e.getErrorMsg());
        } catch (Exception e) {
            resultInfo.setCode(P2PConstant.OPS_FAILED_CODE);
            resultInfo.setMsg(P2PConstant.OPS_FAILED_MSG);

        }

        return resultInfo;

    }

    //用户登录方法
    @RequestMapping("userLogin")
    @ResponseBody
    public ResultInfo userLogin(String phone, String password, HttpSession session) {

        ResultInfo resultInfo = new ResultInfo();

        try {
            BaseUser baseUser = baseUserService.userLogin(phone, password);

            //存入session,方便前台调用
            session.setAttribute("userInfo", baseUser);
        } catch (ParamsExcetion e) {
            e.printStackTrace();
            resultInfo.setCode(P2PConstant.OPS_FAILED_CODE);
            resultInfo.setMsg(e.getErrorMsg());
        } catch (Exception e) {
            resultInfo.setCode(P2PConstant.OPS_FAILED_CODE);
            resultInfo.setMsg(P2PConstant.OPS_FAILED_MSG);

        }

        return resultInfo;

    }

    @RequestMapping("quickLogin")
    @ResponseBody
    public ResultInfo quickLogin(String phone, String picCode, String code, HttpSession session) {

        ResultInfo resultInfo = new ResultInfo();

        //图形验证码比对
        String sessionPicCode = (String) session.getAttribute(P2PConstant.PICTURE_VERIFY_CODE);
        if (StringUtils.isBlank(sessionPicCode)) {
            resultInfo.setCode(P2PConstant.OPS_FAILED_CODE);
            resultInfo.setMsg("验证码已失效");
            return resultInfo;
        }

        if (!picCode.equals(sessionPicCode)) {
            resultInfo.setCode(P2PConstant.OPS_FAILED_CODE);
            resultInfo.setMsg("验证码不匹配");
            return resultInfo;
        }

        //比对手机验证码

        Date sessionTime = (Date) session.getAttribute(P2PConstant.PHONE_VERIFY_CODE_EXPIRE_TIME + phone);
        //是否失效判定
        if (null == sessionTime) {
            resultInfo.setCode(P2PConstant.OPS_FAILED_CODE);
            resultInfo.setMsg("手机验证码失效");
            return resultInfo;

        }
        Date currTime = new Date();
        long time = currTime.getTime() - sessionTime.getTime() / 1000;
        if (time > 180) {
            resultInfo.setCode(P2PConstant.OPS_FAILED_CODE);
            resultInfo.setMsg("手机验证码失效");
            return resultInfo;
        }

        //比对手机验证码参数
        String sessionCode = (String) session.getAttribute(P2PConstant.PHONE_VERIFY_CODE + phone);
        if (!sessionCode.equals(code)) {
            resultInfo.setCode(P2PConstant.OPS_FAILED_CODE);
            resultInfo.setMsg("手机验证码不正确");
            return resultInfo;
        }

        try {
            //接受短信登录的返回值,以便于主页显示
            BaseUser baseUser = baseUserService.quickLogin(phone);
            session.setAttribute("userInfo", baseUser);
        } catch (ParamsExcetion e) {
            e.printStackTrace();
            resultInfo.setCode(P2PConstant.OPS_FAILED_CODE);
            resultInfo.setMsg(e.getErrorMsg());
        } catch (Exception e) {
            resultInfo.setCode(P2PConstant.OPS_FAILED_CODE);
            resultInfo.setMsg(P2PConstant.OPS_FAILED_MSG);

        }

        return resultInfo;

    }

    @RequestMapping("exit")
    public String exit(HttpServletRequest request) {
        request.getSession().removeAttribute("userInfo");
        request.setAttribute("ctx", request.getContextPath());
        return "login";

    }

    //跳转用户实名认证页面
    @RequestMapping("auth")
    public String toAuthPage(HttpServletRequest request) {
        request.setAttribute("ctx", request.getContextPath());
        return "user/auth";
    }


    //实名认证判断及跳转
    @RequestMapping("userAuthCheck")
    @ResponseBody
    public ResultInfo userAuthCheck(HttpServletRequest request) {
        //通过request获得用户信息,存入baseuser
        BaseUser baseUser = (BaseUser) request.getSession().getAttribute("userInfo");
        return basUserSecurityService.userAuthCheck(baseUser.getId());
    }

    //实名认证方法
    @RequestMapping("userAuth")
    @ResponseBody
    public ResultInfo doUserAuth(String realName, String idCard, String businessPassword,
                                 String confirmPassword, HttpSession session) {
        //获取userInfo,以取得用户账号,故需要session
        BaseUser baseUser=(BaseUser) session.getAttribute("userInfo");
        //创建resultInfo
        ResultInfo resultInfo=new ResultInfo();
        //通过service添加信息
        try {
            basUserSecurityService.doUserAuth(realName,idCard,businessPassword,confirmPassword,baseUser.getId());
        } catch (ParamsExcetion e) {
            e.printStackTrace();
            resultInfo.setCode(P2PConstant.OPS_FAILED_CODE);
            resultInfo.setMsg(e.getErrorMsg());
        }catch (Exception e){
            e.printStackTrace();
            resultInfo.setCode(P2PConstant.OPS_FAILED_CODE);
            resultInfo.setMsg(P2PConstant.OPS_FAILED_MSG);
        }

        return resultInfo;

    }


}
