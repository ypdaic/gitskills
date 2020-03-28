package com.daiyanping.cms.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 异常编码枚举
 */
public enum FailureEnum {
    /**
     * 4xx
     */
    PARAMETER_FAILURE(40001, "参数错误"),
    TOKEN_WITHOUT_FAILURE(40002, "缺少Token"),
    TOKEN_EXPIRED_FAILURE(40003, "Token过期"),
    NO_PERMISSION_FAILURE(40004, "当前操作没有权限"),
    VERIFY_LOGIN_FAILURE(40005, "验证码错误"),
    VERIFY_EXPIRE_FAILURE(40006, "验证码过期"),
    INVALID_LOGIN_NAME(40007, "该账号不存在，请联系管理员分配账号"),
    PASSWORD_FAILURE(40008, "密码错误"),
    PASSWORD_CONFIRM_FAILURE(40009, "密码和确认密码不一致"),
    FAIL_LOGIN(40010, "登录失败"),
    USER_LOGINING(40011, "用户正在登录中"),
    NOTEXIST_FAILURE(40012, "记录不存在"),
    OPERATION_FAILURE(40013, "操作失败, 请重试"),
    NO_DATA_ACCESS(40014, "该账号未分配权限"),
    INVALID_ACCOUNT(40015, "账号已被禁用"),
    LOGIN_OTHER_PLACE(40016, "账号已在其它地方登录"),

    DATA_NO_PERMISSION(40021, "数据权限异常"),

    NOT_FOUND(40025, "资源不存在"),
    SERVER_DOWNGRADE(40026, "服务降级"),
    TOKEN_PARSE_FAILURE(40028, "token解析异常"),
    TOKEN_PARSE_EXCEPTION(40029, "token解析异常"),

    API_AUTH_FAILURE(40035, "接口授权验证异常"),
    API_AUTH_ERROR(40036, "接口签名验证错误"),
    API_TIME_ERROR(40037, "接口请求时间验证错误"),

    SIGN_ERROR(40046, "签名失效！"),


    API_REPEAT_ERROR(41080, "签名重复请求"),



    EXCEPTION_FAILURE(50001, "服务器异常");


    private int code;
    private String msg;

    FailureEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    /**
     * 通过code 获得msg
     *
     * @param code
     * @return
     */
    public static String getMsgByCode(int code) {
        for (FailureEnum exceptionCodeEnum : FailureEnum.values()) {
            if (exceptionCodeEnum.getCode() == code) {
                return exceptionCodeEnum.getMsg();
            }
        }
        throw new IllegalArgumentException("No element matches " + code);
    }

    /**
     * 获得枚举的列表, 格式是{1, "异常1"}
     *
     * @return
     */
    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (FailureEnum exceptionCodeEnum : FailureEnum.values()) {
            map.put(exceptionCodeEnum.getCode(), exceptionCodeEnum.getMsg());
        }
        return map;
    }

    /**
     * 获得枚举的列表, 格式是[{"code": 1, "msg": "异常1"}]
     *
     * @return
     */
    public static List<Map<String, Object>> toList() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        for (FailureEnum exceptionCodeEnum : FailureEnum.values()) {
            map = new HashMap<String, Object>();
            map.put("code", exceptionCodeEnum.getCode());
            map.put("msg", exceptionCodeEnum.getMsg());
        }
        return list;
    }

}
