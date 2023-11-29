package com.ecommerce.shopifyorderretrieval.service;

import com.ecommerce.shopifyorderretrieval.configuration.ShopifyApiConfig;
import com.ecommerce.shopifyorderretrieval.exception.UnknownDateFilterException;
import com.ecommerce.shopifyorderretrieval.model.Order;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.time.DayOfWeek;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ShopifyService {

    @Autowired
    private ShopifyApiConfig apiConfig;

    @Autowired
    private RestTemplate restTemplate;

    public List<Order> getOrdersByFilter(String dateFilter) {
        try {
            // Construct the Shopify API URL with the provided dateFilter
            String apiUrl = buildApiUrl(dateFilter);
            log.info("Calling Shopify API with {}", apiUrl);

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

                log.info("Received Shopify API response: {}", jsonResponse);

                // Parse the JSON response into a list of Order objects
                return parseJsonResponse(jsonResponse);
            } else {
                log.error("Failed to retrieve orders from Shopify API. Status code: {}", responseEntity.getStatusCode());
            }

        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.warn("Shopify API returned 404 Not Found for date filter: {}", dateFilter);
                throw new UnknownDateFilterException("No orders found for the specified date filter: " + dateFilter);
            } else {
                log.error("Error while calling Shopify API", ex);
            }
        } catch (Exception e) {
            log.error("Error while fetching orders from Shopify API", e);
        }

        // Return an empty list if there's an error
        return new ArrayList<>();
    }

    private String buildApiUrl(String dateFilter) {
        String baseUrl = apiConfig.getBaseUrl();

        switch (dateFilter.toLowerCase()) {
            case "today":
                return baseUrl + YearMonth.now() + "/orders.json";
            case "yesterday":
                YearMonth lastMonth = YearMonth.now().minusMonths(1);
                return baseUrl + lastMonth + "/orders.json";
            case "last 7 days":
                YearMonth sevenMonthsAgo = YearMonth.now().minusMonths(6);
                return baseUrl + sevenMonthsAgo + "/orders.json";
            case "this week":
                YearMonth startOfWeek = YearMonth.from(YearMonth.now().atDay(1).with(DayOfWeek.MONDAY));
                return baseUrl + startOfWeek + "/orders.json";
            case "this month":
                return baseUrl + YearMonth.now() + "/orders.json";
            case "last 30 days":
                YearMonth thirtyMonthsAgo = YearMonth.now().minusMonths(29);
                return baseUrl + thirtyMonthsAgo + "/orders.json";
            case "last month":
                YearMonth startOfLastMonth = YearMonth.from(YearMonth.now().minusMonths(1).atDay(1));
                return baseUrl + startOfLastMonth + "/orders.json";
            case "custom date":
                // Handle custom date logic
                return baseUrl + dateFilter + "/orders.json";
            default:
                log.warn("Unknown date filter: {} for the order retrieval", dateFilter);
                throw new UnknownDateFilterException("Unknown date filter: " + dateFilter + " for order retrieval");
        }
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
