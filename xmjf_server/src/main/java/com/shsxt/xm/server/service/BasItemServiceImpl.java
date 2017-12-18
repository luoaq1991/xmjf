package com.shsxt.xm.server.service;

import com.github.pagehelper.PageHelper;
import com.shsxt.xm.api.constant.P2PConstant;
import com.shsxt.xm.api.dto.BasItemDto;
import com.shsxt.xm.api.model.ResultInfo;
import com.shsxt.xm.api.query.BasItemQuery;
import com.shsxt.xm.api.service.IBasItemService;
import com.shsxt.xm.api.utils.AssertUtil;
import com.shsxt.xm.api.utils.PageList;
import com.shsxt.xm.server.db.dao.BasItemDao;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class BasItemServiceImpl implements IBasItemService {

    @Resource
    private BasItemDao basItemDao;

    @Override
    public PageList queryBasItemsByParams(BasItemQuery basItemQuery) {

        //启动分页插件
        PageHelper.startPage(basItemQuery.getPageNum(),basItemQuery.getPageSize());
        List<BasItemDto> list=basItemDao.queryForPage(basItemQuery);

        //剩余时间设置,须在dto类中新增字段.
        if(!CollectionUtils.isEmpty(list)){
             for(BasItemDto basItemDto:list){
                 //如果记录为1,待开放
                 if(basItemDto.getItemStatus().equals(1)){
                     Date releaseTime=basItemDto.getReleaseTime();
                     Long syTime=(releaseTime.getTime()-new Date().getTime())/1000;
                     basItemDto.setSyTime(syTime) ;
                 }
             }
        }

        return new PageList(list);

    }

    @Override
    public ResultInfo updateBasItemStatusToOpen(Integer itemId) {
        AssertUtil.isTrue(null==basItemDao.queryById(itemId),"待更新项目不存在");
        AssertUtil.isTrue(basItemDao.updateBasItemStatusToOpen(itemId)<1,P2PConstant.OPS_FAILED_MSG);


        return null;
    }

    //根据itemid查询详情
    @Override
    public BasItemDto queryBasItemByItemId(Integer itemId) {

        return basItemDao.queryById(itemId);

    }


}
