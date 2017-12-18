package com.shsxt.xm.api.query;

import java.io.Serializable;

public class BasItemQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = -7367356455289607243L;
    //项目期限:1(0-30),2(31-90),3(90+)
    private Integer itemCycle;

    //项目类型:1. 车商宝..
    private Integer itemType;

    //是否为历史项目,1是,0非
    private Integer isHistory;

    public Integer getItemCycle() {
        return itemCycle;
    }

    public void setItemCycle(Integer itemCycle) {
        this.itemCycle = itemCycle;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public Integer getIsHistory() {
        return isHistory;
    }

    public void setIsHistory(Integer isHistory) {
        this.isHistory = isHistory;
    }
}
