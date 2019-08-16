package com.daiyanping.cms.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName UserDto
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-08-16
 * @Version 0.1
 */
@Data
public class UserDto {

    @NotNull
    private String name;
}
