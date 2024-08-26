# Customer Management System

## Overview

The Customer Management System is a Spring Boot application designed to handle customer data. It provides endpoints to save customer information and retrieve customer details by reference. It includes exception handling, data persistence, and integration with a CSV file to import customer data.

## Project Structure

### 1. Entities and DTOs

#### Customer Entity
- **Path:** `com.customer.demo.Customer`
- **Purpose:** Represents the customer data model in the database.
- **Fields:**
  - `customerRef`: Unique identifier for the customer.
  - `customerName`: Name of the customer.
  - `addressLine1`: Primary address line.
  - `addressLine2`: Secondary address line.
  - `town`: Town or city of the customer.
  - `county`: County of the customer.
  - `country`: Country of the customer.
  - `postcode`: Postal code of the customer.

#### CustomerDTO
- **Path:** `com.customer.demo.CustomerDTO`
- **Purpose:** Data Transfer Object used to transfer customer data between layers.
- **Fields:** Same as `Customer` entity.

### 2. Repository

#### CustomerRepository
- **Path:** `com.customer.demo.CustomerRepository`
- **Purpose:** Interface for data access operations related to `Customer` entity.
- **Methods:**
  - `findByCustomerRef(String customerRef)`: Finds a customer by their reference.

### 3. Service Layer

#### CustomerService
- **Path:** `com.customer.demo.CustomerService`
- **Purpose:** Contains business logic for customer operations.
- **Methods:**
  - `getCustomerDTOById(String id)`: Retrieves a `CustomerDTO` by customer reference.
  - `saveCustomer(CustomerDTO customerDTO)`: Saves a customer into the database.

### 4. Controller

#### CustomerController
- **Path:** `com.customer.demo.CustomerController`
- **Purpose:** Handles HTTP requests for customer operations.
- **Endpoints:**
  - `POST /api/customers/saveCustomer`: Saves a new customer.
  - `GET /api/customers/{customerRef}`: Retrieves a customer by reference.

### 5. Exception Handling

#### GlobalExceptionHandler
- **Path:** `com.customer.demo.GlobalExceptionHandler`
- **Purpose:** Provides global exception handling for the application.
- **Handlers:**
  - `handleCustomerNotFoundException(CustomerNotFoundException ex)`: Returns 404 Not Found.
  - `handleGenericException(Exception ex)`: Returns 500 Internal Server Error.

### 6. Error Response

#### ErrorResponse
- **Path:** `com.customer.demo.ErrorResponse`
- **Purpose:** Standard format for error responses.
- **Fields:**
  - `statusCode`: HTTP status code.
  - `message`: Error message.

### 7. CSV Import

#### DemoApplication
- **Path:** `com.customer.demo.DemoApplication`
- **Purpose:** Main application entry point. Also processes a CSV file to import customer data.
- **Functionality:**
  - Reads customer data from `customertest.csv`.
  - Sends POST requests to save each customer to the API.

## Testing

### Controller Tests
- **Test class:** `CustomerControllerTest`
- **Purpose:** Tests the HTTP endpoints of `CustomerController` using `MockMvc`.
- **Tests:**
  - `testSaveCustomer_Success`: Verifies that saving a customer via POST request succeeds.
  - `testSaveCustomer_Failure`: Verifies that saving a customer with a failure in the service layer results in a 500 error.
  - `testGetCustomer_Success`: Verifies that retrieving a customer by reference succeeds.
  - `testGetCustomer_NotFound`: Verifies that retrieving a non-existent customer results in a 404 error.

### Service Tests
- **Test class:** `CustomerServiceTest`
- **Purpose:** Tests the business logic of `CustomerService` using Mockito and JUnit.
- **Tests:**
  - `testGetCustomerDTOByID_Success`: Verifies that retrieving a customer by ID succeeds.
  - `testGetCustomerDTOByID_NotFound`: Verifies that incorrect customer ID throws an error relating to the failure.
  - `testSaveCustomer_Success`: Verifies that a customer is saved to the DB successfully.
  - `testSaveCustomer_Failure`: Verifies that saving a customer with a failure in the service layer results in a Runtime Exception being thrown.
