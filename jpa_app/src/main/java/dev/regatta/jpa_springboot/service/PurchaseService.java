package dev.regatta.jpa_springboot.service;

import dev.regatta.jpa_springboot.entity.Purchase;
import dev.regatta.jpa_springboot.repository.PurchaseRepository;
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

    public Purchase create(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    public Purchase update(Long id, Purchase purchase) {
      Purchase existing = purchaseRepository.findById(id).orElseThrow(
          () -> new RuntimeException("Purchase not found with id " + id));
      existing.setItem(purchase.getItem());
      existing.setPrice(purchase.getPrice());
      existing.setPurchaseDate(purchase.getPurchaseDate());
      existing.setCustomer(purchase.getCustomer());
      return purchaseRepository.save(existing);
    }

    public void delete(Long id) { purchaseRepository.deleteById(id); }
}
