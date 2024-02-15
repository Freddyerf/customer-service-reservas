package com.freddyerf.customer.service;

import com.freddyerf.customer.model.Customer;
import com.freddyerf.customer.repository.CustomerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

import java.util.List;
import java.util.Set;

@ApplicationScoped
public class CustomerService {

    @Inject
    CustomerRepository customerRepository;

    @Inject
    private CountryService countryService;

    @Inject
    Validator validator;



    /**
     * Creates and persists a new customer with a demonym based on their country code.
     *
     * @param customer The customer to be created.
     * @return The persisted customer with the demonym set.
     */
    @Transactional
    public Customer createCustomer(Customer customer) {
        String demonym = countryService.getDemonymByCountryCode(customer.getCountry());
        customer.setDemonym(demonym);

        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        if (!violations.isEmpty() || demonym == null) {
            throw new ConstraintViolationException(violations);
        }

        customerRepository.persist(customer);
        return customer;
    }

    /**
     * Retrieves all customers from the database.
     *
     * @return A list of all customers.
     */
    public List<Customer> getAllCustomers() {
        return customerRepository.listAll();
    }

    /**
     * Fetches customers by their country code.
     *
     * @param country The country code of the customers to retrieve.
     * @return A list of customers from the specified country.
     */
    public List<Customer> getCustomersByCountry(String country) {
        return customerRepository.findByCountry(country);
    }

    /**
     * Gets a customer by their unique ID.
     *
     * @param id The ID of the customer to find.
     * @return The found customer, or null if not found.
     */
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    /**
     * Updates an existing customer's details.
     *
     * @param id The ID of the customer to update.
     * @param updatedCustomer The new details for the customer.
     * @return The updated customer, or null if the customer does not exist.
     */
    @Transactional
    public Customer updateCustomer(Long id, Customer updatedCustomer) {

        Customer customer = customerRepository.findById(id);
        if (customer != null) {
            // Update the fields you allow to be updated
            customer.setEmail(updatedCustomer.getEmail());
            customer.setAddress(updatedCustomer.getAddress());
            customer.setPhone(updatedCustomer.getPhone());
            customer.setCountry(updatedCustomer.getCountry());
            String demonym = countryService.getDemonymByCountryCode(customer.getCountry());
            customer.setDemonym(demonym);

            // validate updated customer
            Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
            // Persist the changes
            customerRepository.persist(customer);
            return customer;
        }
        return null; // or throw an exception
    }

    /**
     * Deletes a customer by their ID.
     *
     * @param id The ID of the customer to delete.
     * @return true if the customer was successfully deleted, false otherwise.
     */
    @Transactional
    public boolean deleteCustomer(Long id) {
        return customerRepository.deleteById(id);
    }
}

