package com.daiyanping.springcloud.entity;

import com.daiyanping.springcloud.util.SuperEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *
 * 
 *
 *
 * @author daiyanping
 * @since 2020-05-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Role extends SuperEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 角色名
     */
    private String name;


}
