package dev.regatta.jpa_springboot.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
@Entity
@Table(name = "purchases")
public class Purchase {
    @Id @Column(name = "purchaseid") private Long purchaseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerid", nullable = false)
    @JsonBackReference
    private Customer customer;

    @Column(length = 100) private String item;

    @Column(precision = 10, scale = 2) private BigDecimal price;

    @Column(name = "purchasedate") private LocalDate purchaseDate;
}
