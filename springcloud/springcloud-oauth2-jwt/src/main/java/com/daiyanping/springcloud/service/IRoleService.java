package com.daiyanping.springcloud.service;

import com.daiyanping.springcloud.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.daiyanping.springcloud.vo.RoleDto;
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
public interface IRoleService extends IService<Role> {

    /**
     * 新增
     * @param roleDto
     * @param token
     * @return
     */
    Role add(RoleDto roleDto);

    /**
     * 更新
     * @param roleDto
     * @param token
     * @return
     */
    Role update(RoleDto roleDto);

    /**
     * 删除
     * @param roleDto
     * @param token
     * @return
     */
    Role delete(RoleDto roleDto);

    /**
     * 分页查询
     * @param roleDto
     * @param rolePage
     * @return
     */
    IPage<Role> queryPage(RoleDto roleDto, Page<Role> rolePage);

    /**
     *
     * 查询所有
     * @return
     */
    List<Role> queryAll(RoleDto roleDto);
}
