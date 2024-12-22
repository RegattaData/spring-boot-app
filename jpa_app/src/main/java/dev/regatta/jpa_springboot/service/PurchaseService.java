package dev.regatta.jpa_springboot.service;

import dev.regatta.jpa_springboot.entity.Purchase;
import dev.regatta.jpa_springboot.repository.PurchaseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PurchaseService {

    @Autowired private PurchaseRepository purchaseRepository;

    public List<Purchase> findAll() { return purchaseRepository.findAll(); }

    public Optional<Purchase> findOne(Long id) {
        return purchaseRepository.findById(id);
    }

    public List<Purchase> findByCustomer(Long customerId) {
        return purchaseRepository.findByCustomerCustomerId(customerId);
    }

    @Transactional
    public Purchase create(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    @Transactional
    public Purchase update(Long id, Purchase purchaseDetails) {
        Purchase purchase = purchaseRepository.findById(id).orElseThrow(
            () -> new RuntimeException("Purchase not found with id " + id));

        if (purchaseDetails.getItem() != null) {
            purchase.setItem(purchaseDetails.getItem());
        }
        if (purchaseDetails.getPrice() != null) {
            purchase.setPrice(purchaseDetails.getPrice());
        }
        if (purchaseDetails.getPurchaseDate() != null) {
            purchase.setPurchaseDate(purchaseDetails.getPurchaseDate());
        }
        if (purchaseDetails.getCustomer() != null) {
            purchase.setCustomer(purchaseDetails.getCustomer());
        }

        return purchaseRepository.save(purchase);
    }

    @Transactional
    public void delete(Long id) {
        Purchase purchase = purchaseRepository.findById(id).orElseThrow(
            () -> new RuntimeException("Purchase not found with id " + id));
        purchaseRepository.delete(purchase);
    }
}
