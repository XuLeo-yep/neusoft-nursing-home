package org.example.neuobject.nursinghouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.neuobject.nursinghouse.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM user WHERE login_code = #{loginCode}")
    User selectByLoginCode(String loginCode);
}
