package com.panpan.common.constant;

/**
 * @author panpan
 * @create 2021-09-07 下午8:27
 */

public enum AuthConst {
    SMS_CODE_PREFIX("auth:sms:code:"),
    AUTH_LOCK_PREFIX("auth:sms:lock:");
    private String value;
    AuthConst(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
