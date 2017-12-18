package com.shsxt.xm.api.dto;

import com.shsxt.xm.api.po.BasItem;

import java.io.Serializable;

public class BasItemDto extends BasItem implements Serializable {

    private static final long serialVersionUID = -1338472780985117857L;

    //继承basitem全部字段,包含bas_item_source中的总分
    private Integer total;

    //设置剩余时间字段
    private Long syTime;

    public Long getSyTime() {
        return syTime;
    }

    public void setSyTime(Long syTime) {
        this.syTime = syTime;
    }

    @Override
    public String toString() {
        return super.toString() + " BasItemDto{" +
                "total=" + total +
                '}';
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
