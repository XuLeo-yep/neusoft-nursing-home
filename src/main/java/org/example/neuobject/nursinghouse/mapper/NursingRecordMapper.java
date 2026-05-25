package org.example.neuobject.nursinghouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.neuobject.nursinghouse.entity.NursingRecord;

import java.util.List;

@Mapper
public interface NursingRecordMapper extends BaseMapper<NursingRecord> {

    @Select("SELECT * FROM nursing_record WHERE customer_id = #{customerId} AND is_deleted = 0")
    List<NursingRecord> selectByCustomerId(Long customerId);
}
