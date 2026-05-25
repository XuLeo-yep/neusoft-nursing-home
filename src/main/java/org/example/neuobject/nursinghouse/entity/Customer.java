package org.example.neuobject.nursinghouse.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("customer")
public class Customer {

    // ==================== 自身属性 ====================
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private Integer age;
    private String gender;
    private String idNumber;
    private String phone;
    private String address;
    private String loginCode;
    private String checkInDate;
    private String status;           // 在住 / 外出 / 退住
    private String healthInfo;
    private Long nursingLevelId;     // 护理级别ID
    private Long designatedWorkerId; // 指定护工ID
}
