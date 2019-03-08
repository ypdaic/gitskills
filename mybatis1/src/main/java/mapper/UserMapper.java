package mapper;

import entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    User getAll(@Param("id") Integer id);
}
