package com.shsxt.xm.server;

public enum SmsType {
    //枚举短信通知的类

    REGISTER(1),
    NOTIFY(2);

    private Integer type;

    SmsType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

}
