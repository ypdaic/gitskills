package com.daiyanping.springcloud.mapper;

import com.daiyanping.springcloud.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daiyanping.springcloud.vo.RoleDto;
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
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 分页查询
     * @param rolePage
     * @param roleDto
     * @return
     */
    IPage<Role> queryPage(Page<Role> rolePage, @Param("dto") RoleDto roleDto);

    /**
     * 导出查询数量
     * @param roleDto
     * @return
     */
    Integer queryCount(@Param("dto") RoleDto roleDto);

    /**
     * 导出分页查询
     * @param rolePage
     * @param roleDto
     * @return
     */
    IPage<Map<String, Object>> queryPageForExport(Page<Map<String, Object>> rolePage, @Param("dto") RoleDto roleDto);

    /**
     *
     * 查询所有
     * @return
     */
    List<Role> queryAll(@Param("dto") RoleDto roleDto);

}
