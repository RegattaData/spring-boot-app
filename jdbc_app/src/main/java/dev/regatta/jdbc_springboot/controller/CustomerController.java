package dev.regatta.jdbc_springboot.controller;

import dev.regatta.jdbc_springboot.entity.Customer;
import dev.regatta.jdbc_springboot.entity.Purchase;
import dev.regatta.jdbc_springboot.service.CustomerService;
import dev.regatta.jdbc_springboot.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.github.javafaker.Faker;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PurchaseService purchaseService;

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable Long id) {
        return customerService.findOne(id);
    }

    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.create(customer);
    }

    @PutMapping("/{id}")
    public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
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
    public String populateCustomersAndPurchases() {
        try {
            int numberOfCustomers = 1000;
            int bulkSize = 100;
            int batches = numberOfCustomers / bulkSize;
            int remaining = numberOfCustomers % bulkSize;

            long customerIdCounter = 1L;
            long purchaseIdCounter = 1L;

            Faker faker = new Faker();
            Random random = new Random();

            List<Purchase> allPurchases = new ArrayList<>();

            // Generate and collect customers and their purchases in batches
            for (int b = 0; b < batches; b++) {
                List<Customer> customers = generateCustomersWithPurchases(
                        bulkSize, faker, random, customerIdCounter, purchaseIdCounter);
                // Perform bulk insertion for customers
                customerService.createAll(customers);

                for (Customer customer: customers) {
                    allPurchases.addAll(customer.getPurchases());
                }

                // Perform bulk insertion for purchases
                purchaseService.createAll(allPurchases);
                allPurchases.clear();
                customerIdCounter += bulkSize;
                purchaseIdCounter += customers.stream().mapToInt(c -> c.getPurchases().size()).sum();
            }

            // Handle remaining customers
            if (remaining > 0) {
                List<Customer> customers = generateCustomersWithPurchases(
                        remaining, faker, random, customerIdCounter, purchaseIdCounter);

                for (Customer customer : customers) {
                    allPurchases.addAll(customer.getPurchases());
                }

                // Perform bulk insertion for customers
                customerService.createAll(customers);

                // Perform bulk insertion for purchases
                purchaseService.createAll(allPurchases);
            }

            return numberOfCustomers +
                    " customers with purchases have been successfully added to the database.";
        } catch (Exception e) {
            return "An error occurred during the population process: " + e.getMessage();
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
    private List<Customer> generateCustomersWithPurchases(int count, Faker faker, Random random,
                                             long customerIdStart, long purchaseIdStart) {
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
                LocalDate purchaseDate = LocalDate.now().minusDays(random.nextInt(730));
                purchase.setPurchaseDate(purchaseDate);
                // Random price between $10 and $1000 with 2 decimal places
                BigDecimal price = BigDecimal.valueOf(faker.number().randomDouble(2, 10, 1000));
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
