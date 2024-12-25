package dev.regatta.jdbc_springboot.dao;

import dev.regatta.jdbc_springboot.entity.Purchase;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PurchaseDAO {

    private final String jdbcURL;
    private final String jdbcUsername;
    private final String jdbcPassword;

    public PurchaseDAO(@Value("${spring.datasource.url}") String jdbcURL,
                       @Value("${spring.datasource.username}") String jdbcUsername,
                       @Value("${spring.datasource.password}") String jdbcPassword) {
        this.jdbcURL = jdbcURL;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
    }

    public List<Purchase> listPurchasesByCustomer(Long customerId) throws SQLException {
        String sql = "SELECT * FROM purchases WHERE customerid = ?";
        List<Purchase> purchases = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, customerId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Purchase purchase = new Purchase();
                    purchase.setPurchaseId(resultSet.getLong("purchaseid"));
                    purchase.setCustomerId(resultSet.getLong("customerid"));
                    purchase.setItem(resultSet.getString("item"));
                    purchase.setPrice(resultSet.getBigDecimal("price"));
                    purchase.setPurchaseDate(resultSet.getDate("purchasedate").toLocalDate());
                    purchases.add(purchase);
                }
            }
        }

        return purchases;
    }

    public boolean insertPurchase(Purchase purchase) throws SQLException {
        String sql = "INSERT INTO purchases (purchaseid, customerid, item, price, purchasedate) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, purchase.getPurchaseId());
            statement.setLong(2, purchase.getCustomerId());
            statement.setString(3, purchase.getItem());
            statement.setBigDecimal(4, purchase.getPrice());
            statement.setDate(5, Date.valueOf(purchase.getPurchaseDate()));

            return statement.executeUpdate() > 0;
        }
    }

    // Bulk Insert Purchases
    public boolean bulkInsertPurchases(List<Purchase> purchases) throws SQLException {
        StringBuilder sql = new StringBuilder("INSERT INTO purchases (purchaseid, customerid, item, price, purchasedate) VALUES ");
        int count = 0;

        for (Purchase purchase : purchases) {
            sql.append("(")
               .append(purchase.getPurchaseId()).append(", ")
               .append(purchase.getCustomerId()).append(", ")
               .append("'").append(purchase.getItem().replace("'", "''")).append("', ")
               .append(purchase.getPrice()).append(", ")
               .append("'").append(Date.valueOf(purchase.getPurchaseDate())).append("')")
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
            throw new RuntimeException("Failed to execute bulk insert for purchases", e);
        }
    }

    public List<Purchase> listAllPurchases() throws SQLException {
        String sql = "SELECT * FROM purchases";
        List<Purchase> purchases = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Purchase purchase = new Purchase();
                purchase.setPurchaseId(resultSet.getLong("purchaseid"));
                purchase.setCustomerId(resultSet.getLong("customerid"));
                purchase.setItem(resultSet.getString("item"));
                purchase.setPrice(resultSet.getBigDecimal("price"));
                purchase.setPurchaseDate(resultSet.getDate("purchasedate").toLocalDate());
                purchases.add(purchase);
            }
        }

        return purchases;
    }

    public boolean updatePurchase(Purchase purchase) throws SQLException {
        String sql = "UPDATE purchases SET customerid = ?, item = ?, price = ?, purchasedate = ? WHERE purchaseid = ?";

        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, purchase.getCustomerId());
            statement.setString(2, purchase.getItem());
            statement.setBigDecimal(3, purchase.getPrice());
            statement.setDate(4, Date.valueOf(purchase.getPurchaseDate()));
            statement.setLong(5, purchase.getPurchaseId());

            return statement.executeUpdate() > 0;
        }
    }

    public boolean deletePurchase(Long purchaseId) throws SQLException {
        String sql = "DELETE FROM purchases WHERE purchaseid = ?";

        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, purchaseId);

            return statement.executeUpdate() > 0;
        }
    }

    public Purchase getPurchase(Long purchaseId) throws SQLException {
        String sql = "SELECT * FROM purchases WHERE purchaseid = ?";
        Purchase purchase = null;

        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, purchaseId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    purchase = new Purchase();
                    purchase.setPurchaseId(resultSet.getLong("purchaseid"));
                    purchase.setCustomerId(resultSet.getLong("customerid"));
                    purchase.setItem(resultSet.getString("item"));
                    purchase.setPrice(resultSet.getBigDecimal("price"));
                    purchase.setPurchaseDate(resultSet.getDate("purchasedate").toLocalDate());
                }
            }
        }

        return purchase;
    }
}
