package com.shsxt.xm.api.query;

import java.io.Serializable;

public class BaseQuery implements Serializable {

    private static final long serialVersionUID = 7983741380528264076L;

    private Integer pageNum = 1;
    private Integer pageSize = 10;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
