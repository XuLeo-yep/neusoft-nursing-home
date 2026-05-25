package org.example.neuobject.nursinghouse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>护理项目实体类，是颐养中心提供给老人的可购买、可执行的最小护理服务单元。每个护理项目有独立的定价、执行周期和执行次数，管理员可动态启用/停用。</p>
 * <p>该类实现了 Serializable 接口，支持序列化传输。</p>
 *
 * @author 李梓轩
 * @date 2026/5/18 18:45
 * @version 1.0
 */
@Data
@TableName("nursing_project")
public class NursingProject implements Serializable {
    /** 护理项目ID */
    @TableId(type = IdType.AUTO)
    private Long projectId;
    
    /** 项目编码 */
    private String code;
    
    /** 项目名称 */
    private String name;
    
    /** 项目价格 */
    private BigDecimal price;
    
    /** 项目状态 */
    private String status;
    
    /** 执行周期 */
    private String execCycle;
    
    /** 执行次数 */
    private Integer execTimes;
    
    /** 项目描述 */
    private String description;
}
