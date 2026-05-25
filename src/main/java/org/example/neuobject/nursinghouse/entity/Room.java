package org.example.neuobject.nursinghouse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Room（房间）
 */
@Data
@TableName("room")
public class Room {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String roomId;      // 业务ID 如 "RM-xxx"
    private String roomNumber;  // 房间号 如 "101"
    private String roomType;    // 单人间 / 双人间
    private Integer floor;      // 楼层
    private String status;      // 可用 / 已满 / 维修中
}