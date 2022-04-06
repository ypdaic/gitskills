package com.daiyanping.cms.scala;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class CdpTagMetadataVO {

    private JSONArray data;

    /**
     * 生效开始时间
     */
    private String startDate;
    /**
     * 生效结束时间
     */
    private String endDate;

    private String tagName;

    private String tagNameEn;

    private String catalogId;
    /**
     * 标签类型 1-事实标签 2-规则标签 3-预测标签 4-其他
     */
    private Integer tagType;
    /**
     *计算方式 1-静态标签 2-触发标签 3-批量标签 4-自定义查询
     */
    private Integer calcMethod;
    /**
     * 更新频率 1-天  2-周 3-月
     */
    private Integer updateFrequency;

    /**
     * 标签状态  1-正常 2-已暂停
     */
    private Integer tagStatus;
    /**
     * 标签值类型 1-单值标签 2-多值标签
     */
    private Integer tagValueType;
    /**
     * 创建方式
     * 1-基于文件创建
     * 2-分析模型创建
     * 3-实时触发式标签
     * 4-匹配lookup文件实时触发标签
     * 5-基于属性值创建
     * 6-匹配lookup文件打标签
     * 7-基于复杂条件创建
     * 8-基于聚合指标区间创建
     * 9-基于SQL查询语句创建
     */
    private Integer createMethod;

    private Integer tagWeight;

    private String source;

    private String tagStatisticalClassification;

    private String tagCalcClassification;

    private String tagApplicationClassification;
    /**
     * 系统预制 1-用户自定义 0-系统预制
     */
    private Integer isPrecut;

    private String tagValue;

    private String originalTagValue;

    private Integer columnSequence;

    private Integer syncState;

    private String brandId;

    private String tenantId;

    @JsonSerialize(
            using = ToStringSerializer.class
    )
    private String id;

    @JsonSerialize(
            using = ToStringSerializer.class
    )
    private String createBy;

    @JsonSerialize(
            using = ToStringSerializer.class
    )
    private String createDept;

    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date createAt;

    @JsonSerialize(
            using = ToStringSerializer.class
    )
    private String updateBy;

    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date updateAt;

    private String status;

    private Integer isDeleted;

}