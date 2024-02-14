package com.freddyerf.customer.controller;

import com.freddyerf.customer.model.Customer;
import com.freddyerf.customer.service.CustomerService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerController {

    @Inject
    CustomerService customerService;

    @POST
    public Response createCustomer(Customer customer) {
        Customer createdCustomer = customerService.createCustomer(customer);
        return Response.ok(createdCustomer).build();
    }

    @GET
    public Response getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return Response.ok(customers).build();
    }

    @GET
    @Path("/{country}")
    public Response getCustomersByCountry(@PathParam("country") String country) {
        List<Customer> customers = customerService.getCustomersByCountry(country);
        return Response.ok(customers).build();
    }

    @GET
    @Path("/{id}")
    public Response getCustomersById(@PathParam("id") Long id) {
        Customer customer = customerService.getCustomerById(id);
        return Response.ok(customer).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") Long id, Customer customer) {
        Customer updatedCustomer = customerService.updateCustomer(id, customer);
        if (updatedCustomer != null) {
            return Response.ok(updatedCustomer).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") Long id) {
        if (customerService.deleteCustomer(id)) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}

