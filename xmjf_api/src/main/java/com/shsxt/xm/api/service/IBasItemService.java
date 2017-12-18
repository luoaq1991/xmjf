package com.shsxt.xm.api.service;

import com.shsxt.xm.api.dto.BasItemDto;
import com.shsxt.xm.api.model.ResultInfo;
import com.shsxt.xm.api.query.BasItemQuery;
import com.shsxt.xm.api.utils.PageList;

public interface IBasItemService {
    public PageList queryBasItemsByParams(BasItemQuery basItemQuery);

    public ResultInfo updateBasItemStatusToOpen(Integer itemId);

    public BasItemDto queryBasItemByItemId(Integer itemId);
}
