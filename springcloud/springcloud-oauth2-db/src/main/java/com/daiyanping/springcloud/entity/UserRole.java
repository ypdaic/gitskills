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
public class UserRole extends SuperEntity {

    private static final long serialVersionUID = 1L;

    /**
     * user表id
     */
    private Long userId;

    /**
     * role表id
     */
    private Integer roleId;


}
