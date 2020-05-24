package com.daiyanping.springcloud.util;

import java.io.Serializable;

/**
 * 操作结果集
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 5576237395711742681L;

    /**
     * 请求是否成功
     */
    private boolean success = false;

    /**
     * 响应码
     */
    private int code;

    /**
     * 响应描述
     */
    private String message;


    /**
     * 响应数据
     */

    private T data;

    public Result() {
    }

    public Result (int code, String message) {
        this.code = code;
        this.message = message;
    }


    public Result(int code, String message, T data) {


        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(boolean isSuccess, int code, String message) {
        this.success = isSuccess;
        this.code = code;
        this.message = message;
    }


    public Result(boolean isSuccess, int code, String message, T data) {

        this.success = isSuccess;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }


    public void setData(T data) {

        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
