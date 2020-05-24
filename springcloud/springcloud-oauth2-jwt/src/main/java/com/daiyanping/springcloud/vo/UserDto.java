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
public class UserDto extends BaseDTO {

	/**
	 * id
	 */
	private Long id;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;

	/**
     * 是否新增
     */
	private Boolean isAdd;

}
