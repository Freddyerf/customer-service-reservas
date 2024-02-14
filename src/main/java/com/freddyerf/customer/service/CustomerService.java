package com.freddyerf.customer.service;

import com.freddyerf.customer.model.Customer;
import com.freddyerf.customer.repository.CustomerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class CustomerService {

    @Inject
    CustomerRepository customerRepository;

    @Inject
    private CountryService countryService;

    @Transactional
    public Customer createCustomer(Customer customer) {
        String demonym = countryService.getDemonymByCountryCode(customer.getCountry());
        customer.setDemonym(demonym);

        customerRepository.persist(customer);
        return customer;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.listAll();
    }

    public List<Customer> getCustomersByCountry(String country) {
        return customerRepository.findByCountry(country);
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    @Transactional
    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        Customer customer = customerRepository.findById(id);
        if (customer != null) {
            // Update the fields you allow to be updated
            customer.setEmail(updatedCustomer.getEmail());
            customer.setAddress(updatedCustomer.getAddress());
            customer.setPhone(updatedCustomer.getPhone());
            customer.setCountry(updatedCustomer.getCountry());
            // Persist the changes
            customerRepository.persist(customer);
            return customer;
        }
        return null; // or throw an exception
    }

    @Transactional
    public boolean deleteCustomer(Long id) {
        return customerRepository.deleteById(id);
    }
}

