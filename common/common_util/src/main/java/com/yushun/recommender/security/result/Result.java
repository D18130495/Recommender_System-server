package com.yushun.recommender.security.result;

/**
 * <p>
 * Return Result
 * </p>
 *
 * @author yushun zeng
 * @since 2022-12-30
 */

public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public Result(){}

    protected static <T> Result<T> build(T data) {
        Result<T> result = new Result<T>();

        if (data != null)
            result.setData(data);

        return result;
    }

    public static <T> Result<T> build(T body, ResultCodeEnum resultCodeEnum) {
        Result<T> result = build(body);
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());

        return result;
    }

    public static <T> Result<T> build(Integer code, String message) {
        Result<T> result = build(null);
        result.setCode(code);
        result.setMessage(message);

        return result;
    }

    public static <T> Result<T> ok(){
        return Result.ok(null);
    }

    /**
     * success
     * @param data
     * @param <T>
     * @return
     */
    public static<T> Result<T> ok(T data){
        Result<T> result = build(data);

        return build(data, ResultCodeEnum.SUCCESS);
    }

    public static<T> Result<T> fail(){
        return Result.fail(null);
    }

    /**
     * fail
     * @param data
     * @param <T>
     * @return
     */
    public static<T> Result<T> fail(T data) {
        Result<T> result = build(data);

        return build(data, ResultCodeEnum.FAIL);
    }

    public static<T> Result<T> noPermission(){
        return Result.noPermission(null);
    }

    /**
     * permission denied
     * @param data
     * @param <T>
     * @return
     */
    public static<T> Result<T> noPermission(T data) {
        Result<T> result = build(data);

        return build(data, ResultCodeEnum.PERMISSION);
    }

    public Result<T> message(String msg) {
        this.setMessage(msg);

        return this;
    }

    public Result<T> code(Integer code) {
        this.setCode(code);

        return this;
    }

    public boolean isOk() {
        if(this.getCode().intValue() == ResultCodeEnum.SUCCESS.getCode().intValue()) {
            return true;
        }

        return false;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
