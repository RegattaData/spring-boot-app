package dev.regatta.jdbc_springboot.dao;

import dev.regatta.jdbc_springboot.entity.Customer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomerDAO {
    private final String jdbcURL;
    private final String jdbcUsername;
    private final String jdbcPassword;

    // Constructor
    public CustomerDAO(@Value("${spring.datasource.url}") String jdbcURL,
                       @Value("${spring.datasource.username}") String jdbcUsername,
                       @Value("${spring.datasource.password}") String jdbcPassword) {
        this.jdbcURL = jdbcURL;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
    }

    // Insert a new customer
    public boolean insertCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers (customerid, name, email, phone) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, customer.getCustomerId());
            statement.setString(2, customer.getName());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getPhone());

            return statement.executeUpdate() > 0;
        }
    }

    // Bulk Insert Customers
    public boolean bulkInsertCustomers(List<Customer> customers) throws SQLException {
        StringBuilder sql = new StringBuilder("INSERT INTO customers (customerid, name, email, phone) VALUES ");
        int count = 0;

        for (Customer customer : customers) {
            sql.append("(")
               .append(customer.getCustomerId()).append(", ")
               .append("'").append(customer.getName().replace("'", "''")).append("', ")
               .append("'").append(customer.getEmail().replace("'", "''")).append("', ")
               .append("'").append(customer.getPhone().replace("'", "''")).append("')")
               .append(", ");
            count++;
        }

        // Remove trailing comma and space, and add a semicolon
        if (count > 0) {
            sql.setLength(sql.length() - 2);
            sql.append(";");
        }

        // Execute the bulk insert
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             Statement statement = connection.createStatement()) {

            int rowsInserted = statement.executeUpdate(sql.toString());
            return rowsInserted == count;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute bulk insert for customers", e);
        }
    }

    // List all customers
    public List<Customer> listAllCustomers() throws SQLException {
        String sql = "SELECT * FROM customers";
        List<Customer> listCustomer = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("customerid");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");

                listCustomer.add(new Customer(id, name, email, phone));
            }
        }

        return listCustomer;
    }

    // Update a customer
    public boolean updateCustomer(Customer customer) throws SQLException {
        String sql = "UPDATE customers SET name = ?, email = ?, phone = ? WHERE customerid = ?";

        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, customer.getName());
            statement.setString(2, customer.getEmail());
            statement.setString(3, customer.getPhone());
            statement.setLong(4, customer.getCustomerId());

            return statement.executeUpdate() > 0;
        }
    }

    // Delete a customer
    public boolean deleteCustomer(Long customerId) throws SQLException {
        String sql = "DELETE FROM customers WHERE customerid = ?";

        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, customerId);

            return statement.executeUpdate() > 0;
        }
    }

    // Get a customer by ID
    public Customer getCustomer(Long customerId) throws SQLException {
        String sql = "SELECT * FROM customers WHERE customerid = ?";
        Customer customer = null;

        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, customerId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    String phone = resultSet.getString("phone");

                    customer = new Customer(customerId, name, email, phone);
                }
            }
        }

        return customer;
    }
}
