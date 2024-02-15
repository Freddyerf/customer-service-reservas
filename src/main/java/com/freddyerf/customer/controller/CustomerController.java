package com.freddyerf.customer.controller;

import com.freddyerf.customer.model.Customer;
import com.freddyerf.customer.service.CustomerService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@OpenAPIDefinition(
        info = @Info(title = "Customer API", version = "1.0.0", description = "API for managing customers"),
        tags = {
                @Tag(name = "customer", description = "Operations related to customers")
        }
)
public class CustomerController {

    @Inject
    CustomerService customerService;

    @POST
    @Operation(summary = "Create a new customer", description = "Adds a new customer to the database.")
    @APIResponse(responseCode = "200", description = "Customer created",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Customer.class)))

    public Response createCustomer(Customer customer) {
        Customer createdCustomer = customerService.createCustomer(customer);
        return Response.ok(createdCustomer).build();
    }

    @GET
    @Operation(summary = "Get all customers", description = "Retrieves a list of all customers.")
    @APIResponse(responseCode = "200", description = "List of customers",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Customer.class)))

    public Response getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return Response.ok(customers).build();
    }

    @GET
    @Path("/{country}")
    @Operation(summary = "Get customers by country", description = "Retrieves a list of customers who belong to a specific country.")
    @APIResponse(responseCode = "200", description = "List of customers from the specified country",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Customer.class)))
    @Parameter(name = "country", description = "The ISO country code", example = "US")

    public Response getCustomersByCountry(@PathParam("country") String country) {
        List<Customer> customers = customerService.getCustomersByCountry(country);
        return Response.ok(customers).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get a customer by ID", description = "Retrieves a customer by their unique identifier.")
    @APIResponse(responseCode = "200", description = "Customer details",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Customer.class)))
    @Parameter(name = "id", description = "The unique identifier of the customer", example = "1")

    public Response getCustomersById(@PathParam("id") Long id) {
        Customer customer = customerService.getCustomerById(id);
        return Response.ok(customer).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update a customer", description = "Updates the details of an existing customer.")
    @APIResponse(responseCode = "200", description = "Updated customer details",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Customer.class)))
    @Parameter(name = "id", description = "The unique identifier of the customer to be updated", example = "1")

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
    @Operation(summary = "Delete a customer", description = "Deletes a customer from the database using their ID.")
    @APIResponse(responseCode = "204", description = "Customer deleted successfully")
    @Parameter(name = "id", description = "The unique identifier of the customer to be deleted", example = "1")

    public Response deleteCustomer(@PathParam("id") Long id) {
        if (customerService.deleteCustomer(id)) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}

