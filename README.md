<div align="center">
  <img src="assets/SpringBoot+Regatta.png" alt="Banner" title="Banner" style="width:75%;">
</div>

# Spring Boot Integration with Regatta Database

Welcome to the **Spring Boot / Regatta Integration** repository! This application demonstrates how to integrate a Spring Boot backend with the Regatta Database using **Hibernate (JPA)** or **JDBC** for data access.
In this example app we simulate a Sports store with 2 tables:
- *Customers*
- *Purchases*

The app provdies RESTful APIs to manage customers and their purchases.

## Overview

- **Purpose:** Demonstrate the integration of Spring Boot with Regatta Database.
- **Technologies Used:** Spring Boot, Hibernate (JPA), JDBC, Regatta Database.
- **Functionality:** Manage customers and their purchases, including creating, reading, updating, and deleting records.
- **Two Modes:**
  - **JPA Mode:** Located in the `jpa_app` directory.
  - **JDBC Mode:** Located in the `jdbc_app` directory.

## Data Models

This application works with two primary entities:

1. **Customer**
2. **Purchase**

There is a **foreign key relationship** between the entities, where each **Purchase** is associated with a **Customer**.

## Prerequisites

Before getting started, ensure you have the following:

1. **Java Development Kit (JDK) 17 or higher**
2. **Maven** installed on your machine
3. **Regatta Platform Account:** To create and manage your cluster
4. **Regatta JDBC** and **Regatta Hibernate** JAR packages

If you do not have access to the Regatta Platform or required drivers, please contact Regatta Support for assistance.

## Setup Instructions

### 1. Create a Regatta Cluster
1. **Log in to the [Regatta Cloud](https://cloud.regatta.dev/).**
   - Use your credentials or Google SSO if applicable.
2. **Create a New Cluster:**
   - Click "**CREATE NEW CLUSTER**".
   - Configure the cluster:
     - **Cluster Name:** Enter a meaningful name.
     - **Cluster Type:** Select the appropriate type.
   - Click "**Confirm**".
3. **Wait for the Cluster to Start Running.**

**Note:** If you are using on-premises deployment, you can skip this step.

### 2. Add Dependencies
If you're using the standalone shaded driver, make sure Regatta native client
libraries are already available in your system path. The following command
installs the full shaded JDBC driver locally:
```shell
mvn install:install-file \
  -Dfile=/path/to/drivers/regatta-jdbc-1.5.0-shaded_full.jar \
  -DgroupId=dev.regatta \
  -DartifactId=jdbc1 \
  -Dversion=1.5.0 \
  -Dpackaging=jar
```

#### a. JPA Mode Dependencies

Add the Regatta JARs as dependencies in your `pom.xml` for JPA mode:

```xml
<dependencies>
    <!-- Regatta JDBC Driver -->
    <dependency>
        <groupId>dev.regatta</groupId>
        <artifactId>regatta-jdbc</artifactId>
        <version>1.5.0</version>
    </dependency>

    <!-- Regatta Hibernate Integration -->
    <dependency>
        <groupId>dev.regatta</groupId>
        <artifactId>regatta-hibernate</artifactId>
        <version>1.1.0</version>
    </dependency>
</dependencies>
```

#### b. JDBC Mode Dependencies

Add only the JDBC dependency for JDBC mode:

```xml
<dependencies>
    <!-- Regatta JDBC Driver -->
    <dependency>
        <groupId>dev.regatta</groupId>
        <artifactId>regatta-jdbc</artifactId>
        <version>1.5.0</version>
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
spring.datasource.driver-class-name=dev.regatta.jdbc1.Driver

# Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=dev.regatta.hibernate.RegattaDialect
```

**Regatta Limitation for Property Configuration:**
When working with Spring Boot and Regatta, the cluster URL, username, and password must be explicitly specified in one of the following configuration files:

- `application.properties`
- `application.yaml`
- `application.yml`
- `hibernate.cfg.xml`

**Important:** These properties cannot be stored in profile-specific configuration files (e.g., `application-dev.properties`). Ensure they are added directly to the main configuration files.

#### b. JDBC Mode Configuration

Update the `application.properties` file in the `jdbc_app` directory:

```properties
# Database Configuration
spring.datasource.url=jdbc:regatta:<CLUSTER_IP>:<CLUSTER_PORT>
spring.datasource.username=<YOUR_USERNAME>
spring.datasource.password=<YOUR_PASSWORD>
spring.datasource.driver-class-name=dev.regatta.jdbc1.Driver
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
1,000 customers with purchases have been successfully added to the database.
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

*© 2025 Regatta Team*

