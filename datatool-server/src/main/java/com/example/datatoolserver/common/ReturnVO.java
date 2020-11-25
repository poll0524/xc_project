package com.example.datatoolserver.common;
/**
 * 全局返回包装类
 * @param <T>
 */
public class ReturnVO<T> {

    public static final int SUCCESS = 200;

    public static final int ERROR =500;

    public static final int UNAUTHORIZED = 401;

    public static final int FORBIDDEN = 403;

    public static final String MESSAGE_SUCCESS ="SUCCESS";

    private T data;

    private int code = SUCCESS;

    private String message = MESSAGE_SUCCESS;

    public ReturnVO() {

    }

    public ReturnVO(T data) {

        this.data = data;

    }

    public ReturnVO(T data, int code, String message) {

        this.data = data;

        this.code = code;

        this.message = message;

    }

    public T getData() {

        return data;

    }

    public ReturnVO<T> setData(T data) {

        this.data = data;

        return this;

    }

    public int getCode() {

        return code;

    }

    public ReturnVO<T> setCode(int code) {

        this.code = code;

        return this;

    }

    public String getMessage() {

        return message;

    }

    public ReturnVO<T> setMessage(String message) {

        this.message = message;

        return this;

    }

    public ReturnVO<T> error(String message) {

        this.message = message;

        this.code = ERROR;

        return this;

    }

    public ReturnVO<T> error(int code, String message) {

        this.code = code;

        this.message = message;

        return this;

    }

    public ReturnVO<T> unauthorized(String message) {

        this.code = UNAUTHORIZED;

        this.message = message;

        return this;

    }

    public ReturnVO<T> success(T data) {

        this.data = data;

        this.code = SUCCESS;

        return this;

    }

}
