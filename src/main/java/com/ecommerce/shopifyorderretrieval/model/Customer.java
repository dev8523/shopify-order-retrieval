package com.ecommerce.shopifyorderretrieval.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Customer {
    @JsonProperty("first_name")
    private String customerFirstName;

    @JsonProperty("last_name")
    private String customerLastName;

    @JsonProperty("phone")
    private String customerPhone;

    @JsonProperty("email")
    private String customerEmail;
}
