package com.ecommerce.shopifyorderretrieval.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {
    private Long id;
    private String orderNo;
    private String orderDate;
    private String customerName;
    private String customerMobile;
    private String customerEmail;
    private String customerCity;
    private String customerState;
    private String customerPincode;
    private String customerAddress;
    private String dispatchDate;
    private String currentStatus;
    private String deliveryStatus;
    private double orderAmount;
    private int orderQuantity;
    private String orderType;
}
