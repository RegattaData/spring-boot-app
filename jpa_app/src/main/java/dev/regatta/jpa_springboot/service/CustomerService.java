package dev.regatta.jpa_springboot.service;

import dev.regatta.jpa_springboot.entity.Customer;
import dev.regatta.jpa_springboot.repository.CustomerRepository;
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

    public Customer create(Customer customer) {
        return customerRepository.save(customer);
    }

    public List<Customer> createAll(List<Customer> customers) {
      return customerRepository.saveAll(customers);
    }

    public Customer update(Long id, Customer customer) {
      Customer existing = customerRepository.findById(id).orElseThrow(
          () -> new RuntimeException("Customer not found with id " + id));
      existing.setName(customer.getName());
      existing.setEmail(customer.getEmail());
      existing.setPhone(customer.getPhone());
      return customerRepository.save(existing);
    }

    public void delete(Long id) { customerRepository.deleteById(id); }
}
