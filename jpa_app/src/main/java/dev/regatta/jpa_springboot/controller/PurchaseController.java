package dev.regatta.jpa_springboot.controller;

import dev.regatta.jpa_springboot.dto.PurchaseDto;
import dev.regatta.jpa_springboot.entity.Customer;
import dev.regatta.jpa_springboot.entity.Purchase;
import dev.regatta.jpa_springboot.service.CustomerService;
import dev.regatta.jpa_springboot.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    @Autowired private PurchaseService purchaseService;

    @Autowired private CustomerService customerService;

    @GetMapping
    public List<Purchase> findAll() {
        return purchaseService.findAll();
    }

    @GetMapping("/{id}")
    public Purchase findOne(@PathVariable Long id) {
        return purchaseService.findOne(id).orElseThrow(
            () -> new RuntimeException("Purchase not found with id " + id));
    }

    @GetMapping("/customer/{customerId}")
    public List<Purchase> findByCustomer(@PathVariable Long customerId) {
        return purchaseService.findByCustomer(customerId);
    }

    @PostMapping
    public Purchase create(@RequestBody PurchaseDto purchaseDto) {
        Purchase purchase = new Purchase();
        purchase.setPurchaseId(purchaseDto.getPurchaseId());
        purchase.setItem(purchaseDto.getItem());
        purchase.setPrice(purchaseDto.getPrice());
        purchase.setPurchaseDate(purchaseDto.getPurchaseDate());

        // Fetch the Customer entity based on customerId
        Customer customer =
            customerService.findOne(purchaseDto.getCustomerId())
                .orElseThrow(
                    ()
                        -> new RuntimeException("Customer not found with id " +
                                                purchaseDto.getCustomerId()));
        purchase.setCustomer(customer);

        return purchaseService.create(purchase);
    }

    @PutMapping("/{id}")
    public Purchase update(@PathVariable Long id,
                           @RequestBody PurchaseDto purchaseDto) {
        Purchase purchase = new Purchase();
        purchase.setItem(purchaseDto.getItem());
        purchase.setPrice(purchaseDto.getPrice());
        purchase.setPurchaseDate(purchaseDto.getPurchaseDate());

        if (purchaseDto.getCustomerId() != null) {
            Customer customer =
                customerService.findOne(purchaseDto.getCustomerId())
                    .orElseThrow(()
                                     -> new RuntimeException(
                                         "Customer not found with id " +
                                         purchaseDto.getCustomerId()));
            purchase.setCustomer(customer);
        }

        return purchaseService.update(id, purchase);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        purchaseService.delete(id);
    }
}
