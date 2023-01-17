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
    SUCCESS(200,"Success"),
    FAIL(201, "Fail"),
    INVALID_TOKEN(202, "Invalid token or expired"),
    NOT_AUTHENTICATED(401, "Not authenticated"),
    PERMISSION_DENIED(403, "Permission denied");

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
