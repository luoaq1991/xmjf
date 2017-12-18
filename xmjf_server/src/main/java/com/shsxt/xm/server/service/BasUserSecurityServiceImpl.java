package com.shsxt.xm.server.service;

import com.shsxt.xm.api.constant.P2PConstant;
import com.shsxt.xm.api.model.ResultInfo;
import com.shsxt.xm.api.po.BasUserSecurity;
import com.shsxt.xm.api.service.IBasUserSecurityService;
import com.shsxt.xm.api.service.IBaseUserService;
import com.shsxt.xm.api.utils.AssertUtil;
import com.shsxt.xm.server.db.dao.BasUserSecurityDao;
import com.shsxt.xm.server.utils.MD5;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.Resource;

@Service
public class BasUserSecurityServiceImpl implements IBasUserSecurityService {

    @Resource
    private BasUserSecurityDao basUserSecurityDao;

    @Resource
    private IBaseUserService baseUserService;

    @Override
    public BasUserSecurity queryBasUserSecurityByUserId(Integer userId) {
        return basUserSecurityDao.queryBasUserSecurityByUserId(userId);
    }


    //实名认证方法实现
    @Override
    public ResultInfo userAuthCheck(Integer userId) {
        BasUserSecurity basUserSecurity=basUserSecurityDao.queryBasUserSecurityByUserId(userId);
        ResultInfo resultInfo=new ResultInfo();
        Integer status=basUserSecurity.getRealnameStatus();
        if (status==0){
            //用户未认证
            resultInfo.setCode(301);
            resultInfo.setMsg("用户未进行实名认证");
        }
        if (status==1){
            resultInfo.setCode(200);
            resultInfo.setMsg("用户以已认证");
        }
        if (status==2){
            resultInfo.setCode(302);
            resultInfo.setMsg("认证已提交,正在认证中");
        }

        return resultInfo;
    }

    @Override
    public void doUserAuth(String realName, String idCard, String businessPassword, String confirmPassword, Integer userId) {
        //验证信息
        AssertUtil.isTrue(null==userId||userId==0||null==baseUserService.queryBaseUserById(userId),"用户不存在或未登录");
        AssertUtil.isTrue(StringUtils.isBlank(realName),"真实姓名不能为空");
        //支付密码非空校验以及和确认密码校验
        AssertUtil.isTrue(StringUtils.isBlank(businessPassword)||StringUtils.isBlank(confirmPassword)||!(businessPassword.equals(confirmPassword)),"密码输入有误");
        //身份证校验
        AssertUtil.isTrue(StringUtils.isBlank(idCard),"身份证不能为空");
        AssertUtil.isTrue(idCard.length()!=18,"身份证长度不合法");
        AssertUtil.isTrue(null!=basUserSecurityDao.queryBasUserSecurityByIdCard(idCard),"身份证已被认证");

        //验证后设置参数,对已有用户id的表格进行设置
        BasUserSecurity basUserSecurity=basUserSecurityDao.queryBasUserSecurityByUserId(userId);
        basUserSecurity.setPaymentPassword(MD5.toMD5(businessPassword));
        basUserSecurity.setRealname(realName);
        basUserSecurity.setIdentifyCard(idCard);
        basUserSecurity.setRealnameStatus(1);
        //更新数据库
        AssertUtil.isTrue(basUserSecurityDao.update(basUserSecurity)<1, P2PConstant.OPS_FAILED_MSG);


    }
}
