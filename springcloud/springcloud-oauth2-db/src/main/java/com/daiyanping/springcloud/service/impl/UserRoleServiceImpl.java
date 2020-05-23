package com.daiyanping.springcloud.service.impl;

import com.daiyanping.springcloud.entity.Role;
import com.daiyanping.springcloud.entity.UserRole;
import com.daiyanping.springcloud.mapper.UserRoleMapper;
import com.daiyanping.springcloud.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daiyanping.springcloud.util.JavaUtils;
import com.daiyanping.springcloud.vo.UserRoleDto;
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
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {


    /**
     * 新增
     * @param userRoleDto
     * @param token
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserRole add(UserRoleDto userRoleDto) {

        UserRole userRole = new UserRole();
        userRole.setUserId(userRoleDto.getUserId());
        userRole.setRoleId(userRoleDto.getRoleId());
        userRole.insert();
        return userRole;
    }

    /**
     * 更新
     * @param userRoleDto
     * @param token
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserRole update(UserRoleDto userRoleDto) {
        UserRole userRole = baseMapper.selectById(userRoleDto.getId());
        if (userRole == null) {
            return null;
        }

        JavaUtils.INSTANCE.acceptIfNotNull(userRoleDto.getUserId(), userRole::setUserId);
        JavaUtils.INSTANCE.acceptIfNotNull(userRoleDto.getRoleId(), userRole::setRoleId);
        userRole.updateById();
        return userRole;
    }

    /**
     * 删除
     * @param userRoleDto
     * @param token
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserRole delete(UserRoleDto userRoleDto) {
        UserRole userRole = baseMapper.selectById(userRoleDto.getId());
        if (userRole == null) {
            return null;
        }
        userRole.updateById();

        return userRole;
    }

    /**
     * 分页查询
     * @param userRoleDto
     * @param userRolePage
     * @return
     */
    @Override
    public IPage<UserRole> queryPage(UserRoleDto userRoleDto, Page<UserRole> userRolePage) {

        return baseMapper.queryPage(userRolePage, userRoleDto);
    }



    /**
     *
     * 查询所有
     * @return
     */
    public List<UserRole> queryAll(UserRoleDto userRoleDto) {
        return baseMapper.queryAll(userRoleDto);
    }

    @Override
    public List<Role> queryAllToRole(UserRoleDto userRoleDto) {
        return baseMapper.queryAllToRole(userRoleDto);
    }

}
