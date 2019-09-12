package com.daiyanping.springcloud.provider.entity;

import com.daiyanping.springcloud.common.base.SuperEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author daiyanping
 * @since 2019-08-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User extends SuperEntity{

    private static final long serialVersionUID=1L;

    private String name;

    private String password;

    private Integer age;

}
