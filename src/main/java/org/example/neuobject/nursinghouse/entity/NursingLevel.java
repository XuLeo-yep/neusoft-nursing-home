package org.example.neuobject.nursinghouse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>护理级别实体类，用于表示颐养中心老人的护理等级信息。每个护理级别有独立的名称和状态，管理员可动态管理。</p>
 * <p>该类实现了 Serializable 接口，支持序列化传输。</p>
 *
 * @author 李梓轩
 * @date 2026/5/18
 * @version 1.0
 */
@Data
@TableName("nursing_level")
public class NursingLevel implements Serializable {
    /** 护理级别ID */
    @TableId(type = IdType.AUTO)
    private Long levelId;
    /** 护理级别名称 */
    private String name;
    /** 护理级别状态 */
    private String status;
}
