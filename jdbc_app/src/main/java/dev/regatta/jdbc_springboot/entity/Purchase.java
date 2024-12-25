package dev.regatta.jdbc_springboot.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Purchase {

    private Long purchaseId;
    private Long customerId;
    private String item;
    private BigDecimal price;
    private LocalDate purchaseDate;

    // Getters and Setters

    public Long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Long purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public void setCustomer(Customer customer) {
        if (customer != null) {
            this.customerId = customer.getCustomerId();
        } else {
            this.customerId = null;
        }
    }
}
