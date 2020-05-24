package com.daiyanping.springcloud.service.impl;

import com.daiyanping.springcloud.entity.Role;
import com.daiyanping.springcloud.mapper.RoleMapper;
import com.daiyanping.springcloud.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daiyanping.springcloud.util.JavaUtils;
import com.daiyanping.springcloud.vo.RoleDto;
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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {


    /**
     * 新增
     * @param roleDto
     * @param token
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Role add(RoleDto roleDto) {

        Role role = new Role();
        role.setName(roleDto.getName());
        role.insert();
        return role;
    }

    /**
     * 更新
     * @param roleDto
     * @param token
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Role update(RoleDto roleDto) {
        Role role = baseMapper.selectById(roleDto.getId());
        if (role == null) {
            return null;
        }

        JavaUtils.INSTANCE.acceptIfNotNull(roleDto.getName(), role::setName);
        role.updateById();
        return role;
    }

    /**
     * 删除
     * @param roleDto
     * @param token
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Role delete(RoleDto roleDto) {
        Role role = baseMapper.selectById(roleDto.getId());
        if (role == null) {
            return null;
        }
        role.updateById();

        return role;
    }

    /**
     * 分页查询
     * @param roleDto
     * @param rolePage
     * @return
     */
    @Override
    public IPage<Role> queryPage(RoleDto roleDto, Page<Role> rolePage) {

        return baseMapper.queryPage(rolePage, roleDto);
    }

    /**
     *
     * 查询所有
     * @return
     */
    public List<Role> queryAll(RoleDto roleDto) {
        return baseMapper.queryAll(roleDto);
    }

}
