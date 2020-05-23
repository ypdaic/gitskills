package com.daiyanping.springcloud.service;

import com.daiyanping.springcloud.entity.Role;
import com.daiyanping.springcloud.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.daiyanping.springcloud.vo.UserRoleDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
public interface IUserRoleService extends IService<UserRole> {

    /**
     * 新增
     * @param userRoleDto
     * @param token
     * @return
     */
    UserRole add(UserRoleDto userRoleDto);

    /**
     * 更新
     * @param userRoleDto
     * @param token
     * @return
     */
    UserRole update(UserRoleDto userRoleDto);

    /**
     * 删除
     * @param userRoleDto
     * @param token
     * @return
     */
    UserRole delete(UserRoleDto userRoleDto);

    /**
     * 分页查询
     * @param userRoleDto
     * @param userRolePage
     * @return
     */
    IPage<UserRole> queryPage(UserRoleDto userRoleDto, Page<UserRole> userRolePage);

    /**
     *
     * 查询所有
     * @return
     */
    List<UserRole> queryAll(UserRoleDto userRoleDto);

    List<Role> queryAllToRole(UserRoleDto userRoleDto);
}
