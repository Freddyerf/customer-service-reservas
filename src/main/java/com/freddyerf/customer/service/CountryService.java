package com.freddyerf.customer.service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
@ApplicationScoped
public class CountryService {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private static final String API_URL = "https://restcountries.com/v3.1/alpha/";
    private static final String API_FIELDS = "demonyms"; // Replace with your API key


    public String getDemonymByCountryCode(String countryCode) {
        try {
            String url = API_URL + countryCode + "?fields=" + API_FIELDS;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Gson gson = new Gson();
                JsonElement element = gson.fromJson(response.body(), JsonElement.class);
                String maleDemonym = element.getAsJsonObject()
                        .get("demonyms")
                        .getAsJsonObject()
                        .get("eng")
                        .getAsJsonObject()
                        .get("m")
                        .getAsString();

                return maleDemonym;
            } else {
                System.err.println("Error fetching demonym: " + response.statusCode());
            }
            return null; // Demonym not found
        }
        catch (MalformedURLException | InterruptedException e) {
            System.err.println("Invalid URL: " + e.getMessage());
            return null;
        }
        catch (IOException e) {
            System.err.println("Error during HTTP request: " + e.getMessage());
            return null;
        }
        catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
