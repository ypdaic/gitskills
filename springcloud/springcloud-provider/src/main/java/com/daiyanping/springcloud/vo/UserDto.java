package com.daiyanping.springcloud.vo;

import lombok.Data;

@Data
public class UserDto {

    private Long id;

    private String name;

    private String password;

    private Integer age;
}
