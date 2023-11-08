package com.lambda.telegram.bot.enums;

import org.lambda.framework.common.exception.ExceptionEnumFunction;

public enum BotExceptionEnum implements ExceptionEnumFunction {
    EA_ACCESS_000("EA_ACCESS_000","目标对象必须存在");










    private String code;

    private String message;
    // 构造方法
    private BotExceptionEnum(String code, String message) {
        this.code = code;
        this.message = message;

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
