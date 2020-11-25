package com.example.xcschoolserver.common;

import java.io.Serializable;

/**
 * 全局异常类
 */
public class ServiceException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;

    private String message;

    public ServiceException() {

        super();

    }

    public ServiceException(String message) {

        super(message);

    }

    public ServiceException(int code, String message) {
        this.code=code;
        this.message=message;



    }

    public int getCode() {

        return code;

    }

    public void setCode(int code) {

        this.code = code;

    }

    @Override

    public String getMessage() {

        return message;

    }

    public void setMessage(String message) {

        this.message = message;

    }

}

