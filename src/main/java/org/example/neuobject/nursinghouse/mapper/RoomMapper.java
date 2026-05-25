package org.example.neuobject.nursinghouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.neuobject.nursinghouse.entity.Room;

@Mapper
public interface RoomMapper extends BaseMapper<Room> {

    @Select("SELECT * FROM room WHERE room_number = #{roomNumber}")
    Room selectByRoomNumber(String roomNumber);
}
