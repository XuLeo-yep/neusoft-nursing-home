package org.example.neuobject.nursinghouse.controller;

import org.example.neuobject.nursinghouse.common.Result;
import org.example.neuobject.nursinghouse.entity.*;
import org.example.neuobject.nursinghouse.service.ApplicationService;
import org.example.neuobject.nursinghouse.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/application")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private CustomerService customerService;

    // ==================== 退住申请 ====================
    @PostMapping("/checkout/submit")
    public Result addCheckOutApplication(@RequestBody Map<String, String> params) {
        long customerId = Long.parseLong(params.get("customerId"));
        Customer customer = customerService.findById(customerId);
        if (customer == null) return Result.error("客户不存在");

        Date plannedDate = parseDate(params.get("plannedCheckOutDate"));
        if (plannedDate == null) return Result.error("日期格式错误，请使用 yyyy-MM-dd");

        CheckOutApplication app = new CheckOutApplication();
        app.setApplicationId("CO-" + System.currentTimeMillis());
        app.setReason(params.get("reason"));
        app.setPlannedCheckOutDate(plannedDate);
        app.setCustomerId(customerId);
        app.setApplyTime(new Date());
        app.setApprovalStatus("待审批");
        applicationService.saveCheckOutApplication(app);
        return Result.success();
    }

    @GetMapping("/checkout/list")
    public Result listCheckOutApplications() {
        return Result.success(applicationService.findAllCheckOutApplications());
    }

    @PutMapping("/checkout/{id}/approve")
    public Result approveCheckOut(@PathVariable String id, @RequestBody Map<String, String> params) {
        CheckOutApplication app = applicationService.findCheckOutApplicationById(id);
        if (app == null) return Result.error("申请不存在");

        String result = params.get("result");  // "通过" 或 "不通过"
        String status = "通过".equals(result) ? "已批准" : "已拒绝";
        app.setApprovalStatus(status);
        app.setApprover(params.get("approver"));
        app.setApprovalTime(new Date());
        applicationService.saveCheckOutApplication(app);
        return Result.success();
    }

    // ==================== 外出申请 ====================
    @PostMapping("/outing/submit")
    public Result addOutingApplication(@RequestBody Map<String, String> params) {
        long customerId = Long.parseLong(params.get("customerId"));
        Customer customer = customerService.findById(customerId);
        if (customer == null) return Result.error("客户不存在");

        Date outingTime = parseDate(params.get("outingTime"));
        Date plannedReturnTime = parseDate(params.get("plannedReturnTime"));
        if (outingTime == null || plannedReturnTime == null) return Result.error("日期格式错误");

        OutingApplication app = new OutingApplication();
        app.setApplicationId("OUT-" + System.currentTimeMillis());
        app.setReason(params.get("reason"));
        app.setOutingTime(outingTime);
        app.setPlannedReturnTime(plannedReturnTime);
        app.setDestination(params.get("destination"));
        app.setCustomerId(customerId);
        app.setApprovalStatus("待审批");
        applicationService.saveOutingApplication(app);
        return Result.success();
    }

    @GetMapping("/outing/list")
    public Result listOutingApplications() {
        return Result.success(applicationService.findAllOutingApplications());
    }

    @PutMapping("/outing/{id}/approve")
    public Result approveOuting(@PathVariable String id, @RequestBody Map<String, String> params) {
        OutingApplication app = applicationService.findOutingApplicationById(id);
        if (app == null) return Result.error("申请不存在");

        String status = "通过".equals(params.get("result")) ? "已批准" : "已拒绝";
        app.setApprovalStatus(status);
        app.setApprover(params.get("approver"));
        app.setApprovalTime(new Date());
        applicationService.saveOutingApplication(app);
        return Result.success();
    }

    @PutMapping("/outing/{id}/return")
    public Result registerReturn(@PathVariable String id) {
        OutingApplication app = applicationService.findOutingApplicationById(id);
        if (app == null) return Result.error("申请不存在");

        app.setActualReturnTime(new Date());
        applicationService.saveOutingApplication(app);
        return Result.success();
    }

    // ==================== 工具方法 ====================
    private Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
}
