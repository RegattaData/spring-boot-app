package dev.regatta.jpa_springboot.controller;

import dev.regatta.jpa_springboot.entity.Customer;
import dev.regatta.jpa_springboot.entity.Purchase;
import dev.regatta.jpa_springboot.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.github.javafaker.Faker;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired private CustomerService customerService;

    @GetMapping
    public List<Customer> findAll() {
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    public Customer findOne(@PathVariable Long id) {
        return customerService.findOne(id).orElseThrow(
            () -> new RuntimeException("Customer not found with id " + id));
    }

    @PostMapping
    public Customer create(@RequestBody Customer customer) {
        return customerService.create(customer);
    }

    @PutMapping("/{id}")
    public Customer update(@PathVariable Long id,
                           @RequestBody Customer customer) {
        return customerService.update(id, customer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        boolean isDeleted = customerService.deleteCustomerById(id);
        if (isDeleted) {
            String message = "Customer with ID " + id + " deleted successfully.";
            return ResponseEntity.ok(message);
        } else {
            String errorMessage = "Customer with ID " + id + " not found.";
            return ResponseEntity.status(404).body(errorMessage);
        }
    }

    /**
     * Endpoint to populate the database with 1000 customers for testing.
     * Each customer will have between 1 to 5 purchases.
     * URL: POST /customers/populate
     */
    @PostMapping("/populate")
    public String populateCustomers() {
      try {
        int numberOfCustomers = 1000;
        int batchSize = 100;
        int batches = numberOfCustomers / batchSize;
        int remaining = numberOfCustomers % batchSize;

        long customerIdCounter = 1L;
        long purchaseIdCounter = 1L;

        Faker faker = new Faker();
        Random random = new Random();

        for (int b = 0; b < batches; b++) {
          List<Customer> customers = generateCustomers(
              batchSize, faker, random, customerIdCounter, purchaseIdCounter);
          customerIdCounter += batchSize;
          purchaseIdCounter +=
              customers.stream().mapToInt(c -> c.getPurchases().size()).sum();
          customerService.createAll(customers);
        }

        // Handle remaining customers
        if (remaining > 0) {
          List<Customer> customers = generateCustomers(
              remaining, faker, random, customerIdCounter, purchaseIdCounter);
          customerService.createAll(customers);
        }

        return numberOfCustomers +
            " customers with purchases have been successfully added to the database.";
      } catch (Exception e) {
        return "An error occurred during the population process: " +
            e.getMessage();
      }
    }

    /**
     * Generates a list of customers with random data and purchases.
     *
     * @param count the number of customers to generate
     * @param faker the Faker instance for generating data
     * @param random the Random instance for generating random numbers
     * @param customerIdStart the starting customer ID
     * @param purchaseIdStart the starting purchase ID
     * @return a list of generated customers
     */
    private List<Customer> generateCustomers(int count, Faker faker,
                                             Random random,
                                             long customerIdStart,
                                             long purchaseIdStart) {
      List<Customer> customers = new ArrayList<>(count);

      for (int i = 0; i < count; i++) {
        Customer customer = new Customer();
        customer.setCustomerId(customerIdStart++);
        String rawName = faker.name().fullName();
        String sanitizedName = rawName.replace("'", "");
        customer.setName(sanitizedName);
        customer.setEmail(faker.internet().emailAddress());
        String phone = faker.phoneNumber().phoneNumber();
        customer.setPhone(phone.length() > 12 ? phone.substring(0, 12) : phone);

        // Generate between 1 to 5 purchases for this customer
        int numberOfPurchases = random.nextInt(5) + 1;
        for (int p = 0; p < numberOfPurchases; p++) {
          Purchase purchase = new Purchase();
          purchase.setPurchaseId(purchaseIdStart++);
          // Random purchase date within the last 2 years
          LocalDate purchaseDate =
              LocalDate.now().minusDays(random.nextInt(730));
          purchase.setPurchaseDate(purchaseDate);
          // Random price between $10 and $1000 with 2 decimal places
          BigDecimal price =
              BigDecimal.valueOf(faker.number().randomDouble(2, 10, 1000));
          purchase.setPrice(price);
          purchase.setItem(faker.commerce().productName());
          purchase.setCustomer(customer);

          customer.getPurchases().add(purchase);
        }

        customers.add(customer);
      }

      return customers;
    }
}
