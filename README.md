<div align="center">
  <img src="assets/banner.jpg" alt="Banner" title="Banner">
</div>
# Spring Boot Integration with Regatta Database

Welcome to the **Spring Boot Regatta Integration** repository! This application demonstrates how to integrate a Spring Boot backend with the Regatta Database using **Hibernate (JPA)** or **JDBC** for data access. It provides RESTful APIs to manage customers and their purchases.

## Overview

- **Purpose:** Demonstrate the integration of Spring Boot with Regatta Database.
- **Technologies Used:** Spring Boot, Hibernate (JPA), JDBC, Regatta Database, Java Faker, Lombok.
- **Functionality:** Manage customers and their purchases, including creating, reading, updating, and deleting records.
- **Two Modes:**
  - **JPA Mode:** Located in the `jpa_app` directory.
  - **JDBC Mode:** Located in the `jdbc_app` directory.

## Data Models

This application works with two primary entities:

1. **Customer**
2. **Purchase**

There is a **foreign key relationship** between them, where each **Purchase** is associated with a **Customer**.

## Prerequisites

Before getting started, ensure you have the following:

1. **Java Development Kit (JDK) 17 or higher**
2. **Maven** installed on your machine
3. **Regatta Database Access:**
   - Contact the Regatta team to obtain the **Regatta JDBC** and **Regatta Hibernate** JAR packages.

4. **Regatta Platform Account:** To create and manage database clusters.

## Setup Instructions

### 1. Create a Regatta Cluster

To connect your Spring Boot application to the Regatta Database, first set up a Regatta cluster.

1. **Access Regatta Platform:**
   - Navigate to the [Regatta Platform](https://cloud.regatta.dev/) and log in with credentials received from Regatta.
   Note that if the email being used is one associated with Google and you are already signed into Google, you may leverage the Single-Sign-On with the Google option.

2. **Create a New Cluster:**
   - Click on the "**+ CREATE NEW CLUSTER**" button.
   - **Configure Cluster:**
     - **Cluster Name:** Enter a meaningful name for your cluster.
     - **Cluster Type:** Select the appropriate type based on your needs.
   - Click "**Confirm**" to initiate cluster creation.

3. **Wait for Cluster to Run:**
   - Monitor the cluster status. Wait until it changes from "**Starting**" to "**Running**".

### 2. Add Dependencies

#### a. JPA Mode Dependencies

Add the Regatta JARs as dependencies in your `pom.xml` for JPA mode:

```xml
<dependencies>
    <!-- Regatta JDBC Driver -->
    <dependency>
        <groupId>dev.regatta</groupId>
        <artifactId>regatta-jdbc</artifactId>
        <version>1.3.7</version>
    </dependency>

    <!-- Regatta Hibernate Integration -->
    <dependency>
        <groupId>dev.regatta</groupId>
        <artifactId>regatta-hibernate</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

#### b. JDBC Mode Dependencies

Add only the JDBC dependency for the JDBC mode:

```xml
<dependencies>
    <!-- Regatta JDBC Driver -->
    <dependency>
        <groupId>dev.regatta</groupId>
        <artifactId>regatta-jdbc</artifactId>
        <version>1.3.7</version>
    </dependency>
</dependencies>
```

### **3. Configure Application**

#### a. JPA Mode Configuration

Update the `application.properties` file in the `jpa_app` directory:

```properties
# Database Configuration
spring.datasource.url=jdbc:regatta:<CLUSTER_IP>:<CLUSTER_PORT>
spring.datasource.username=<YOUR_USERNAME>
spring.datasource.password=<YOUR_PASSWORD>
spring.datasource.driver-class-name=dev.regatta.jdbc.Driver

# Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=dev.regatta.hibernate.RegattaDialect
```

Additionally, ensure the `hibernate.cfg.xml` file is properly set up:

```xml
<?xml version='1.0' encoding='utf-8'?>
<!DOCTYPE hibernate-configuration PUBLIC
          "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
          "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <session-factory>
        <property name="connection.driver_class">dev.regatta.jdbc.Driver</property>
        <property name="connection.url">jdbc:regatta:<CLUSTER_IP>:<CLUSTER_PORT></property>
        <property name="connection.username"><YOUR_USERNAME></property>
        <property name="connection.password"><YOUR_PASSWORD></property>
        <property name="dialect">dev.regatta.hibernate.RegattaDialect</property>
    </session-factory>
</hibernate-configuration>
```

#### b. JDBC Mode Configuration

Update the `application.properties` file in the `jdbc_app` directory:

```properties
# Database Configuration
spring.datasource.url=jdbc:regatta:<CLUSTER_IP>:<CLUSTER_PORT>
spring.datasource.username=<YOUR_USERNAME>
spring.datasource.password=<YOUR_PASSWORD>
spring.datasource.driver-class-name=dev.regatta.jdbc.Driver
```

For JDBC mode, you do not need Hibernate-specific configurations such as `spring.jpa.*` properties.

### 4. Build the Application

Navigate to the root directory of the respective project (`jpa_app` or `jdbc_app`) and run:

```bash
mvn clean install
```

This command will compile the project, run tests, and package the application.

### 5. Run the Application

After a successful build, start the Spring Boot application using:

```bash
mvn spring-boot:run
```

Ensure you run the command in the appropriate directory (`jpa_app` or `jdbc_app`).

## API Endpoints

The application exposes the following RESTful APIs to manage customers and their purchases.

### Populate Customers and Purchases

**Description:** Populate the database with 1000 customers, each having 1 to 5 purchases.

**Command:**

```bash
curl -X POST http://localhost:8080/customers/populate
```

**Response:**

```
1000 customers with purchases have been successfully added to the database.
```

### Retrieve a Customer by ID

**Description:** Fetch details of a specific customer using their unique ID.

**Command:**

```bash
curl -X GET http://localhost:8080/customers/1
```

**Response:**
> **⚠️ Note:** The customers and purchases are auto-generated by the data population process. This is only an example.
```json
{
  "customerId": 1,
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phone": "123-456-7890",
  "purchases": [
    {
      "purchaseId": 101,
      "item": "Laptop",
      "price": 999.99,
      "purchaseDate": "2024-04-15"
    }
  ]
}
```

### Create a New Customer

**Description:** Add a new customer to the database.

**Command:**

```bash
curl -X POST http://localhost:8080/customers \
     -H "Content-Type: application/json" \
     -d '{
           "customerId": 2100000,
           "name": "John Doe",
           "email": "john.doe@example.com",
           "phone": "123-456-7890"
         }'
```

**Response:**
> **⚠️ Note:** The customers and purchases are auto-generated by the data population process. This is only an example.
```json
{
  "customerId": 2100000,
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phone": "123-456-7890",
  "purchases": []
}
```

### Update an Existing Customer

**Description:** Modify details of an existing customer.

**Command:**

```bash
curl -X PUT http://localhost:8080/customers/2100000 \
     -H "Content-Type: application/json" \
     -d '{
           "name": "Martin Doe",
           "email": "martin.doe@example.com",
           "phone": "098-765-4321"
         }'
```

**Response:**

```json
{
  "customerId": 2100000,
  "name": "Martin Doe",
  "email": "martin.doe@example.com",
  "phone": "098-765-4321",
  "purchases": []
}
```

### Delete a Customer

**Description:** Remove a customer from the database using their ID.

**Command:**

```bash
curl -X DELETE http://localhost:8080/customers/2100000
```

**Response:**

```
Customer with ID 2100000 deleted successfully.
```

---

*© 2024 Regatta Team*

