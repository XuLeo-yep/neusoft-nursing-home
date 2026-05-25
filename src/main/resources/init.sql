-- ============================================
-- 东软颐养中心管理系统 - 数据库初始化脚本
-- 使用方法：在 MySQL 中执行此文件
-- mysql -u root -p 0520-demo < init.sql
-- ============================================

CREATE DATABASE IF NOT EXISTS `0520-demo`
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE `0520-demo`;

-- ============================================
-- 1. 用户表（管理员 + 健康管家）
-- 对应实体：User / Admin / HealthWorker
-- ============================================
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id`           BIGINT       NOT NULL AUTO_INCREMENT  COMMENT '用户ID',
  `username`     VARCHAR(50)  NOT NULL                 COMMENT '用户名',
  `password`     VARCHAR(100) NOT NULL                 COMMENT '密码',
  `phone`        VARCHAR(20)  DEFAULT NULL             COMMENT '手机号',
  `login_code`   VARCHAR(50)  NOT NULL                 COMMENT '登录编码（账号）',
  `role`         VARCHAR(20)  NOT NULL DEFAULT 'admin' COMMENT '角色：admin / health_worker',
  `served_customer_ids` TEXT  DEFAULT NULL             COMMENT '健康管家服务的客户ID列表（JSON数组）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_login_code` (`login_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表（管理员和健康管家）';

-- 预设管理员账号
INSERT INTO `user` (`username`, `password`, `login_code`, `role`) VALUES
('管理员1', 'admin1', 'admin1', 'admin'),
('管理员2', 'admin2', 'admin2', 'admin'),
('管理员3', 'admin3', 'admin3', 'admin'),
('健康管家1', '123456', 'health1', 'health_worker');

-- ============================================
-- 2. 客户表（老人）
-- 对应实体：Customer
-- ============================================
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `id`              BIGINT       NOT NULL AUTO_INCREMENT  COMMENT '客户ID',
  `username`        VARCHAR(50)  NOT NULL                 COMMENT '客户姓名',
  `password`        VARCHAR(100) DEFAULT '123456'         COMMENT '登录密码（默认123456）',
  `age`             INT          DEFAULT NULL             COMMENT '年龄',
  `gender`          VARCHAR(10)  DEFAULT NULL             COMMENT '性别',
  `id_number`       VARCHAR(20)  DEFAULT NULL             COMMENT '身份证号',
  `phone`           VARCHAR(20)  DEFAULT NULL             COMMENT '联系电话',
  `address`         VARCHAR(200) DEFAULT NULL             COMMENT '家庭住址',
  `login_code`      VARCHAR(50)  DEFAULT NULL             COMMENT '登录编码',
  `check_in_date`   VARCHAR(20)  DEFAULT NULL             COMMENT '入住日期',
  `status`          VARCHAR(20)  DEFAULT '在住'           COMMENT '状态：在住 / 外出 / 退住',
  `health_info`     TEXT         DEFAULT NULL             COMMENT '健康信息',
  `nursing_level_id` BIGINT      DEFAULT NULL             COMMENT '护理级别ID',
  `designated_worker_id` BIGINT  DEFAULT NULL             COMMENT '指定护工ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户表（老人）';

-- 预设客户
INSERT INTO `customer` (`username`, `age`, `gender`, `phone`, `login_code`, `status`) VALUES
('张三', 75, '男', '13800138001', 'zhangsan', '在住'),
('李四', 68, '女', '13800138002', 'lisi', '在住'),
('王五', 80, '男', '13800138003', 'wangwu', '在住');

-- ============================================
-- 3. 房间表
-- 对应实体：Room
-- ============================================
DROP TABLE IF EXISTS `room`;
CREATE TABLE `room` (
  `id`           BIGINT       NOT NULL AUTO_INCREMENT  COMMENT '房间ID',
  `room_id`      VARCHAR(50)  NOT NULL                 COMMENT '房间编号（如 RM-xxx）',
  `room_number`  VARCHAR(20)  NOT NULL                 COMMENT '房间号（如 101）',
  `room_type`    VARCHAR(20)  DEFAULT NULL             COMMENT '房间类型：单人间 / 双人间',
  `floor`        INT          DEFAULT NULL             COMMENT '楼层',
  `status`       VARCHAR(20)  DEFAULT '可用'           COMMENT '状态：可用 / 已满 / 维修中',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_room_number` (`room_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='房间表';

-- ============================================
-- 4. 床位表
-- 对应实体：Bed
-- ============================================
DROP TABLE IF EXISTS `bed`;
CREATE TABLE `bed` (
  `id`           BIGINT       NOT NULL AUTO_INCREMENT  COMMENT '床位ID',
  `bed_id`       VARCHAR(50)  NOT NULL                 COMMENT '床位业务ID（如 BD-xxx）',
  `bed_number`   VARCHAR(20)  NOT NULL                 COMMENT '床位号（如 A、B）',
  `bed_type`     VARCHAR(20)  DEFAULT '普通床'         COMMENT '床位类型：普通床 / 护理床',
  `status`       VARCHAR(20)  DEFAULT '空闲'           COMMENT '状态：空闲 / 已入住 / 维修中',
  `room_id`      BIGINT       NOT NULL                 COMMENT '所属房间ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_bed_id` (`bed_id`),
  KEY `idx_room_id` (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='床位表';

-- ============================================
-- 5. 床位使用记录表
-- 对应实体：BedUsageHistory
-- ============================================
DROP TABLE IF EXISTS `bed_usage_history`;
CREATE TABLE `bed_usage_history` (
  `id`           BIGINT       NOT NULL AUTO_INCREMENT  COMMENT '记录ID',
  `history_id`   VARCHAR(50)  NOT NULL                 COMMENT '记录业务ID（如 BU-xxx）',
  `start_date`   DATETIME     DEFAULT NULL             COMMENT '入住日期',
  `end_date`     DATETIME     DEFAULT NULL             COMMENT '退住日期',
  `monthly_fee`  DECIMAL(10,2) DEFAULT 0              COMMENT '月费',
  `status`       VARCHAR(20)  DEFAULT '入住中'         COMMENT '状态',
  `customer_id`  BIGINT       DEFAULT NULL             COMMENT '客户ID',
  `bed_id`       VARCHAR(50)  DEFAULT NULL             COMMENT '床位业务ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_history_id` (`history_id`),
  KEY `idx_customer_id` (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='床位使用记录表';

-- ============================================
-- 6. 退住申请表
-- 对应实体：CheckOutApplication
-- ============================================
DROP TABLE IF EXISTS `check_out_application`;
CREATE TABLE `check_out_application` (
  `id`                    BIGINT       NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
  `application_id`        VARCHAR(50)  NOT NULL                 COMMENT '申请业务ID（如 CO-xxx）',
  `apply_time`            DATETIME     DEFAULT NULL             COMMENT '申请时间',
  `reason`                VARCHAR(500) DEFAULT NULL             COMMENT '退住原因',
  `planned_check_out_date` DATETIME    DEFAULT NULL             COMMENT '计划退住日期',
  `actual_check_out_date`  DATETIME    DEFAULT NULL             COMMENT '实际退住日期',
  `refund_amount`         DECIMAL(10,2) DEFAULT 0              COMMENT '退款金额',
  `approval_status`       VARCHAR(20)  DEFAULT '待审批'         COMMENT '审批状态：待审批 / 已批准 / 已拒绝',
  `approver`              VARCHAR(50)  DEFAULT NULL             COMMENT '审批人',
  `approval_time`         DATETIME     DEFAULT NULL             COMMENT '审批时间',
  `customer_id`           BIGINT       DEFAULT NULL             COMMENT '客户ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_application_id` (`application_id`),
  KEY `idx_customer_id` (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退住申请表';

-- ============================================
-- 7. 外出申请表
-- 对应实体：OutingApplication
-- ============================================
DROP TABLE IF EXISTS `outing_application`;
CREATE TABLE `outing_application` (
  `id`                   BIGINT       NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
  `application_id`       VARCHAR(50)  NOT NULL                 COMMENT '申请业务ID（如 OUT-xxx）',
  `reason`               VARCHAR(500) DEFAULT NULL             COMMENT '外出事由',
  `outing_time`          DATETIME     DEFAULT NULL             COMMENT '外出时间',
  `planned_return_time`  DATETIME     DEFAULT NULL             COMMENT '预计返回时间',
  `actual_return_time`   DATETIME     DEFAULT NULL             COMMENT '实际返回时间',
  `destination`          VARCHAR(200) DEFAULT NULL             COMMENT '目的地',
  `companion`            VARCHAR(50)  DEFAULT NULL             COMMENT '陪同人',
  `approval_status`      VARCHAR(20)  DEFAULT '待审批'         COMMENT '审批状态：待审批 / 已批准 / 已拒绝',
  `approver`             VARCHAR(50)  DEFAULT NULL             COMMENT '审批人',
  `approval_time`        DATETIME     DEFAULT NULL             COMMENT '审批时间',
  `customer_id`          BIGINT       DEFAULT NULL             COMMENT '客户ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_application_id` (`application_id`),
  KEY `idx_customer_id` (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='外出申请表';

-- ============================================
-- 8. 护理级别表
-- 对应实体：NursingLevel
-- ============================================
DROP TABLE IF EXISTS `nursing_level`;
CREATE TABLE `nursing_level` (
  `id`       BIGINT       NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
  `level_id` BIGINT       NOT NULL                 COMMENT '级别业务ID',
  `name`     VARCHAR(100) NOT NULL                 COMMENT '级别名称',
  `status`   VARCHAR(20)  DEFAULT '启用'           COMMENT '状态：启用 / 停用',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_level_id` (`level_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='护理级别表';

-- 预设数据
INSERT INTO `nursing_level` (`level_id`, `name`, `status`) VALUES
(1, '基础护理', '启用'),
(2, '高级护理', '启用');

-- ============================================
-- 9. 护理项目表
-- 对应实体：NursingProject
-- ============================================
DROP TABLE IF EXISTS `nursing_project`;
CREATE TABLE `nursing_project` (
  `id`          BIGINT         NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
  `project_id`  BIGINT         NOT NULL                 COMMENT '项目业务ID',
  `code`        VARCHAR(50)    NOT NULL                 COMMENT '项目编码',
  `name`        VARCHAR(100)   NOT NULL                 COMMENT '项目名称',
  `price`       DECIMAL(10,2)  DEFAULT 0               COMMENT '价格',
  `status`      VARCHAR(20)    DEFAULT '启用'           COMMENT '状态：启用 / 停用 / 已删除',
  `exec_cycle`  VARCHAR(50)    DEFAULT NULL             COMMENT '执行周期：每日 / 每周 / 每2小时',
  `exec_times`  INT            DEFAULT 1               COMMENT '执行次数',
  `description` VARCHAR(500)   DEFAULT NULL             COMMENT '项目描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_project_id` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='护理项目表';

-- 预设数据
INSERT INTO `nursing_project` (`project_id`, `code`, `name`, `price`, `status`, `exec_cycle`, `exec_times`, `description`) VALUES
(1, 'NP001', '量血压',   10, '启用', '每日',    2, '每日测量血压'),
(2, 'NP002', '翻身护理', 20, '启用', '每2小时', 6, '协助翻身'),
(3, 'NP003', '喂药',     15, '启用', '每日',    3, '按时喂药');

-- ============================================
-- 10. 级别-项目关联表
-- 对应实体：LevelProject
-- ============================================
DROP TABLE IF EXISTS `level_project`;
CREATE TABLE `level_project` (
  `id`         BIGINT NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
  `level_id`   BIGINT NOT NULL                 COMMENT '护理级别ID',
  `project_id` BIGINT NOT NULL                 COMMENT '护理项目ID',
  PRIMARY KEY (`id`),
  KEY `idx_level_id` (`level_id`),
  KEY `idx_project_id` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='级别-项目关联表';

-- 预设数据：基础护理(1) → 量血压(1) + 喂药(3)；高级护理(2) → 量血压(1) + 翻身护理(2) + 喂药(3)
INSERT INTO `level_project` (`level_id`, `project_id`) VALUES
(1, 1), (1, 3),
(2, 1), (2, 2), (2, 3);

-- ============================================
-- 11. 客户购买的护理项目表
-- 对应实体：CustomerNursingProject
-- ============================================
DROP TABLE IF EXISTS `customer_nursing_project`;
CREATE TABLE `customer_nursing_project` (
  `id`                  BIGINT   NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
  `customer_id`         BIGINT   DEFAULT NULL             COMMENT '客户ID',
  `project_id`          BIGINT   DEFAULT NULL             COMMENT '项目业务ID',
  `purchase_date`       DATETIME DEFAULT NULL             COMMENT '购买日期',
  `quantity`            INT      DEFAULT 1                COMMENT '购买数量',
  `expire_date`         DATETIME DEFAULT NULL             COMMENT '到期日期',
  `remaining_quantity`  INT      DEFAULT 0                COMMENT '剩余数量',
  PRIMARY KEY (`id`),
  KEY `idx_customer_id` (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户购买的护理项目表';

-- ============================================
-- 12. 护理记录表
-- 对应实体：NursingRecord
-- ============================================
DROP TABLE IF EXISTS `nursing_record`;
CREATE TABLE `nursing_record` (
  `id`               BIGINT   NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
  `customer_id`      BIGINT   DEFAULT NULL             COMMENT '客户ID',
  `project_id`       BIGINT   DEFAULT NULL             COMMENT '护理项目ID',
  `nursing_date`     DATETIME DEFAULT NULL             COMMENT '护理日期',
  `nursing_quantity` INT      DEFAULT 1                COMMENT '护理数量',
  `is_deleted`       TINYINT  DEFAULT 0                COMMENT '是否删除：0-否 / 1-是',
  PRIMARY KEY (`id`),
  KEY `idx_customer_id` (`customer_id`),
  KEY `idx_nursing_date` (`nursing_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='护理记录表';

-- ============================================
-- 初始化房间和床位数据
-- ============================================
-- 插入房间
INSERT INTO `room` (`room_id`, `room_number`, `room_type`, `floor`, `status`) VALUES
('RM-101', '101', '双人间', 1, '可用'),
('RM-102', '102', '单人间', 1, '可用'),
('RM-201', '201', '双人间', 2, '可用');

-- 插入床位（101房间）
INSERT INTO `bed` (`bed_id`, `bed_number`, `bed_type`, `status`, `room_id`) VALUES
('BD-101-A', 'A', '普通床', '空闲', 1),
('BD-101-B', 'B', '普通床', '空闲', 1);

-- 插入床位（102房间）
INSERT INTO `bed` (`bed_id`, `bed_number`, `bed_type`, `status`, `room_id`) VALUES
('BD-102-A', 'A', '护理床', '空闲', 2);

-- 插入床位（201房间）
INSERT INTO `bed` (`bed_id`, `bed_number`, `bed_type`, `status`, `room_id`) VALUES
('BD-201-A', 'A', '普通床', '空闲', 3),
('BD-201-B', 'B', '普通床', '空闲', 3);
