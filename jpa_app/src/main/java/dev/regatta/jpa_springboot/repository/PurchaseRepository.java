package dev.regatta.jpa_springboot.repository;

import dev.regatta.jpa_springboot.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByCustomerCustomerId(Long customerId);
}
