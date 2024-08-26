package com.customer.demo;

import java.io.FileReader;
import java.io.IOException;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;


@SpringBootApplication
public class DemoApplication {

	// Path to the CSV file containing customer data
	private static final String CSV_FILE_PATH = "customertest.csv";

	// URL of the API endpoint to save customer data
	private static final String POST_URL = "http://localhost:8080/api/customers/saveCustomer";

	public static void main(String[] args) {

		SpringApplication.run(DemoApplication.class, args);


		CSVReader reader; 

		try { 
			// Initialise the CSV reader and process the file
			reader = new CSVReader(new FileReader(CSV_FILE_PATH));


			String[] nextLine;

			RestTemplate restTemplate = new RestTemplate();

			// Iterate through each line of the CSV file
			while ((nextLine = reader.readNext()) != null) { 
				// Build a CustomerDTO object from the CSV data
				CustomerDTO customerDTO = CustomerDTO.builder()
						.customerRef(nextLine[0])
						.customerName(nextLine[1])
						.addressLine1(nextLine[2])
						.addressLine2(nextLine[3]) 
						.town(nextLine[4]) 
						.county(nextLine[5]) 
						.country(nextLine[6])
						.postcode(nextLine[7]).build();

				// Send a POST request to save the customer data
				restTemplate.postForObject(POST_URL, customerDTO, String.class); }

			reader.close();

		} catch (IOException | CsvValidationException | RestClientException e) {
			e.printStackTrace(); }

	}

}
