package com.freddyerf.service;

import com.freddyerf.customer.model.Customer;
import com.freddyerf.customer.repository.CustomerRepository;
import com.freddyerf.customer.service.CountryService;
import com.freddyerf.customer.service.CustomerService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;


@QuarkusTest
class CustomerServiceTest {

    @InjectMock
    CustomerRepository customerRepository;

    @InjectMock
    CountryService countryService;

    @Inject
    CustomerService customerService;


    // Tests retrieving all customers successfully
    @Test
    void testGetAllCustomers() {
        List<Customer> customers = List.of(new Customer(), new Customer());
        Mockito.when(customerRepository.listAll()).thenReturn(customers);

        List<Customer> result = customerService.getAllCustomers();

        assertEquals(2, result.size());
    }

    // Verifies behavior when a customer ID is not found
    @Test
    void testGetCustomerByIdNotFound() {
        Mockito.when(customerRepository.findById(1L)).thenReturn(null);

        Customer result = customerService.getCustomerById(1L);

        assertNull(result);
    }


    // Ensures a customer can be created successfully with valid country code
    @Test
    void testCreateCustomerSuccess() {
        Customer mockCustomer = new Customer();
        mockCustomer.setCountry("US");
        Mockito.when(countryService.getDemonymByCountryCode("US")).thenReturn("American");

        customerService.createCustomer(mockCustomer);

        Mockito.verify(customerRepository, Mockito.times(1)).persist(mockCustomer);
        assertEquals("American", mockCustomer.getDemonym());
    }

    // Tests customer creation with an invalid country code
    @Test
    void testCreateCustomerInvalidCountry() {
        Customer mockCustomer = new Customer();
        mockCustomer.setCountry("InvalidCode");
        Mockito.when(countryService.getDemonymByCountryCode("InvalidCode")).thenReturn(null);

        Customer result = customerService.createCustomer(mockCustomer);

        assertNull(result.getDemonym());
    }

    // Verifies updating a non-existent customer results in no change
    @Test
    void testUpdateNonExistentCustomer() {
        Mockito.when(customerRepository.findById(1L)).thenReturn(null);

        Customer updatedCustomer = customerService.updateCustomer(1L, new Customer());

        assertNull(updatedCustomer);
    }

    // Checks deletion behavior for a customer that doesn't exist
    @Test
    void testDeleteNonExistentCustomer() {
        Mockito.when(customerRepository.deleteById(1L)).thenReturn(false);

        boolean result = customerService.deleteCustomer(1L);

        assertFalse(result);
    }

    // Confirms a customer can be updated successfully
    @Test
    void testUpdateExistingCustomerSuccess() {
        Long customerId = 1L;
        Customer existingCustomer = new Customer();
        existingCustomer.setId(customerId);
        Customer updatedCustomer = new Customer();
        updatedCustomer.setEmail("new@example.com");
        updatedCustomer.setCountry("US");

        Mockito.when(customerRepository.findById(customerId)).thenReturn(existingCustomer);
        Mockito.when(countryService.getDemonymByCountryCode("US")).thenReturn("American");
        Mockito.doNothing().when(customerRepository).persist(any(Customer.class));

        Customer result = customerService.updateCustomer(customerId, updatedCustomer);

        assertNotNull(result);
        assertEquals("American", result.getDemonym());
        assertEquals("new@example.com", result.getEmail());
        Mockito.verify(customerRepository).persist(any(Customer.class));
    }

    // Tests successful deletion of an existing customer
    @Test
    void testDeleteExistingCustomerSuccess() {
        Long customerId = 1L;
        Mockito.when(customerRepository.deleteById(customerId)).thenReturn(true);

        boolean result = customerService.deleteCustomer(customerId);

        assertTrue(result);
        Mockito.verify(customerRepository, Mockito.times(1)).deleteById(customerId);
    }

    // Simulates failure in fetching demonym from CountryService
    @Test
    void testCreateCustomerWithCountryServiceFailure() {
        Customer mockCustomer = new Customer();
        mockCustomer.setCountry("US");
        Mockito.when(countryService.getDemonymByCountryCode("US")).thenReturn(null); // Simulate failure

        Customer result = customerService.createCustomer(mockCustomer);

        assertNull(result.getDemonym()); // Assuming your logic handles the failure by not setting a demonym
    }

    // Verifies handling of invalid email during customer creation
    @Test
    void testCreateCustomerWithInvalidEmail() {
        Customer mockCustomer = new Customer();
        mockCustomer.setEmail("invalidEmail");
        // Assuming a method in your service that validates the email format
        // And it throws a custom ValidationException for invalid emails

        ValidationException exception = assertThrows(ValidationException.class, () -> customerService.createCustomer(mockCustomer));

        assertNotNull(exception);
        assertEquals("Invalid email format", exception.getMessage());
    }



}
