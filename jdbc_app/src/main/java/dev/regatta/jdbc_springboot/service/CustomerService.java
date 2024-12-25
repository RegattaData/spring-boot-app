package dev.regatta.jdbc_springboot.service;

import dev.regatta.jdbc_springboot.dao.CustomerDAO;
import dev.regatta.jdbc_springboot.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerDAO customerDAO;

    public List<Customer> findAll() {
        try {
            return customerDAO.listAllCustomers();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch customers", e);
        }
    }

    public Customer findOne(Long id) {
        try {
            return customerDAO.getCustomer(id);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch customer with id " + id, e);
        }
    }

    public Customer create(Customer customer) {
        try {
            boolean inserted = customerDAO.insertCustomer(customer);
            if (!inserted) {
                throw new RuntimeException("Failed to create customer");
            }
            return customer;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create customer", e);
        }
    }

    public List<Customer> createAll(List<Customer> customers) {
        try {
            boolean success = customerDAO.bulkInsertCustomers(customers);
            if (!success) {
                throw new RuntimeException("Failed to create some customers in bulk");
            }
            return customers;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create customers in bulk", e);
        }
    }

    public Customer update(Long id, Customer customer) {
        try {
            Customer existing = customerDAO.getCustomer(id);
            if (existing == null) {
                throw new RuntimeException("Customer not found with id " + id);
            }
            existing.setName(customer.getName());
            existing.setEmail(customer.getEmail());
            existing.setPhone(customer.getPhone());
            boolean updated = customerDAO.updateCustomer(existing);
            if (!updated) {
                throw new RuntimeException("Failed to update customer with id " + id);
            }
            return existing;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update customer with id " + id, e);
        }
    }

    public boolean deleteCustomerById(Long id) {
        try {
            boolean deleted = customerDAO.deleteCustomer(id);
            if (!deleted) {
                return false;
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete customer with id " + id, e);
        }
    }

}
