package com.daiyanping.springcloud.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daiyanping.springcloud.common.DB.DB;
import com.daiyanping.springcloud.common.DB.DBTypeEnum;
import com.daiyanping.springcloud.provider.entity.User;
import com.daiyanping.springcloud.provider.mapper.UserMapper;
import com.daiyanping.springcloud.provider.service.IUserService;
import com.daiyanping.springcloud.vo.UserDto;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author daiyanping
 * @since 2019-08-03
 */
@Service
@DB(DBTypeEnum.DB1)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    public User addUser(UserDto userDto) {
        User user = new User();
        user.setAge(userDto.getAge());
        user.setName(userDto.getName());
        boolean save = save(user);
        return save ? user : null;
    }
}
