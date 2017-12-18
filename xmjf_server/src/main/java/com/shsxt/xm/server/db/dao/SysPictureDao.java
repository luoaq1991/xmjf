package com.shsxt.xm.server.db.dao;

import com.shsxt.xm.api.po.SysPicture;
import com.shsxt.xm.server.base.BaseDao;

import java.util.List;

public interface SysPictureDao extends BaseDao<SysPicture> {

    public List<SysPicture> queryPicturesByItemId(Integer itemId);


}