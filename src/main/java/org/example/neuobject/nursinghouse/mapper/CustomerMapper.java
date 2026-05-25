package org.example.neuobject.nursinghouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.neuobject.nursinghouse.entity.Customer;

@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
}
