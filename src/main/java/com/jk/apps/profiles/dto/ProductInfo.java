package com.jk.apps.profiles.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfo {

    private String title;
    private String shopifyProductId;

    // Example metafield-related fields
    private String licenseType;
    private String platform;
    private String version;
    private boolean supportIncluded;
}
