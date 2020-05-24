package com.daiyanping.springcloud.mapper;

import com.daiyanping.springcloud.entity.Role;
import com.daiyanping.springcloud.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daiyanping.springcloud.vo.UserRoleDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.Map;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author daiyanping
 * @since 2020-05-23
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 分页查询
     * @param userRolePage
     * @param userRoleDto
     * @return
     */
    IPage<UserRole> queryPage(Page<UserRole> userRolePage, @Param("dto") UserRoleDto userRoleDto);

    /**
     * 导出查询数量
     * @param userRoleDto
     * @return
     */
    Integer queryCount(@Param("dto") UserRoleDto userRoleDto);

    /**
     * 导出分页查询
     * @param userRolePage
     * @param userRoleDto
     * @return
     */
    IPage<Map<String, Object>> queryPageForExport(Page<Map<String, Object>> userRolePage, @Param("dto") UserRoleDto userRoleDto);

    /**
     *
     * 查询所有
     * @return
     */
    List<UserRole> queryAll(@Param("dto") UserRoleDto userRoleDto);

    List<Role> queryAllToRole(@Param("dto") UserRoleDto userRoleDto);
}
