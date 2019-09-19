package com.daiyanping.cms.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 操作日志表
 * </p>
 *
 * @author daiyanping
 * @since 2019-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OperateLog {

    private static final long serialVersionUID = 1L;

    private Long accountId;

    /**
     * 用户名
     */
    private String accountName;

    /**
     * 是否开启 0-不开 1-开启
     */
    private Integer enable;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 操作详情
     */
    private String operateDetail;


}
