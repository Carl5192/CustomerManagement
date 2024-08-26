package com.customer.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test to ensure that a customer is successfully retrieved
     * by its ID and mapped to a CustomerDTO.
     */
    @Test
    public void testGetCustomerDTOById_Success() {
        String customerRef = "123";
        Customer customer = Customer.builder()
                .customerRef(customerRef)
                .customerName("Carl Carver")
                .addressLine1("50 Spital lane")
                .addressLine2("Spital")
                .town("Chesterfield")
                .county("England")
                .country("Derbyshire")
                .postcode("S410HJ")
                .build();

        // Mocking the repository to return a customer
        when(customerRepository.findByCustomerRef(customerRef)).thenReturn(customer);

        // Calling the service method
        CustomerDTO result = customerService.getCustomerDTOById(customerRef);

        // Asserting the results
        assertNotNull(result);
        assertEquals(customerRef, result.getCustomerRef());
        assertEquals("Carl Carver", result.getCustomerName());
    }

    /**
     * Test to verify that a CustomerNotFoundException is thrown
     * when the customer with the provided ID does not exist.
     */
    @Test
    public void testGetCustomerDTOById_NotFound() {
        String customerRef = "999";
        
        // Mocking the repository to return null, simulating a missing customer
        when(customerRepository.findByCustomerRef(customerRef)).thenReturn(null);

        // Asserting that the exception is thrown with the correct message
        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
            customerService.getCustomerDTOById(customerRef);
        });

        
        assertEquals("Customer not found with id: " + customerRef, exception.getMessage());
    }

    /**
     * Test to verify that a customer is successfully saved.
     */
    @Test
    public void testSaveCustomer_Success() {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .customerRef("123")
                .customerName("Carl Carver")
                .addressLine1("50 Spital lane")
                .addressLine2("Spital")
                .town("Chesterfield")
                .county("England")
                .country("Derbyshire")
                .postcode("S410HJ")
                .build();

        Customer customer = Customer.builder()
                .customerRef(customerDTO.getCustomerRef())
                .customerName(customerDTO.getCustomerName())
                .addressLine1(customerDTO.getAddressLine1())
                .addressLine2(customerDTO.getAddressLine2())
                .town(customerDTO.getTown())
                .county(customerDTO.getCounty())
                .country(customerDTO.getCountry())
                .postcode(customerDTO.getPostcode())
                .build();

        // Calling the service method to save the customer
        customerService.saveCustomer(customerDTO);

        // Verifying that the repository's save method was called once
        verify(customerRepository, times(1)).save(customer);
    }

    /**
     * Test to verify that a RuntimeException is thrown with an appropriate message
     * when saving a customer fails.
     */
    @Test
    public void testSaveCustomer_Failure() {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .customerRef("999")
                .customerName("Ron Dalee")
                .addressLine1("123 Patrick Street")
                .addressLine2("Burton")
                .town("Test Town")
                .county("South Yorkshire")
                .country("England")
                .postcode("S410TH")
                .build();

        // Simulate a failure by throwing an exception when the repository's save method is called
        doThrow(new RuntimeException("Failed to save customer")).when(customerRepository).save(org.mockito.ArgumentMatchers.any(Customer.class));

        // Assert that the RuntimeException is thrown with the correct message
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            customerService.saveCustomer(customerDTO);
        });

        assertEquals("Failed to save customer: " + customerDTO.getCustomerRef(), exception.getMessage());
    }
}
