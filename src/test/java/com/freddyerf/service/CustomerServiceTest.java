package com.freddyerf.service;

import com.freddyerf.customer.model.Customer;
import com.freddyerf.customer.repository.CustomerRepository;
import com.freddyerf.customer.service.CountryService;
import com.freddyerf.customer.service.CustomerService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
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

    private Customer createValidCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");
        customer.setAddress("123 Main St");
        customer.setPhone("555-1234");
        customer.setCountry("US"); // Ensure this is valid and expected by the test
        return customer;
    }


    // Tests retrieving all customers successfully
    @Test
    void testGetAllCustomers() {
        List<Customer> customers = List.of(createValidCustomer(), createValidCustomer());
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
        Customer mockCustomer = createValidCustomer();
        Mockito.when(countryService.getDemonymByCountryCode("US")).thenReturn("American");
        Mockito.doNothing().when(customerRepository).persist(Mockito.any(Customer.class));

        Customer result = customerService.createCustomer(mockCustomer);

        Mockito.verify(customerRepository, Mockito.times(1)).persist(mockCustomer);
        assertEquals("American", result.getDemonym());
    }

    // Tests customer creation with an invalid country code
    @Test
    void testCreateCustomerInvalidCountry() {
        Customer mockCustomer = createValidCustomer();
        mockCustomer.setCountry("InvalidCode");
        Mockito.when(countryService.getDemonymByCountryCode("InvalidCode")).thenReturn(null);

        assertThrows(ConstraintViolationException.class, () -> customerService.createCustomer(mockCustomer));
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
        Customer existingCustomer = createValidCustomer();
        existingCustomer.setId(customerId);
        Customer updatedCustomer = createValidCustomer();
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
        Customer mockCustomer = createValidCustomer();
        Mockito.when(countryService.getDemonymByCountryCode("US")).thenReturn(null); // Simulate failure

        assertThrows(ConstraintViolationException.class, () -> customerService.createCustomer(mockCustomer));
    }

    // Verifies handling of invalid email during customer creation
    @Test
    void testCreateCustomerWithInvalidEmail() {
        Customer mockCustomer = createValidCustomer();
        mockCustomer.setEmail("invalidEmail");

        ValidationException exception = assertThrows(ValidationException.class, () -> customerService.createCustomer(mockCustomer));

        assertNotNull(exception);
    }



}
