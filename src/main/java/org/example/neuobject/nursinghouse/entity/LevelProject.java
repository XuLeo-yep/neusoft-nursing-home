package org.example.neuobject.nursinghouse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>护理级别-项目关联实体类，用于表示护理级别与护理项目之间的多对多关联关系。</p>
 * <p>该类实现了 Serializable 接口，支持序列化传输。</p>
 *
 * @author 李梓轩
 * @date 2026/5/18
 * @version 1.0
 */
@Data
@TableName("level_project")
public class LevelProject implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long levelId;
    private Long projectId;
}