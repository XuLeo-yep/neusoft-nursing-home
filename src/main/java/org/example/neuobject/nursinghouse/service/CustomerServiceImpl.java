package org.example.neuobject.nursinghouse.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.neuobject.nursinghouse.entity.Customer;
import org.example.neuobject.nursinghouse.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public void addCustomer(Customer customer) {
        customerMapper.insert(customer);
    }

    @Override
    public void deleteCustomer(long id) {
        customerMapper.deleteById(id);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customerMapper.updateById(customer);
    }

    @Override
    public Customer findById(long id) {
        return customerMapper.selectById(id);
    }

    @Override
    public List<Customer> findAll() {
        return customerMapper.selectList(null);
    }

    @Override
    public List<Customer> findByUsername(String username) {
        return customerMapper.selectList(
            new LambdaQueryWrapper<Customer>().like(Customer::getUsername, username)
        );
    }
}
