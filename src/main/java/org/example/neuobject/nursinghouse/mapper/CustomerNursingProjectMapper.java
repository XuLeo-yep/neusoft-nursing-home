package org.example.neuobject.nursinghouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.neuobject.nursinghouse.entity.CustomerNursingProject;

import java.util.List;

@Mapper
public interface CustomerNursingProjectMapper extends BaseMapper<CustomerNursingProject> {

    @Select("SELECT * FROM customer_nursing_project WHERE customer_id = #{customerId}")
    List<CustomerNursingProject> selectByCustomerId(Long customerId);
}
