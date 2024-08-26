package com.customer.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;
    
    @Autowired
    private MockMvc mockMvc;
    
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
        		.setControllerAdvice(new GlobalExceptionHandler())
        		.build();

    }

    /**
     * Test to verify that the controller works correctly when the request is made.
     */
    @Test
    public void testSaveCustomer_Success() throws Exception {
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

        // Perform a POST request to the controller's /saveCustomer endpoint
        mockMvc.perform(post("/api/customers/saveCustomer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isOk()) // Expect 200 OK status
                .andExpect(content().string("Customer saved successfully")); // Expect success message

        // Verify that the service's saveCustomer method was called once
        verify(customerService, times(1)).saveCustomer(customerDTO);
    }
    
    /**
     * Test to verify that the controller correctly handles a failure when saving a customer.
     */
    @Test
    public void testSaveCustomer_Failure() throws Exception {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .customerRef("99889")
                .customerName("Failed Customer")
                .addressLine1("123 Failure St")
                .addressLine2("Failtown")
                .town("Failville")
                .county("Failshire")
                .country("Failand")
                .postcode("FAIL123")
                .build();

        // Simulate a failure by throwing a RuntimeException from the service's saveCustomer method
        doThrow(new RuntimeException("Failed to save customer: " + customerDTO.getCustomerRef()))
                .when(customerService).saveCustomer(customerDTO);

        // Perform a POST request to the controller's /saveCustomer endpoint
        mockMvc.perform(post("/api/customers/saveCustomer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isInternalServerError()) // Expect 500 INTERNAL_SERVER_ERROR status
                .andExpect(content().string("An error occurred while saving the customer")); // Expect error message
    }

    /**
     * Test to verify that the controller expects the correct data and HTTP status.
     */
    @Test
    public void testGetCustomer_Success() throws Exception {
        String customerRef = "123";
        CustomerDTO customerDTO = CustomerDTO.builder()
                .customerRef(customerRef)
                .customerName("Carl Carver")
                .addressLine1("50 Spital lane")
                .addressLine2("Spital")
                .town("Chesterfield")
                .county("England")
                .country("Derbyshire")
                .postcode("S410HJ")
                .build();

        when(customerService.getCustomerDTOById(customerRef)).thenReturn(customerDTO);

        // Perform a GET request to the controller's /{customerRef} endpoint
        mockMvc.perform(get("/api/customers/{customerRef}", customerRef))
                .andExpect(status().isOk()) // Expect 200 OK status
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerRef").value(customerDTO.getCustomerRef()))
                .andExpect(jsonPath("$.customerName").value(customerDTO.getCustomerName()))
                .andExpect(jsonPath("$.addressLine1").value(customerDTO.getAddressLine1()))
                .andExpect(jsonPath("$.addressLine2").value(customerDTO.getAddressLine2()))
                .andExpect(jsonPath("$.town").value(customerDTO.getTown()))
                .andExpect(jsonPath("$.county").value(customerDTO.getCounty()))
                .andExpect(jsonPath("$.country").value(customerDTO.getCountry()))
                .andExpect(jsonPath("$.postcode").value(customerDTO.getPostcode()));
    }
    
    /**
     * Test to verify that the controller correctly handles a failure when finding a customer
     */
    @Test
    public void testGetCustomer_NotFound() throws Exception {
        String customerRef = "999";
        when(customerService.getCustomerDTOById(customerRef))
                .thenThrow(new CustomerNotFoundException("Customer not found with id: " + customerRef));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/{customerRef}", customerRef))
                .andExpect(status().isNotFound()) // Expect 404 NOT_FOUND status
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Customer not found with id: " + customerRef));
    }
}

