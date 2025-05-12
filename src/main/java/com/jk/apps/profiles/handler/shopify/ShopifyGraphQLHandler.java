package com.jk.apps.profiles.handler.shopify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class ShopifyGraphQLHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShopifyGraphQLHandler.class);

    @Value("${shopify.store-url}")
    private String storeUrl;

    @Value("${shopify.access-token}")
    private String accessToken;

    private static final String GRAPHQL_ENDPOINT = "/admin/api/2025-04/graphql.json";

    public String executeMutation(String query) {
        HttpHeaders headers = new HttpHeaders();
        LOGGER.info("Executing mutation query: " + query);
        LOGGER.info("Authorization: Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Shopify-Access-Token", accessToken);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(Map.of("query", query), headers);
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.postForObject(storeUrl + GRAPHQL_ENDPOINT, entity, String.class);
    }
}
