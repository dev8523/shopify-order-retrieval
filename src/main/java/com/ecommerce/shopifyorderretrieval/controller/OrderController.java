package com.ecommerce.shopifyorderretrieval.controller;

import com.ecommerce.shopifyorderretrieval.model.Order;
import com.ecommerce.shopifyorderretrieval.service.ExcelService;
import com.ecommerce.shopifyorderretrieval.service.ShopifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private ShopifyService shopifyService;

    @Autowired
    private ExcelService excelService;

    @GetMapping("/generateExcel")
    public ResponseEntity<String> generateExcel(@RequestParam String filter) {
        // Step-1: Fetch the order data from shopify API
        List<Order> orders = shopifyService.getOrdersByFilter(filter);

        // Step-2: Generate an excel based on the fetched order data from shopify API
        String filePath = "/Users/debasishsahoo/Documents/GitHub/shopify-order-retrieval/src/main/resources/excel/file.xlsx";
        excelService.generateExcel(orders, filePath);
        return ResponseEntity.ok("Excel file generated successfully");
    }
}
