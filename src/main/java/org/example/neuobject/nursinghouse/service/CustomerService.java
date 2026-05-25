package org.example.neuobject.nursinghouse.service;

import org.example.neuobject.nursinghouse.entity.Customer;

import java.util.List;

public interface CustomerService {

    void addCustomer(Customer customer);

    void deleteCustomer(long id);

    void updateCustomer(Customer customer);

    Customer findById(long id);

    List<Customer> findAll();

    List<Customer> findByUsername(String username);
}
