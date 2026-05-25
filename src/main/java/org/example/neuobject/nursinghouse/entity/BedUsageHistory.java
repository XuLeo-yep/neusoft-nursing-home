package org.example.neuobject.nursinghouse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.example.neuobject.nursinghouse.entity.Customer;

import java.util.Date;

/**
 * BedUsageHistory（床位使用历史）
 * 记录某位老人在某个床位的入住历史
 */
@Data
@TableName("bed_usage_history")
public class BedUsageHistory {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String historyId;    // 业务ID 如 "BU-xxx"
    private Date startDate;
    private Date endDate;
    private Double monthlyFee;
    private String status;       // 在住 / 已退住
    private Long customerId;
    private String bedId;        // 床位业务ID
}

