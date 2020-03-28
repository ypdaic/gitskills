package com.daiyanping.cms.result;

import com.daiyanping.cms.enums.ExceptionCodeEnum;
import com.daiyanping.cms.enums.FailureEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * 返回结果的工具类，包含了错误码
 */
public class ResultUtil {

    public static final int SUCCESS_CODE = 20000;

    public static final String QUERY_OK = "查询成功";
    public static final String ADD_OK = "添加成功";
    public static final String UPDATE_OK = "修改成功";
    public static final String DELETE_OK = "删除成功";
    public static final String OPERATE_OK = "操作成功";

    public static boolean SUCCESS_BOOL = true;
    public static boolean FAILURE_BOOL = false;

    /**
     * 请求成功，返回data为null
     *
     * @return
     */
    public static <T> Result<T> success() {
        Result<T> result = new Result<T>(SUCCESS_BOOL, SUCCESS_CODE, OPERATE_OK);
        return result;
    }

    /**
     * 请求成功，返回msg是"OPERATE_OK", data为参数具体值
     * @param <T>
     *
     * @param data
     * @return
     */
    public static <T> Result<T> success(T t) {
        return success(QUERY_OK, t);
    }

    /**
     * 请求成功，返回msg,data为参数具体值
     *
     * @param msg
     * @param data
     * @return
     */
    public static <T> Result<T> success(String msg, T t) {
        return new Result<T>(SUCCESS_BOOL, SUCCESS_CODE, msg, t);
    }

    /**
     * 新增成功
     *
     * @param data
     * @return
     */
    public static <T> Result<T> successOfInsert(T t) {
        return success(ADD_OK, t);
    }

    /**
     * 修改成功
     *
     * @param data
     * @return
     */
    public static <T> Result<T> successOfUpdate(T t) {
        return success(UPDATE_OK, t);
    }

    /**
     * 删除成功
     *
     * @param data
     * @return
     */
    public static <T> Result<T> successOfDelete(T t) {
        return success(DELETE_OK, t);
    }

    /**
     * 请求失败
     *
     * @param failureEnum
     * @param msg
     * @param data
     * @return
     */
    public static <T> Result<T> failure(FailureEnum failureEnum, String msg, T t) {
        int code = failureEnum.getCode();
        if (StringUtils.isNoneBlank(msg)) {
            msg = failureEnum.getMsg();
        }
        Result<T> result = new Result<T>(FAILURE_BOOL, code, msg, t);
        return result;
    }

    /**
     * 请求失败
     *
     * @param failureEnum
     * @param msg
     * @return
     */
    public static <T> Result<T> failure(FailureEnum failureEnum, String msg) {
        return failure(failureEnum, msg, null);
    }


    public static <T> Result<T> failure(int code, String msg) {
        return new Result<T>(FAILURE_BOOL, code, msg, null);
    }

    /**
     * 请求失败
     *
     * @param failureEnum
     * @return
     */
    public static <T> Result<T> failure(FailureEnum failureEnum) {
        return failure(failureEnum, null, null);
    }

    /**
     * 请求出错 <br>
     *
     * @param exceptionCodeEnum
     * @return
     */
    public static <T> Result<T> error(ExceptionCodeEnum exceptionCodeEnum) {
        return error(exceptionCodeEnum, null);
    }

    /**
     * 请求出错 <br>
     *
     * @param exceptionCodeEnum
     * @param data
     * @return
     */
    public static <T> Result<T> error(ExceptionCodeEnum exceptionCodeEnum, T t) {
        int code = exceptionCodeEnum.getCode();
        String msg = exceptionCodeEnum.getMsg();
        Result<T> result = new Result<T>(FAILURE_BOOL, code, msg, t);
        return result;
    }

    /**
     * 未知错误<br>
     *
     * @return
     */
    public static <T> Result<T> unknownError() {
        return error(ExceptionCodeEnum.UNKNOWN_ERROR);
    }

    /**
     * 未知错误<br>
     *
     * @param data
     * @return
     */
    public static <T> Result<T> unknownError(T t) {
        return error(ExceptionCodeEnum.UNKNOWN_ERROR, t);
    }


}
