package com.daiyanping.cms.vo;

import com.daiyanping.cms.validator.Create;
import com.daiyanping.cms.validator.Update;
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

    @NotNull(message = "用户名不可为空", groups = {Create.class, Update.class})
    private String name;
}
