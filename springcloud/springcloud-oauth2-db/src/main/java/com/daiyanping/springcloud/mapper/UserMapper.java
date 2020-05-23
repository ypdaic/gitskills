package com.daiyanping.springcloud.mapper;

import com.daiyanping.springcloud.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daiyanping.springcloud.vo.UserDto;
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
public interface UserMapper extends BaseMapper<User> {

    /**
     * 分页查询
     * @param userPage
     * @param userDto
     * @return
     */
    IPage<User> queryPage(Page<User> userPage, @Param("dto") UserDto userDto);

    /**
     * 导出查询数量
     * @param userDto
     * @return
     */
    Integer queryCount(@Param("dto") UserDto userDto);

    /**
     * 导出分页查询
     * @param userPage
     * @param userDto
     * @return
     */
    IPage<Map<String, Object>> queryPageForExport(Page<Map<String, Object>> userPage, @Param("dto") UserDto userDto);

    /**
     *
     * 查询所有
     * @return
     */
    List<User> queryAll(@Param("dto") UserDto userDto);

}
