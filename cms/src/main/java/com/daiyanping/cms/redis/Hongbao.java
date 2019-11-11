package com.daiyanping.cms.redis;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName Hongbao
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-11-11
 * @Version 0.1
 */
@Data
public class Hongbao {

    private Integer id;

    private Integer userID;

    private BigDecimal money;
}
