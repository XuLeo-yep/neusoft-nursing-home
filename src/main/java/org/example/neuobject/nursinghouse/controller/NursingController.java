package org.example.neuobject.nursinghouse.controller;

import org.example.neuobject.nursinghouse.common.Result;
import org.example.neuobject.nursinghouse.entity.*;
import org.example.neuobject.nursinghouse.service.CustomerService;
import org.example.neuobject.nursinghouse.service.NursingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/nursing")
public class NursingController {

    @Autowired
    private NursingService nursingService;
    @Autowired
    private CustomerService customerService;

    // ==================== 护理级别 ====================
    @GetMapping("/level/list")
    public Result listNursingLevels() {
        return Result.success(nursingService.findAllNursingLevels());
    }

    @PostMapping("/level/add")
    public Result addNursingLevel(@RequestBody NursingLevel level) {
        nursingService.saveNursingLevel(level);
        return Result.success();
    }

    @PutMapping("/level/{id}/status")
    public Result updateNursingLevelStatus(@PathVariable Long id, @RequestBody Map<String, String> params) {
        nursingService.updateNursingLevelStatus(id, params.get("status"));
        return Result.success();
    }

    // ==================== 护理项目 ====================
    @GetMapping("/project/list")
    public Result listNursingProjects() {
        return Result.success(nursingService.findAllNursingProjects());
    }

    @PostMapping("/project/add")
    public Result addNursingProject(@RequestBody NursingProject project) {
        nursingService.saveNursingProject(project);
        return Result.success();
    }

    @PutMapping("/project/update")
    public Result updateNursingProject(@RequestBody NursingProject project) {
        NursingProject existing = nursingService.findNursingProjectById(project.getProjectId());
        if (existing == null) return Result.error("项目不存在");
        existing.setName(project.getName());
        existing.setPrice(project.getPrice());
        existing.setStatus(project.getStatus());
        nursingService.saveNursingProject(existing);
        return Result.success();
    }

    @DeleteMapping("/project/{id}")
    public Result deleteNursingProject(@PathVariable Long id) {
        nursingService.deleteNursingProject(id);
        return Result.success();
    }

    // ==================== 级别-项目关联 ====================
    @GetMapping("/level/{levelId}/projects")
    public Result listLevelProjects(@PathVariable Long levelId) {
        return Result.success(nursingService.findLevelProjectsByLevelId(levelId));
    }

    @PostMapping("/level/{levelId}/project/{projectId}")
    public Result addLevelProject(@PathVariable Long levelId, @PathVariable Long projectId) {
        LevelProject lp = new LevelProject();
        lp.setLevelId(levelId);
        lp.setProjectId(projectId);
        nursingService.addLevelProject(lp);
        return Result.success();
    }

    @DeleteMapping("/level/{levelId}/project/{projectId}")
    public Result removeLevelProject(@PathVariable Long levelId, @PathVariable Long projectId) {
        nursingService.removeLevelProject(levelId, projectId);
        return Result.success();
    }

    // ==================== 客户护理设置 ====================
    @PostMapping("/customer/{customerId}/level/{levelId}")
    public Result setCustomerNursingLevel(@PathVariable Long customerId, @PathVariable Long levelId) {
        Customer customer = customerService.findById(customerId);
        if (customer == null) return Result.error("客户不存在");

        NursingLevel level = nursingService.findNursingLevelById(levelId);
        if (level == null) return Result.error("护理级别不存在");

        customer.setNursingLevelId(levelId);
        customerService.updateCustomer(customer);
        return Result.success();
    }

    @DeleteMapping("/customer/{customerId}/level")
    public Result removeCustomerNursingLevel(@PathVariable Long customerId) {
        Customer customer = customerService.findById(customerId);
        if (customer == null) return Result.error("客户不存在");

        customer.setNursingLevelId(null);
        customerService.updateCustomer(customer);
        return Result.success();
    }

    @GetMapping("/customer/{customerId}/project")
    public Result listCustomerProjects(@PathVariable Long customerId) {
        return Result.success(nursingService.findCustomerNursingProjectsByCustomerId(customerId));
    }

    @PostMapping("/customer/{customerId}/project")
    public Result purchaseNursingProject(@PathVariable Long customerId, @RequestBody Map<String, Object> params) {
        Customer customer = customerService.findById(customerId);
        if (customer == null) return Result.error("客户不存在");

        Long projectId = Long.valueOf(params.get("projectId").toString());
        NursingProject project = nursingService.findNursingProjectById(projectId);
        if (project == null) return Result.error("护理项目不存在");

        int quantity = params.containsKey("quantity") ? (int) params.get("quantity") : 1;
        if (quantity <= 0) quantity = 1;

        Date expireDate = new Date(System.currentTimeMillis() + 90L * 24 * 60 * 60 * 1000);

        CustomerNursingProject cnp = new CustomerNursingProject();
        cnp.setId(System.currentTimeMillis());
        cnp.setPurchaseDate(new Date());
        cnp.setQuanity(quantity);
        cnp.setExpireDate(expireDate);
        cnp.setRemainingQuanity(quantity);
        cnp.setProjectId(projectId);
        cnp.setCustomerId(customerId);
        nursingService.saveCustomerNursingProject(cnp);
        return Result.success();
    }

    // ==================== 护理记录 ====================
    @GetMapping("/record/list/{customerId}")
    public Result listNursingRecords(@PathVariable Long customerId) {
        return Result.success(nursingService.findNursingRecordsByCustomerId(customerId));
    }

    @PostMapping("/record/add")
    public Result addNursingRecord(@RequestBody Map<String, Object> params) {
        Long customerId = Long.valueOf(params.get("customerId").toString());
        Customer customer = customerService.findById(customerId);
        if (customer == null) return Result.error("客户不存在");

        Long projectId = Long.valueOf(params.get("projectId").toString());
        NursingProject project = nursingService.findNursingProjectById(projectId);
        if (project == null) return Result.error("护理项目不存在");

        int quantity = params.containsKey("quantity") ? (int) params.get("quantity") : 1;

        NursingRecord record = new NursingRecord();
        record.setId(System.currentTimeMillis());
        record.setNursingDate(new Date());
        record.setNursingQuanity(quantity);
        record.setDeleted(false);
        record.setProjectId(projectId);
        record.setCustomerId(customerId);
        nursingService.saveNursingRecord(record);
        return Result.success();
    }

    @DeleteMapping("/record/{id}")
    public Result deleteNursingRecord(@PathVariable Long id) {
        nursingService.deleteNursingRecord(id);
        return Result.success();
    }
}
