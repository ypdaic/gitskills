package com.daiyanping.springcloud.provider.service;

import com.daiyanping.springcloud.vo.UserDto;
import com.daiyanping.springcloud.provider.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author daiyanping
 * @since 2019-08-03
 */
public interface IUserService extends IService<User> {

    User addUser(UserDto userDto);
}
