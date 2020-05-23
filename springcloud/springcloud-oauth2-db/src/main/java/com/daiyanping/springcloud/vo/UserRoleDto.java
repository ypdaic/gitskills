package com.daiyanping.springcloud.vo;

import com.daiyanping.springcloud.util.BaseDTO;
import lombok.Data;

/**
 *
 *  DTO对象
 *
 *
 * @author daiyanping
 * @since 2020-05-23
 */
@Data
public class UserRoleDto extends BaseDTO {

	/**
	 * id
	 */
	private Long id;

	/**
	 * user表id
	 */
	private Long userId;

	/**
	 * role表id
	 */
	private Integer roleId;

	/**
     * 是否新增
     */
	private Boolean isAdd;

}
