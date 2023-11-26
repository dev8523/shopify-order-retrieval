package com.ecommerce.shopifyorderretrieval.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "shopify.api")
public class ShopifyApiConfig {
    private String url;
    private String apiKey;
    private String apiSecret;
    private String accessToken;
}
