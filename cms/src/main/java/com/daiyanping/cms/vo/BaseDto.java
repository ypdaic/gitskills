package com.daiyanping.cms.vo;

import lombok.Data;

/**
 * @ClassName BaseDto
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-10-12
 * @Version 0.1
 */
@Data
public abstract class BaseDto {

    private int pageIndex;

    private int pageSize;
}
