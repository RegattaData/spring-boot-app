package dev.regatta.jdbc_springboot.controller;

import dev.regatta.jdbc_springboot.entity.Customer;
import dev.regatta.jdbc_springboot.entity.Purchase;
import dev.regatta.jdbc_springboot.service.CustomerService;
import dev.regatta.jdbc_springboot.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public List<Purchase> findAll() {
        return purchaseService.findAll();
    }

    @GetMapping("/{id}")
    public Purchase findOne(@PathVariable Long id) {
        Purchase purchase = purchaseService.findOne(id);
        if (purchase == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase not found with id " + id);
        }
        return purchase;
    }

    @GetMapping("/customer/{customerId}")
    public List<Purchase> findByCustomer(@PathVariable Long customerId) {
        if (customerService.findOne(customerId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with id " + customerId);
        }
        return purchaseService.findByCustomer(customerId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Purchase create(@RequestBody Purchase purchase) {
        // Validate customer existence
        Customer customer = customerService.findOne(purchase.getCustomerId());
        if (customer == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Customer not found with id " + purchase.getCustomerId());
        }

        purchase.setCustomerId(customer.getCustomerId());

        return purchaseService.create(purchase);
    }

    @PutMapping("/{id}")
    public Purchase update(@PathVariable Long id, @RequestBody Purchase purchase) {
        // Fetch the existing purchase
        Purchase existingPurchase = purchaseService.findOne(id);
        if (existingPurchase == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase not found with id " + id);
        }

        existingPurchase.setItem(purchase.getItem());
        existingPurchase.setPrice(purchase.getPrice());
        existingPurchase.setPurchaseDate(purchase.getPurchaseDate());

        // Validate customer existence if customerId is provided
        if (purchase.getCustomerId() != null) {
            Customer customer = customerService.findOne(purchase.getCustomerId());
            if (customer == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with id " + purchase.getCustomerId());
            }
            existingPurchase.setCustomerId(purchase.getCustomerId());
        }

        return purchaseService.update(id, existingPurchase);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        Purchase existingPurchase = purchaseService.findOne(id);
        if (existingPurchase == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase not found with id " + id);
        }
        purchaseService.delete(id);
    }
}
