package com.daiyanping.springcloud.enums;

public enum EnableEnum {
    DELETE(-1), DISABLE(0), ENABLE(1), DELETED(101);

    private int value;

    public int getValue() {
        return value;
    }

    EnableEnum(int value) {
        this.value = value;
    }
}

