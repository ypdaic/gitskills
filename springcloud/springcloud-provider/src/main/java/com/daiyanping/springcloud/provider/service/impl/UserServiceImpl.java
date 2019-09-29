package com.daiyanping.springcloud.provider.service.impl;

import com.daiyanping.springcloud.vo.UserDto;
import com.daiyanping.springcloud.common.DB.DB;
import com.daiyanping.springcloud.common.DB.DBTypeEnum;
import com.daiyanping.springcloud.provider.entity.User;
import com.daiyanping.springcloud.provider.mapper.UserMapper;
import com.daiyanping.springcloud.provider.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

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

    @Autowired
    TransactionTemplate transactionTemplate;

    @Transactional
    public User addUser(UserDto userDto) {
        User user = new User();
        user.setAge(userDto.getAge());
        user.setName(userDto.getName());
        boolean save = save(user);
        transactionTemplate.execute((status) -> {
            User user2 = new User();
            user2.setAge(userDto.getAge());
            user2.setName(userDto.getName());
            user2.insert();
            return user2;
        });
        return save ? user : null;
    }
}
