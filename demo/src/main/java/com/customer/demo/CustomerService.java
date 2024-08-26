package com.customer.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.customer.demo.CustomerNotFoundException;




@Service
@Transactional
public class CustomerService {

	@Autowired
	private final CustomerRepository customerRepository;


	public CustomerService (CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

    // Retrieve customer data by customer reference
	public CustomerDTO getCustomerDTOById (String id) {
		Customer customerEntity = customerRepository.findByCustomerRef(id);

		if (customerEntity == null) {
			throw new CustomerNotFoundException("Customer not found with id: " + id);
		}
        
		// Map Customer entity to a CustomerDTO and return it
		return CustomerDTO.builder()
				.customerRef(customerEntity.getCustomerRef())
				.customerName(customerEntity.getCustomerName())
				.addressLine1(customerEntity.getAddressLine1())
				.addressLine2(customerEntity.getAddressLine2())
				.town(customerEntity.getTown())
				.country(customerEntity.getCounty())
				.county(customerEntity.getCountry())
				.postcode(customerEntity.getPostcode())
				.build();

	}

    // Save customer data to the repository
	public void saveCustomer(CustomerDTO customerDTO) {

		try {
            // Map CustomerDTO to a Customer entity and save it
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

			customerRepository.save(customer);

		} catch (Exception e) {
			throw new RuntimeException("Failed to save customer: " + customerDTO.getCustomerRef(), e);
		}
	}

}