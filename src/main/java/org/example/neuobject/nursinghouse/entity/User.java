package org.example.neuobject.nursinghouse.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>定义admin和healthWorker 的抽象基类</p>
 *
 * @author baige
 * @version 1.0
 * @date 2026/5/20 9:42
 */
@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String phone;
    private String loginCode;
    private String role;             // "admin" 或 "health_worker"
    private String servedCustomerIds; // 仅 health_worker 有值，存 JSON
}
