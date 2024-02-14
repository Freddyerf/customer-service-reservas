package com.freddyerf.service;

import com.freddyerf.customer.model.Customer;
import com.freddyerf.customer.repository.CustomerRepository;
import com.freddyerf.customer.service.CountryService;
import com.freddyerf.customer.service.CustomerService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
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


    @Test
    void testGetAllCustomers() {
        List<Customer> customers = List.of(new Customer(), new Customer());
        Mockito.when(customerRepository.listAll()).thenReturn(customers);

        List<Customer> result = customerService.getAllCustomers();

        assertEquals(2, result.size());
    }

    @Test
    void testGetCustomerByIdNotFound() {
        Mockito.when(customerRepository.findById(1L)).thenReturn(null);

        Customer result = customerService.getCustomerById(1L);

        assertNull(result);
    }


    @Test
    void testCreateCustomerSuccess() {
        Customer mockCustomer = new Customer();
        mockCustomer.setCountry("US");
        Mockito.when(countryService.getDemonymByCountryCode("US")).thenReturn("American");

        customerService.createCustomer(mockCustomer);

        Mockito.verify(customerRepository, Mockito.times(1)).persist(mockCustomer);
        assertEquals("American", mockCustomer.getDemonym());
    }


    @Test
    void testCreateCustomerInvalidCountry() {
        Customer mockCustomer = new Customer();
        mockCustomer.setCountry("InvalidCode");
        Mockito.when(countryService.getDemonymByCountryCode("InvalidCode")).thenReturn(null);

        Customer result = customerService.createCustomer(mockCustomer);

        assertNull(result.getDemonym());
    }

    @Test
    void testUpdateNonExistentCustomer() {
        Mockito.when(customerRepository.findById(1L)).thenReturn(null);

        Customer updatedCustomer = customerService.updateCustomer(1L, new Customer());

        assertNull(updatedCustomer);
    }

    @Test
    void testDeleteNonExistentCustomer() {
        Mockito.when(customerRepository.deleteById(1L)).thenReturn(false);

        boolean result = customerService.deleteCustomer(1L);

        assertFalse(result);
    }



}
