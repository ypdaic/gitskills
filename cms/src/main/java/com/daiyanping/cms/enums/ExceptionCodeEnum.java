package com.daiyanping.cms.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 异常编码枚举
 */
public enum ExceptionCodeEnum {

    /**
     * 5xx 现在有的编码是500-505, 511(要求网络认证)
     */
    PARAMETER_ERROR(50001, "参数错误"),
    NOTEXIST_ERROR(50002, "不存在的信息错误"),
    UNKNOWN_ERROR(50003, "服务异常");

    private int code;
    private String msg;

    ExceptionCodeEnum(int code, String msg) {
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
        for (ExceptionCodeEnum exceptionCodeEnum : ExceptionCodeEnum.values()) {
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
        for (ExceptionCodeEnum exceptionCodeEnum : ExceptionCodeEnum.values()) {
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
        for (ExceptionCodeEnum exceptionCodeEnum : ExceptionCodeEnum.values()) {
            map = new HashMap<String, Object>();
            map.put("code", exceptionCodeEnum.getCode());
            map.put("msg", exceptionCodeEnum.getMsg());
        }
        return list;
    }

}
