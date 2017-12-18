package com.shsxt.xm.server.service;

import com.shsxt.xm.api.constant.P2PConstant;
import com.shsxt.xm.api.po.*;
import com.shsxt.xm.api.service.IBaseUserService;
import com.shsxt.xm.api.utils.AssertUtil;
import com.shsxt.xm.api.utils.RandomCodesUtils;
import com.shsxt.xm.server.db.dao.*;
import com.shsxt.xm.server.utils.DateUtils;
import com.shsxt.xm.server.utils.MD5;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class BaseUserServiceImpl implements IBaseUserService {

    @Resource
    private BaseUserDao baseUserDao;
    @Resource
    private BasUserInfoDao basUserInfoDao;
    @Resource
    private BasUserSecurityDao basUserSecurityDao;
    @Resource
    private BusAccountDao busAccountDao;
    @Resource
    private BusUserIntegralDao busUserIntegralDao;
    @Resource
    private BusUserStatDao busUserStatDao;
    @Resource
    private BasExperiencedGoldDao basExperiencedGoldDao;
    @Resource
    private SysLogDao sysLogDao;



    @Override
    public BaseUser queryBaseUserById(Integer id) {
        return baseUserDao.queryBaseUserById(id);
    }

    @Override
    public BaseUser queryBaseUserByPhone(String phone) {
        return baseUserDao.queryBaseUserByPhone(phone);
    }

    @Override
    public void saveBaseUser(String phone, String password) {

        //初始化表格
        Integer userId=initBaseUser(phone, password);

        initBaseUserInfo(userId);

        initBaseUserSecurity(userId);

        initBusAccount(userId);

        initBusUserIntegral(userId);

        initBusUserStat(userId);

        initBasExperiencedGold(userId);

        initSysLog(userId);

    }

    //传统登录
    @Override
    public BaseUser userLogin(String phone, String password) {

        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(password),"密码不能为空");
        BaseUser baseUser=baseUserDao.queryBaseUserByPhone(phone);
        AssertUtil.isTrue(null==baseUser,"用户不存在");

        //密码加盐值后加密,再与数据库数据进行比对
        String salt=baseUser.getSalt();
        password=MD5.toMD5(password+salt);
        AssertUtil.isTrue(!password.equals(baseUser.getPassword()),"密码不正确");
        return baseUser;


    }

    //快捷登录
    @Override
    public BaseUser quickLogin(String phone) {
        //校验参数非空
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号不能为空");
        BaseUser baseUser=baseUserDao.queryBaseUserByPhone(phone);
        //校验参数是否存在
        AssertUtil.isTrue(null==baseUser,"该用户不存在!");
        return baseUser;

    }

    private void initSysLog(Integer userId) {
        SysLog sysLog=new SysLog();
        sysLog.setAddtime(new Date());
        sysLog.setUserId(userId);
        sysLog.setCode("REGISTER");
        sysLog.setOperating("用户注册");
        sysLog.setResult(1);
        sysLog.setType(4);
        AssertUtil.isTrue(sysLogDao.insert(sysLog)<1,P2PConstant.OPS_FAILED_MSG);


    }

    private void initBasExperiencedGold(Integer userId) {
        BasExperiencedGold basExperiencedGold=new BasExperiencedGold();
        basExperiencedGold.setAddtime(new Date());
        basExperiencedGold.setAmount(BigDecimal.valueOf(2888L));
        basExperiencedGold.setGoldName("注册体验金");
        basExperiencedGold.setRate(BigDecimal.valueOf(10));
        basExperiencedGold.setStatus(2);
        basExperiencedGold.setUsefulLife(3);
        basExperiencedGold.setUserId(userId);
        basExperiencedGold.setWay("register");
        basExperiencedGold.setExpiredTime(DateUtils.addTime(1,new Date(),30));
        AssertUtil.isTrue(basExperiencedGoldDao.insert(basExperiencedGold)<1,P2PConstant.OPS_FAILED_MSG);
    }

    private void initBusUserStat(Integer userId) {
        BusUserStat busUserStat=new BusUserStat();
        busUserStat.setUserId(userId);
        AssertUtil.isTrue(busUserStatDao.insert(busUserStat)<1,P2PConstant.OPS_FAILED_MSG);

    }

    private void initBusUserIntegral(Integer userId) {
        BusUserIntegral busUserIntegral=new BusUserIntegral();
        busUserIntegral.setId(userId);
        busUserIntegral.setTotal(0);
        busUserIntegral.setUsable(0);
        AssertUtil.isTrue(busUserIntegralDao.insert(busUserIntegral)<1,P2PConstant.OPS_FAILED_MSG);
    }

    private void initBusAccount(Integer userId) {

        BusAccount busAccount=new BusAccount();
        busAccount.setUserId(userId);
        busAccount.setCash(BigDecimal.ZERO);
        AssertUtil.isTrue(busAccountDao.insert(busAccount)<1,P2PConstant.OPS_FAILED_MSG);


    }

    private void initBaseUserSecurity(Integer userId) {

        BasUserSecurity basUserSecurity=new BasUserSecurity();
        basUserSecurity.setId(userId);
        basUserSecurity.setPhoneStatus(1);
        AssertUtil.isTrue(basUserSecurityDao.insert(basUserSecurity)<1,P2PConstant.OPS_FAILED_MSG);
    }

    //通过userId关联多张表
    private void initBaseUserInfo(Integer userId) {
        BasUserInfo basUserInfo=new BasUserInfo();
        basUserInfo.setUserId(userId);
        //设置邀请码
        String inviteCode=RandomCodesUtils.createRandom(false,6);
        basUserInfo.setInviteCode(inviteCode);
        AssertUtil.isTrue(basUserInfoDao.insert(basUserInfo)<1,P2PConstant.OPS_FAILED_MSG);


    }



    private Integer initBaseUser(String phone, String password) {
//校验参数
        AssertUtil.isTrue(StringUtils.isBlank(phone), "手机号不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(password), "密码不能为空");
        AssertUtil.isTrue(null != queryBaseUserByPhone(phone), "该手机号已注册");
        //校验完成添加信息
        BaseUser baseUser = new BaseUser();
        baseUser.setAddtime(new Date());
        baseUser.setMobile(phone);

        //设置盐值
        String salt = RandomCodesUtils.createRandom(false, 4);
        baseUser.setSalt(salt);

        //密码+盐值加密
        password = MD5.toMD5(password + salt);
        baseUser.setPassword(password);
        baseUser.setReferer("PC");
        baseUser.setStatus(1);
        baseUser.setType(1);


        Integer userId = baseUserDao.insert(baseUser);
        //判断是否操作成功
        AssertUtil.isTrue(null == userId || 0 == userId, P2PConstant.OPS_FAILED_MSG);
        String year = new SimpleDateFormat("yyyy").format(new Date());
        baseUser.setUsername("SXT_P2P" + year+userId);

        AssertUtil.isTrue(baseUserDao.update(baseUser) < 1, P2PConstant.OPS_FAILED_MSG);

        return userId;

    }
}
