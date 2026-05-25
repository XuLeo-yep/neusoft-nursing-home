package org.example.neuobject.nursinghouse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("outing_application")
public class OutingApplication {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String applicationId;   // 业务ID 如 "OUT-xxx"
    private String reason;
    private Date outingTime;
    private Date plannedReturnTime;
    private Date actualReturnTime;
    private String destination;
    private String companion;
    private String approvalStatus;  // 待审批 / 已批准 / 已拒绝
    private String approver;
    private Date approvalTime;
    private Long customerId;
}