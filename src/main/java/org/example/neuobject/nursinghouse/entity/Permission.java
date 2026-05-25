package org.example.neuobject.nursinghouse.entity;


/**
 * <p>Defines all operating permission in the system</p>
 *
 * @author baige
 * @version 1.0
 * @date 2026/5/20 9:17
 */
public enum Permission {
    // ==========================================
    // 用户管理模块
    // ==========================================
    USER_ADD("添加用户"),
    USER_EDIT("修改用户"),
    USER_DELETE("删除用户"),
    USER_QUERY("查询用户"),
    USER_ASSIGN_ROLE("分配角色"),

    // ==========================================
    // 客户管理模块
    // ==========================================
    CUSTOMER_CHECK_IN("入住登记"),
    CUSTOMER_DELETE("删除客户"),
    CUSTOMER_EDIT("修改客户信息"),
    CUSTOMER_QUERY("查询客户"),

    // ==========================================
    // 床位管理模块
    // ==========================================
    BED_VIEW_DIAGRAM("查看床位示意图"),
    BED_TRANSFER("床位调换"),

    // ==========================================
    // 护理管理模块
    // ==========================================
    NURSING_LEVEL_MANAGE("护理级别管理"),
    NURSING_PROJECT_MANAGE("护理项目管理"),
    NURSING_RECORD_QUERY("查询护理记录"),

    // ==========================================
    // 健康管家模块
    // ==========================================
    HEALTH_SET_OBJECT("设置服务对象"),
    HEALTH_FOCUS("服务关注"),
    HEALTH_DAILY_CARE("日常护理"),
    HEALTH_APPLY_OUT("外出申请"),
    HEALTH_APPLY_QUIT("退住申请"),

    // ==========================================
    // 审批管理模块
    // ==========================================
    APPROVAL_OUT("外出审批"),
    APPROVAL_QUIT("退住审批");

    private String description;

    // 构造函数
    Permission(String description) {
        this.description = description;
    }

    //Getter方法
    public String getDescription() {
        return description;
    }
}
