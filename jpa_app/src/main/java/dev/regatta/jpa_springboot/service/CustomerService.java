package dev.regatta.jpa_springboot.service;

import dev.regatta.jpa_springboot.entity.Customer;
import dev.regatta.jpa_springboot.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired private CustomerRepository customerRepository;

    public List<Customer> findAll() { return customerRepository.findAll(); }

    public Optional<Customer> findOne(Long id) {
        return customerRepository.findById(id);
    }

    @Transactional
    public Customer create(Customer customer) {
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer update(Long id, Customer customerDetails) {
        Customer customer = customerRepository.findById(id).orElseThrow(
            () -> new RuntimeException("Customer not found with id " + id));

        if (customerDetails.getName() != null) {
            customer.setName(customerDetails.getName());
        }
        if (customerDetails.getEmail() != null) {
            customer.setEmail(customerDetails.getEmail());
        }
        if (customerDetails.getPhone() != null) {
            customer.setPhone(customerDetails.getPhone());
        }

        return customerRepository.save(customer);
    }

    @Transactional
    public void delete(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(
            () -> new RuntimeException("Customer not found with id " + id));
        customerRepository.delete(customer);
    }
}
