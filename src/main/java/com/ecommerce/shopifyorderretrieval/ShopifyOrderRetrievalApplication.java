package com.ecommerce.shopifyorderretrieval;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.ecommerce.shopifyorderretrieval")
public class ShopifyOrderRetrievalApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopifyOrderRetrievalApplication.class, args);
    }

}

