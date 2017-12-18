package com.shsxt.xm.web.controller;

import com.shsxt.xm.api.constant.P2PConstant;
import com.shsxt.xm.api.dto.BasItemDto;
import com.shsxt.xm.api.exceptions.ParamsExcetion;
import com.shsxt.xm.api.model.ResultInfo;
import com.shsxt.xm.api.po.*;
import com.shsxt.xm.api.query.BasItemQuery;
import com.shsxt.xm.api.service.*;
import com.shsxt.xm.api.utils.PageList;
import org.apache.ibatis.annotations.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("basItem")
public class BasItemController {

    @Resource
    private IBasItemService basItemService;

    @Resource
    private IBusAccountService busAccountService;

    @Resource
    private IBasUserSecurityService basUserSecurityService;

    @Resource
    private IBusItemLoanService busItemLoanService;

    @Resource
    private ISysPictureService sysPictureService;


    //跳转页面,获取上下文地址
    @RequestMapping("list")
    public String toBasItemListPage(HttpServletRequest request) {

        request.setAttribute("ctx", request.getContextPath());
        return "item/invest_list";

    }

    //获取页码数据及项目数据,返回pageList
    @RequestMapping("queryBasItemsByParams")
    @ResponseBody
    public PageList queryBasItemsByParams(BasItemQuery basItemQuery) {
        return basItemService.queryBasItemsByParams(basItemQuery);
    }

    //倒计时后更新至开放状态,方法
    @RequestMapping("updateBasItemStatusToOpen")
    @ResponseBody
    public ResultInfo updateBasItemStatusToOpen(Integer itemId) {
        ResultInfo resultInfo = new ResultInfo();
        try {
            basItemService.updateBasItemStatusToOpen(itemId);
        } catch (ParamsExcetion e) {
            resultInfo.setCode(P2PConstant.OPS_FAILED_CODE);
            resultInfo.setMsg(e.getErrorMsg());
            e.printStackTrace();
        } catch (Exception e) {
            resultInfo.setCode(P2PConstant.OPS_FAILED_CODE);
            resultInfo.setMsg(P2PConstant.OPS_FAILED_MSG);
            e.printStackTrace();
        }
        return resultInfo;
    }

    //点击立即投资,跳转至新页面
    //根据itemId查询项目详情
    @RequestMapping("itemDetailPage")
    public String itemDetailPage(Integer itemId, ModelMap modelMap,HttpServletRequest request){

        //通过项目id获得项目基本信息
        BasItemDto basItemDto=basItemService.queryBasItemByItemId(itemId);
        //session获取用户信息
        BaseUser baseUser=(BaseUser)request.getSession().getAttribute("userInfo");
        //判断,若未登录,显示登录,已登录显示用户信息
        if(null!=baseUser){
            BusAccount busAccount=busAccountService.queryBusAccountByUserId(baseUser.getId());
            modelMap.addAttribute("busAccount",busAccount);

        }

        //获取贷款人的基本信息
        BasUserSecurity basUserSecurity=basUserSecurityService.queryBasUserSecurityByUserId(basItemDto.getItemUserId());
        BusItemLoan busItemLoan=busItemLoanService.queryBusItemLoanByItemId(itemId);
        //获取图片
        List<SysPicture> list=sysPictureService.queryPicturesByItemId(itemId);

        //添加至modelMap
        modelMap.addAttribute("loanUser",basUserSecurity);
        modelMap.addAttribute("busItemLoan",busItemLoan);
        modelMap.addAttribute("ctx",request.getContextPath());
        modelMap.addAttribute("item",basItemDto);
        modelMap.addAttribute("pics",list);
        return "item/details";
    }




}
