package com.jk.apps.profiles.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "shopify.batch")
public class ShopifyBatchConfig {
    private int productSize;
    private int customerSize;
    private int orderSize;
    private int customerProfileSize;

    // Getters and Setters

    public int getProductSize() {
        return productSize;
    }

    public void setProductSize(int productSize) {
        this.productSize = productSize;
    }

    public int getCustomerSize() {
        return customerSize;
    }

    public void setCustomerSize(int customerSize) {
        this.customerSize = customerSize;
    }

    public int getOrderSize() {
        return orderSize;
    }

    public void setOrderSize(int orderSize) {
        this.orderSize = orderSize;
    }

    public int getCustomerProfileSize() {
        return customerProfileSize;
    }

    public void setCustomerProfileSize(int customerProfileSize) {
        this.customerProfileSize = customerProfileSize;
    }
}