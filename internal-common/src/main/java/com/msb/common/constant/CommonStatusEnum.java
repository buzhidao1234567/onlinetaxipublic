package com.msb.common.constant;

import lombok.Getter;

public enum CommonStatusEnum {

    SUCCESS(200,"success","成功"),
    FAIL(400,"fail","失败"),
    ;

    @Getter
    private int code;
    @Getter
    private String value;
    private String name;

    CommonStatusEnum(int code, String value, String name) {
        this.code = code;
        this.value = value;
        this.name = name;
    }
}
