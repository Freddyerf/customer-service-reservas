package com.freddyerf.customer.repository;

import com.freddyerf.customer.model.Customer;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class CustomerRepository implements PanacheRepository<Customer> {

    // 1. Creation is handled by Panache's persist method implicitly

    // 2. Get all existing customers is handled by Panache

    // 3. Get all existing customers who belong to a specific country
    public List<Customer> findByCountry(String country) {
        return list("country", country);
    }

    // 4. Get a specific client by its identifier is handled by Panache

    // 5. Update is handled by Panache's entity management implicitly

    // 6. Delete a customer by their ID is handled by Panache

}
