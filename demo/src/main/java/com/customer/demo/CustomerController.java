package com.customer.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.customer.demo.CustomerDTO;
import com.customer.demo.CustomerService;



@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
    	this.customerService = customerService; 
    }
	
    // Endpoint to save customer data
    @PostMapping("/saveCustomer")
	public ResponseEntity<String> saveCustomer(@RequestBody CustomerDTO customerDTO) {
			customerService.saveCustomer(customerDTO);
			return ResponseEntity.ok("Customer saved successfully");
	}
    
    // Endpoint to retrieve customer data by customer reference
    @GetMapping("/{customerRef}")
	public ResponseEntity<CustomerDTO> getCustomer(@PathVariable String customerRef) {
			CustomerDTO customerDTO = customerService.getCustomerDTOById(customerRef);
			return ResponseEntity.ok(customerDTO);
	}
}
