package com.daiyanping.springcloud.service;

import com.daiyanping.springcloud.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.daiyanping.springcloud.vo.UserDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author daiyanping
 * @since 2020-05-23
 */
public interface IUserService extends IService<User> {

    /**
     * 新增
     * @param userDto
     * @param token
     * @return
     */
    User add(UserDto userDto);

    /**
     * 更新
     * @param userDto
     * @param token
     * @return
     */
    User update(UserDto userDto);

    /**
     * 删除
     * @param userDto
     * @param token
     * @return
     */
    User delete(UserDto userDto);

    /**
     * 分页查询
     * @param userDto
     * @param userPage
     * @return
     */
    IPage<User> queryPage(UserDto userDto, Page<User> userPage);

    /**
     *
     * 查询所有
     * @return
     */
    List<User> queryAll(UserDto userDto);

    User findByUsername(String username);
}
