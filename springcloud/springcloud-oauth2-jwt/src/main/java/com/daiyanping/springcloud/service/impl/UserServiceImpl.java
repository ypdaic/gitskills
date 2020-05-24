package com.daiyanping.springcloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daiyanping.springcloud.entity.User;
import com.daiyanping.springcloud.mapper.UserMapper;
import com.daiyanping.springcloud.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daiyanping.springcloud.util.JavaUtils;
import com.daiyanping.springcloud.vo.UserDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author daiyanping
 * @since 2020-05-23
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {


    /**
     * 新增
     * @param userDto
     * @param token
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public User add(UserDto userDto) {

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.insert();
        return user;
    }

    /**
     * 更新
     * @param userDto
     * @param token
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public User update(UserDto userDto) {
        User user = baseMapper.selectById(userDto.getId());
        if (user == null) {
            return null;
        }

        JavaUtils.INSTANCE.acceptIfNotNull(userDto.getUsername(), user::setUsername);
        JavaUtils.INSTANCE.acceptIfNotNull(userDto.getPassword(), user::setPassword);
        user.updateById();
        return user;
    }

    /**
     * 删除
     * @param userDto
     * @param token
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public User delete(UserDto userDto) {
        User user = baseMapper.selectById(userDto.getId());
        if (user == null) {
            return null;
        }
        user.updateById();

        return user;
    }

    /**
     * 分页查询
     * @param userDto
     * @param userPage
     * @return
     */
    @Override
    public IPage<User> queryPage(UserDto userDto, Page<User> userPage) {

        return baseMapper.queryPage(userPage, userDto);
    }

    /**
     *
     * 查询所有
     * @return
     */
    public List<User> queryAll(UserDto userDto) {
        return baseMapper.queryAll(userDto);
    }

    @Override
    public User findByUsername(String username) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username);
        User user = baseMapper.selectOne(userQueryWrapper);

        return user;
    }

}
