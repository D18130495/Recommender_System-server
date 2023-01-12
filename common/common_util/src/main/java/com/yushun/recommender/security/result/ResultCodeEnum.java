package com.yushun.recommender.security.result;

/**
 * <p>
 * Return Result Enum
 * </p>
 *
 * @author yushun zeng
 * @since 2022-12-30
 */

public enum ResultCodeEnum {

    SUCCESS(200,"success"),
    FAIL(201, "fail"),

    LOGIN_AUTH(208, "Not login"),
    PERMISSION(209, "Not Auth");

    private Integer code;
    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
