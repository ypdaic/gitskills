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
public class RoleDto extends BaseDTO {

	/**
	 * id
	 */
	private Long id;

	/**
	 * 角色名
	 */
	private String name;

	/**
     * 是否新增
     */
	private Boolean isAdd;

}
