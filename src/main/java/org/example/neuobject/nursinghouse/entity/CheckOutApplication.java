package org.example.neuobject.nursinghouse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * CheckOutApplication（退住申请）
 */
@Data
@TableName("check_out_application")
public class CheckOutApplication {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String applicationId;  // 业务ID 如 "CO-xxx"
    private Date applyTime;
    private String reason;
    private Date plannedCheckOutDate;
    private Date actualCheckOutDate;
    private Double refundAmount;
    private String approvalStatus; // 待审批 / 已批准 / 已拒绝
    private String approver;
    private Date approvalTime;
    private Long customerId;
}