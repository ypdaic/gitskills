package com.daiyanping.springcloud.enums;

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
    ACCOUNT_ROLE_EXISTS(40017, "角色与账号存在关联"),
    MENU_ROLE_EXISTS(40018, "菜单与角色存在关联"),
    ACCOUNT_GROUPS_EXISTS(40019, "技能组与账号存在关联"),
    CATEGORY_NOTEXIST_FAILURE(40020, "回复类别不存在"),
    DATA_NO_PERMISSION(40021, "数据权限异常"),
    DEPARTMENT_NAME_EXISTS_FAILURE(40022, "部门名称已存在"),
    PASSWORD_NOT_EMPTY_FAILURE(40023, "密码不能为空"),
    PASSWORD_FORMAT_FAILURE(40024, "密码必须为8-16位英文、数字组合"),
    NOT_FOUND(40025, "资源不存在"),
    SERVER_DOWNGRADE(40026, "服务降级"),
    TOKEN_PARSE_FAILURE(40028, "token解析异常"),
    TOKEN_PARSE_EXCEPTION(40029, "token解析异常"),
    USER_NAME_FAILURE(40030, "用户名必须为5-16位英文、数字组合"),
    ACCOUNT_USER_NAME_EXISTS_FAILURE(40031, "账号已存在"),
    DEPARTMENT_ACCOUNT_EXISTS(40032, "部门与账号存在关联"),
    ROLE_NAME_EXISTS_FAILURE(40033, "角色名已存在"),
    MENU_HAS_CHILD_ERROR(40034, "当前菜单存在子菜单"),
    API_AUTH_FAILURE(40035, "接口授权验证异常"),
    API_AUTH_ERROR(40036, "接口签名验证错误"),
    API_TIME_ERROR(40037, "接口请求时间验证错误"),
    EXPORT_ERROR(40038, "导出异常"),
    SINGLE_DOCUMENT_ADD_ERROR(40039, "页面内容，文件至少填一项"),
    CHILD_COLUMN_EXISTS_ERROR(40040, "栏目子级已存在！"),
    SINGLE_DOCUMENT_OWN_COLUMN_ERROR(40041, "所属栏目已存在!"),
    PROJECT_NAME_EXISTS_ERROR(40042, "项目名称已存在！"),
    PRODUCT_NAME_EXISTS_ERROR(40043, "商品名称已存在！"),
    SIGN_ERROR(40046, "签名失效！"),


    // cms 41XXX
//    DEPARTMENT_ACCOUNT_EXISTS(40025, "部门与账号存在关联"),
    EXCEL_FILE(41025, "上传的文件必须为xls或者xlsx类型"),
    XLS_FILE(41026, "上传的文件必须为xls类型"),
    XLSX_FILE(41027, "上传的文件必须为xlsx类型"),
    FILE_NOT_NULL_FAILURE(41028, "上传的文件不能为空"),
    FILE_MAX_SIZE_FAILURE(41029, "文件最大20M"),
    IMPORT_ERROR(41035, "导入异常"),
    REPEAT_EVALUATE(41036, "不能重复评价"),
//    EXPORT_ERROR(40037, "导出异常"),
//    ACCOUNT_USER_NAME_EXISTS_FAILURE(40053, "账号已存在"),
//    ROLE_NAME_EXISTS_FAILURE(40054, "角色名已存在"),
    EXCEL_MAX_ROW(41068, "最多30万行"),
    INPUT_FAILURE(41074, "输入错误"),
    SATISFACTION_ENABLE_FAILURE(41075, "满意度配置至少开启两项"),
    VISITOR_PHONE_EXISTS_FAILURE(41076, "电话号码存在"),
//    API_AUTH_FAILURE(40077, "接口授权验证异常"),
//    API_AUTH_ERROR(40078, "接口签名验证错误"),
//    API_TIME_ERROR(40079, "接口请求时间验证错误"),
    API_REPEAT_ERROR(41080, "签名重复请求"),
    WORK_ORDER_NO_PERMISSION_ERROR(41081, "您没有权限操作该工单！"),
    SYS_CONFIG_SESSION_TIMEOUT_ERROR(41082, "会话超时时间应大于长时间未回复标注"),
//    SINGLE_DOCUMENT_ADD_ERROR(40083, "页面内容，文件至少填一项"),
//    USER_NAME_FAILURE(40084, "用户名必须为5-16位英文、数字组合"),
//    MENU_HAS_CHILD_ERROR(40085, "当前菜单存在子菜单"),
    MULTI_DOCUMENT_DELETE_ERROR(41086, "该多文下存在文章，请先删除文章，再删除该多文！"),
//    CHILD_COLUMN_EXISTS_ERROR(40087, "栏目子级已存在！"),
    CHECK_PATIENT_EXISTS_ERROR(41088, "提示：您已通过其他渠道申请该援助项目，如有疑问请联系专属顾问"),
//    PROJECT_NAME_EXISTS_ERROR(40089, "项目名称已存在！"),
//    PRODUCT_NAME_EXISTS_ERROR(40090, "商品名称已存在！"),
    REPEATED_SUBMIT_ERROR(41091, "重复提交!"),
//    SINGLE_DOCUMENT_OWN_COLUMN_ERROR(40092, "所属栏目已存在!"),
    CONTACT_NUMBER_ERROR(41093, "联系电话格式错误!"),
    CONDITION_DESC_ERROR(41094, "病情说明过长!"),
    CONTACT_NUMBER_LENGTH_ERROR(41095, "联系电话过长!"),
    USER_NOT_EXIST(41096, "该用户不存在！"),
    PHONE_HAS_REGISTERED(41097, "该号码已经被注册！"),
    PASSWORD_CONFIRMATION_PASSWORD_ERROR(41098, "密码与确认密码不相同！"),
    SEND_SMS_COUNT_ERROR(41099, "最多重发10次，若没有收到请耐心等待！"),
    SEND_SMS_RESUBMIT_ERROR(41100, "60秒之内不能重发，请勿频繁点击!"),
    MED_ASSIST_PROJECT_NOT_EXITS(41101, "未找到项目信息"),
    ID_CARD_NO_NAME_FAILED(41103, "身份证识别失败，未能识别到姓名。"),
    ID_CARD_NO_IDSN_FAILED(41104, "身份证识别失败，未能识别到证件号码。"),
    MED_ASSIST_PROJECT_OR_PATIENT_NOT_EXITS(41105, "未找到患者信息或者项目信息"),
    FILE_NOT_EXITS(41106, "未检测到文件，上传失败。"),
    IMAGE_WRONG_FORMAT(41107, "图片格式不正确，上传失败。"),
    OPEN_ID_ERROR(41108, "openid获取错误！"),
    PROJECT_AID_USER_HISTORY_EXIST(41109, "该用户不存在搜索历史"),
    VOLUNTEER_AUTH_FAIL(41110, "志愿者认证未通过或待审核"),
    VOLUNTEER_LOGIN_MOBILE_EXIST(41111, "存在多个账号"),
    VOLUNTEER_REFUSE_AGAIN_APPLY(41112, "请勿重复提交申请"),
    FALLBACK_ERROR(50002, "网络异常"),


    // manager 42XXX
    HOSPITAL_EXCEL_NAME_EMPTY(42026, "第%s行医院名称不能为空"),
    HOSPITAL_EXCEL_REGION_EMPTY(42026, "第%s行区域信息不能为空"),
    HOSPITAL_EXCEL_REGION_ERROR(42027, "第%s行地区数据错误，但数据已入库"),
    HOSPITAL_EXCEL_ADRESS_EXISTS(42028, "第%s行详细地址不能为空"),
    HOSPITAL_EXCEL_EXISTS(42029, "第%s行医院已存在"),

    COMMISSIONER_EXCEL_NAME_EMPTY(42026, "第%s行公益专员不能为空"),
    COMMISSIONER_EXCEL_PHONE_EMPTY(42026, "第%s行手机号不能为空"),
    COMMISSIONER_EXCEL_PHONE_ERROR(42026, "第%s行手机号格式不对"),
    COMMISSIONER_EXCEL_ENABLE_EMPTY(42026, "第%s行是否在岗不能为空"),
    COMMISSIONER_EXCEL_ENABLE_ERROR(42026, "第%s行是否在岗文案匹配错误"),
    COMMISSIONER_EXCEL_ROLE_ERROR(42026, "第%s行角色文案匹配错误"),
    COMMISSIONER_EXCEL_NAME_EXIST(42026, "第%s行公益专员已存在"),

    FUNDATION_EXCEL_NAME_EMPTY(42026, "第%s行基金会名称不能为空"),
    FUNDATION_EXCEL_BRIEF_EMPTY(42026, "第%s行基金会简介不能为空"),
    FUNDATION_EXCEL_PHONE_ERROR(42026, "第%s行基金会电话格式不对"),
    FUNDATION_EXCEL_NAME_EXIST(42026, "第%s行基金会名称已存在"),

    DONATE_COMPANY_EXCEL_NAME_EMPTY(42026, "第%s行捐赠企业名称不能为空"),
    DONATE_COMPANY_EXCEL_PHONE_EMPTY(42026, "第%s行捐赠介绍不能为空"),
    DONATE_COMPANY_EXCEL_ENABLE_EMPTY(42026, "第%s行是否启用不能为空"),
    DONATE_COMPANY_EXCEL_NAME_EXIST(42026, "第%s行捐赠企业已存在"),

    DRUG_EXCEL_NAME_EMPTY(42026, "第%s行药品名称不能为空"),
    DRUG_EXCEL_SPECIFICATIO_EMPTY(42026, "第%s行药品规格不能为空"),
    DRUG_EXCEL_EXIST(42026, "第%s行药品已存在"),

    DISEASE_EXCEL_NAME_EMPTY(42026, "第%s行疾病名称不能为空"),

    DRUGSTORE_EXCEL_NAME_EMPTY(42026, "第%s行药房名称不能为空"),
    DRUGSTORE_EXCEL_REGION_EMPTY(42026, "第%s行地区信息不能为空"),
    DRUGSTORE_EXCEL_REGION_ERROR(42026, "第%s行省市数据错误"),
    DRUGSTORE_EXCEL_EXIST(42026, "第%s行药房已存在"),
    DRUGSTORE_EXCEL_ADRESS_EMPTY(42026, "第%s行药房地址不能为空"),
    DRUGSTORE_EXCEL_RECEIVE_ADRESS_EMPTY(42026, "第%s行收获地址不能为空"),

    HEALTH_WORKER_EXCEL_NAME_EMPTY(42026, "第%s行医生姓名不能为空"),
    HEALTH_WORKER_EXCEL_SEX_EMPTY(42026, "第%s行性别不能为空"),
    HEALTH_WORKER_EXCEL_POST_EMPTY(42026, "第%s行医护角色不能为空"),
    HEALTH_WORKER_EXCEL_IDCARD_EMPTY(42026, "第%s行身份证号码不能为空"),
    HEALTH_WORKER_EXCEL_IDCARD_ERROR(42026, "第%s行身份证号码错误"),
    HEALTH_WORKER_EXCEL_PHONE_ERROR(42026, "第%s行手机号码错误"),
    HEALTH_WORKER_EXCEL_EXIST(42026, "第%s行医护工作者已存在"),

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
