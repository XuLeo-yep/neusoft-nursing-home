package org.example.neuobject.nursinghouse.controller;

import org.example.neuobject.nursinghouse.common.Result;
import org.example.neuobject.nursinghouse.entity.Customer;
import org.example.neuobject.nursinghouse.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/add")
    public Result addCustomer(@RequestBody Customer customer) {
        customerService.addCustomer(customer);
        return Result.success();
    }

    @GetMapping("/list")
    public Result listCustomers(@RequestParam(required = false) String name) {
        List<Customer> customers;
        if (name != null && !name.isEmpty()) {
            customers = customerService.findByUsername(name);
        } else {
            customers = customerService.findAll();
        }
        return Result.success(customers);
    }

    @GetMapping("/{id}")
    public Result getCustomer(@PathVariable long id) {
        Customer customer = customerService.findById(id);
        if (customer == null) {
            return Result.error("客户不存在");
        }
        return Result.success(customer);
    }

    @PutMapping("/update")
    public Result updateCustomer(@RequestBody Customer customer) {
        customerService.updateCustomer(customer);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result deleteCustomer(@PathVariable long id) {
        customerService.deleteCustomer(id);
        return Result.success();
    }
}
