package org.example.neuobject.nursinghouse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Bed（床位）
 */
@Data
@TableName("bed")
public class Bed {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String bedId;       // 业务ID 如 "BD-xxx"
    private String bedNumber;   // 床位号 如 "A"
    private String status;      // 空闲 / 已入住 / 维修中
    private String bedType;     // 普通床 / 护理床
    private Long roomId;        // 所属房间ID
}
