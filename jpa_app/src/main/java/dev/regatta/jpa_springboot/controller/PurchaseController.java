package dev.regatta.jpa_springboot.controller;

import dev.regatta.jpa_springboot.dto.PurchaseDto;
import dev.regatta.jpa_springboot.entity.Customer;
import dev.regatta.jpa_springboot.entity.Purchase;
import dev.regatta.jpa_springboot.service.CustomerService;
import dev.regatta.jpa_springboot.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

  @Autowired private PurchaseService purchaseService;

  @Autowired private CustomerService customerService;

  /**
   * Retrieve all purchases.
   * URL: GET /purchases
   */
  @GetMapping
  public List<Purchase> findAll() {
    return purchaseService.findAll();
    }

    /**
     * Retrieve a specific purchase by ID.
     * URL: GET /purchases/{id}
     */
    @GetMapping("/{id}")
    public Purchase findOne(@PathVariable Long id) {
      return purchaseService.findOne(id).orElseThrow(
          ()
              -> new ResponseStatusException(
                  HttpStatus.NOT_FOUND, "Purchase not found with id " + id));
    }

    /**
     * Retrieve all purchases for a specific customer.
     * URL: GET /purchases/customer/{customerId}
     */
    @GetMapping("/customer/{customerId}")
    public List<Purchase> findByCustomer(@PathVariable Long customerId) {
        return purchaseService.findByCustomer(customerId);
    }

    /**
     * Create a new purchase.
     * URL: POST /purchases
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Purchase create(@RequestBody PurchaseDto purchaseDto) {
      Purchase purchase = new Purchase();
      purchase.setPurchaseId(purchaseDto.getPurchaseId());
      purchase.setItem(purchaseDto.getItem());
      purchase.setPrice(purchaseDto.getPrice());
      purchase.setPurchaseDate(purchaseDto.getPurchaseDate());

      // Fetch the Customer entity based on customerId
      Customer customer =
          customerService.findOne(purchaseDto.getCustomerId())
              .orElseThrow(()
                               -> new ResponseStatusException(
                                   HttpStatus.NOT_FOUND,
                                   "Customer not found with id " +
                                       purchaseDto.getCustomerId()));
      purchase.setCustomer(customer);

      return purchaseService.create(purchase);
    }

    /**
     * Update an existing purchase.
     * URL: PUT /purchases/{id}
     */
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
                                   -> new ResponseStatusException(
                                       HttpStatus.NOT_FOUND,
                                       "Customer not found with id " +
                                           purchaseDto.getCustomerId()));
          purchase.setCustomer(customer);
        }

        return purchaseService.update(id, purchase);
    }

    /**
     * Delete a purchase by ID.
     * URL: DELETE /purchases/{id}
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
      purchaseService.delete(id);
    }
}
