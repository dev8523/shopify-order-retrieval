package com.ecommerce.shopifyorderretrieval.service;

import com.ecommerce.shopifyorderretrieval.configuration.ShopifyApiConfig;
import com.ecommerce.shopifyorderretrieval.model.Order;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ShopifyService {

    @Autowired
    private ShopifyApiConfig apiConfig;

    @Autowired
    private RestTemplate restTemplate;

    public List<Order> getOrdersByFilter(String filter) {
        try {
            // Construct the Shopify API URL with the provided filter
            String apiUrl = apiConfig.getUrl() + "?filter=" + filter;

            // Prepare headers with API key and access token
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Shopify-Access-Token", apiConfig.getAccessToken());
            headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);

            // Prepare the request entity with headers
            RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(apiUrl));

            // Make the REST call
            ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                String jsonResponse = responseEntity.getBody();

                // Log the JSON response for debugging purposes
                log.info("Received Shopify API response: {}", jsonResponse);

                // Parse the JSON response into a list of Order objects
                List<Order> orders = parseJsonResponse(jsonResponse);

                log.info("Retrieved {} orders from Shopify API for filter '{}'", orders.size(), filter);

                return orders;
            } else {
                log.error("Failed to retrieve orders from Shopify API. Status code: {}", responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error while fetching orders from Shopify API", e);
        }

        // Return an empty list if there's an error
        return new ArrayList<>();
    }

    private List<Order> parseJsonResponse(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // Mark Unknown Properties as Ignorable
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            JsonNode ordersNode = rootNode.get("orders");
            if (ordersNode != null && ordersNode.isArray()) {
                List<Order> orders = new ArrayList<>();

                for (JsonNode orderNode : ordersNode) {
                    Order order = objectMapper.treeToValue(orderNode, Order.class);
                    orders.add(order);
                }

                return orders;
            } else {
                log.warn("No 'orders' node found in Shopify API response.");
            }
        } catch (IOException e) {
            log.error("Error parsing Shopify API JSON response", e);
        }

        // Return an empty list if there's an error
        return new ArrayList<>();
    }
}
