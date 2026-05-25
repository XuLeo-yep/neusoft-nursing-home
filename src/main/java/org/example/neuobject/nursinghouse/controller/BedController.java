package org.example.neuobject.nursinghouse.controller;

import org.example.neuobject.nursinghouse.common.Result;
import org.example.neuobject.nursinghouse.entity.*;
import org.example.neuobject.nursinghouse.mapper.BedMapper;
import org.example.neuobject.nursinghouse.service.BedService;
import org.example.neuobject.nursinghouse.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/bed")
public class BedController {

    @Autowired
    private BedService bedService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private BedMapper bedMapper;

    @GetMapping("/diagram")
    public Result showBedDiagram() {
        List<Bed> allBeds = bedMapper.selectList(null);
        int totalBeds = allBeds.size();
        int freeBeds = 0, occupiedBeds = 0;
        for (Bed bed : allBeds) {
            if ("空闲".equals(bed.getStatus())) freeBeds++;
            else if ("已入住".equals(bed.getStatus())) occupiedBeds++;
        }
        Map<String, Integer> stats = new HashMap<>();
        stats.put("total", totalBeds);
        stats.put("free", freeBeds);
        stats.put("occupied", occupiedBeds);
        return Result.success(stats);
    }

    @GetMapping("/rooms")
    public Result listAllRooms() {
        return Result.success(bedService.findAllRooms());
    }

    @GetMapping("/history")
    public Result listBedUsageHistories() {
        return Result.success(bedService.findAllBedUsageHistories());
    }

    @PostMapping("/transfer")
    public Result transferBed(@RequestBody Map<String, Object> params) {
        long customerId = Long.valueOf(params.get("customerId").toString());
        String roomNumber = (String) params.get("roomNumber");
        long bedId = Long.valueOf(params.get("bedId").toString());

        Customer customer = customerService.findById(customerId);
        if (customer == null) return Result.error("客户不存在");

        Room targetRoom = bedService.findRoomByNumber(roomNumber);
        if (targetRoom == null) return Result.error("房间不存在");

        bedService.updateBedStatus(bedId, "已入住");

        BedUsageHistory history = new BedUsageHistory();
        history.setHistoryId("BU-" + System.currentTimeMillis());
        history.setStartDate(new Date());
        history.setStatus("在住");
        history.setMonthlyFee(3000.0);
        history.setCustomerId(customerId);
        history.setBedId(String.valueOf(bedId));
        bedService.saveBedUsageHistory(history);

        return Result.success();
    }
}
