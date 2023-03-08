package com.panpan.common.exceptionenum;

/**
 * @author panpan
 * @create 2021-06-02 下午7:19
 */
public enum ExceptionNumber {
    VALID_EXCEPTHION(10000,"参数校验失败"),
    UNKNOWN_EXCEPTHION(10001,"系统未知异常"),
    PUT_ESSKU_EXCEPTHION(20000,"商品上架异常"),
    UNREGISTERED_EXCEPTION(30000,"该账号未注册"),
    LOGGING_WRONG_PASSWORD(30001,"密码错误");


    private final Integer code;
    private final String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    private ExceptionNumber(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
