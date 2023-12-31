package com.ecommerce.shopifyorderretrieval.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Order {
    @JsonProperty("order_number")
    private String orderNo;

    @JsonProperty("created_at")
    private String orderDate;

    @JsonProperty("customer")
    private Customer customer;
}
