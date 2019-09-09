package mapper;

import entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface UserMapper {

    User getAll(@Param("id") Integer id);

    @Update("update user set name = #{user.name} where id = #{user.id}")
    User update(@Param("user") User user);

    @Insert("insert user (id,name,age,email) values (#{user.id}, #{user.name}, #{user.age}, #{user.email})")
    User insert(@Param("user") User user);
}
