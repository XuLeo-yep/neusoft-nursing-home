package org.example.neuobject.nursinghouse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("nursing_record")
public class NursingRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long projectId;
    private Long customerId;
    private Date nursingDate;
    private Integer nursingQuanity;
    private boolean isDeleted;
}

