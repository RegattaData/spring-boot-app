package dev.regatta.jdbc_springboot.service;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class DatabaseSetupService {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseSetupService.class);

    @Value("${spring.datasource.url}")
    private String jdbcURL;

    @Value("${spring.datasource.username}")
    private String jdbcUsername;

    @Value("${spring.datasource.password}")
    private String jdbcPassword;

    @EventListener(ContextRefreshedEvent.class)
    public void createTables() {
        logger.info("Starting table creation...");
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             Statement statement = connection.createStatement()) {

            Set<String> existingTables = getExistingTables(connection);

            if (!existingTables.contains("customers")) {
                createCustomerTable(statement);
            } else {
                logger.info("Customer table already exists.");
            }

            if (!existingTables.contains("purchases")) {
                createPurchaseTable(statement);
            } else {
                logger.info("Purchase table already exists.");
            }

        } catch (SQLException e) {
            logger.error("Failed to check or create tables in the database.", e);
            throw new RuntimeException("Failed to check or create tables in the database.", e);
        }
    }

    private Set<String> getExistingTables(Connection connection) throws SQLException {
        Set<String> tables = new HashSet<>();
        String showTablesSQL = "SHOW TABLES";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(showTablesSQL)) {
            while (resultSet.next()) {
                tables.add(resultSet.getString(1).toLowerCase());
            }
        }
        logger.debug("Existing tables: {}", tables);
        return tables;
    }

    private void createCustomerTable(Statement statement) throws SQLException {
        String createCustomerTableSQL = """
                CREATE TABLE customers (
                    customerid BIGINT PRIMARY KEY INDEX,
                    name VARCHAR(100) NOT NULL,
                    email VARCHAR(100) NOT NULL,
                    phone VARCHAR(15)
                );
                """;
        statement.execute(createCustomerTableSQL);
        logger.debug("Customer table created or already exists.");
    }

    private void createPurchaseTable(Statement statement) throws SQLException {
        String createPurchaseTableSQL = """
                CREATE TABLE purchases (
                    purchaseid BIGINT PRIMARY KEY INDEX,
                    customerid BIGINT NOT NULL REFERENCES customers INDEX,
                    item VARCHAR(255),
                    price DECIMAL(10, 2),
                    purchasedate DATE
                );
                """;
        statement.execute(createPurchaseTableSQL);
        logger.debug("Purchase table created or already exists.");
    }
}
