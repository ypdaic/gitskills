package com.daiyanping.springcloud.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daiyanping.springcloud.common.DB.DB;
import com.daiyanping.springcloud.common.DB.DBTypeEnum;
import com.daiyanping.springcloud.common.annotation.PrintRequestTime;
import com.daiyanping.springcloud.provider.entity.User;
import com.daiyanping.springcloud.provider.mapper.UserMapper;
import com.daiyanping.springcloud.provider.service.IUserService;
import com.daiyanping.springcloud.vo.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
@PrintRequestTime(printError = 0l)
@CacheConfig(cacheNames = "APP_CACHE")
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

    @Cacheable(sync = true, key = "'USER:' + #userDto.id")
    @Override
    public User getUser(UserDto userDto) {
        return baseMapper.selectById(userDto.getId());
    }

    @CacheEvict(key = "'USER:' + #userDto.id")
    public void deleteUser(UserDto userDto) {
//        baseMapper.deleteById(userDto.getId());
    }
}
