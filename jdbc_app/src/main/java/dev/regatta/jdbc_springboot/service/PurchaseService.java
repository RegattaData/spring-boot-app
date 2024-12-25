package dev.regatta.jdbc_springboot.service;

import dev.regatta.jdbc_springboot.dao.PurchaseDAO;
import dev.regatta.jdbc_springboot.entity.Purchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseDAO purchaseDAO;

    public List<Purchase> findAll() {
        try {
            return purchaseDAO.listAllPurchases();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch all purchases", e);
        }
    }

    public Purchase findOne(Long id) {
        try {
            Purchase purchase = purchaseDAO.getPurchase(id);
            if (purchase == null) {
                throw new IllegalArgumentException("Purchase not found with id " + id);
            }
            return purchase;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch purchase with id " + id, e);
        }
    }

    public List<Purchase> findByCustomer(Long customerId) {
        try {
            return purchaseDAO.listPurchasesByCustomer(customerId);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch purchases for customer with id " + customerId, e);
        }
    }

    public Purchase create(Purchase purchase) {
        try {
            if (!purchaseDAO.insertPurchase(purchase)) {
                throw new IllegalArgumentException("Failed to create purchase");
            }
            return purchase;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create purchase", e);
        }
    }

    public List<Purchase> createAll(List<Purchase> purchases) {
        try {
            boolean success = purchaseDAO.bulkInsertPurchases(purchases);
            if (!success) {
                throw new RuntimeException("Failed to create some customers in bulk");
            }
            return purchases;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create customers in bulk", e);
        }
    }

    public Purchase update(Long id, Purchase purchase) {
        try {
            Purchase existing = purchaseDAO.getPurchase(id);
            if (existing == null) {
                throw new IllegalArgumentException("Purchase not found with id " + id);
            }

            // Update the fields of the existing purchase
            existing.setItem(purchase.getItem());
            existing.setPrice(purchase.getPrice());
            existing.setPurchaseDate(purchase.getPurchaseDate());
            existing.setCustomerId(purchase.getCustomerId());

            if (!purchaseDAO.updatePurchase(existing)) {
                throw new IllegalArgumentException("Failed to update purchase with id " + id);
            }
            return existing;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update purchase with id " + id, e);
        }
    }

    public void delete(Long id) {
        try {
            if (!purchaseDAO.deletePurchase(id)) {
                throw new IllegalArgumentException("Failed to delete purchase with id " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete purchase with id " + id, e);
        }
    }
}
