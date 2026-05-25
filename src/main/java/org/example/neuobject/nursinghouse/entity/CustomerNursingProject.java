package org.example.neuobject.nursinghouse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

@Data
@TableName("customer_nursing_project")
public class CustomerNursingProject implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Date purchaseDate;
    private Integer quanity;
    private Date expireDate;
    private Integer remainingQuanity;
    private Long projectId;
    private Long customerId;
}
