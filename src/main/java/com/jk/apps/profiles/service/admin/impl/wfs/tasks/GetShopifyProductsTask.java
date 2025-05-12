package com.jk.apps.profiles.service.admin.impl.wfs.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jk.apps.profiles.dto.ProductInfo;
import com.jk.apps.profiles.handler.shopify.GraphQLTemplateSvcImpl;
import com.jk.apps.profiles.handler.shopify.ShopifyGraphQLHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GetShopifyProductsTask {

    private final ShopifyGraphQLHandler graphqlHandler;
    private final GraphQLTemplateSvcImpl templateService;
    private ObjectMapper objectMapper = new ObjectMapper();

    public List<ProductInfo> fetch() {
        List<ProductInfo> results = new ArrayList<>();
        String cursor = null;
        boolean hasNextPage;

        do {
            String query = templateService.render(
                    "product-list-paginated.gtpl",
                    Map.of("cursor", cursor)
            );

            String response = graphqlHandler.executeMutation(query);
            JsonNode root;
            try {
                root = objectMapper.readTree(response);
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse Shopify response", e);
            }

            JsonNode edges = root.at("/data/products/edges");
            for (JsonNode edge : edges) {
                String gid = edge.at("/node/id").asText();
                String title = edge.at("/node/title").asText();
                ProductInfo p = new ProductInfo();
                p.setTitle(title);
                p.setShopifyProductId(gid);
                results.add(p);
            }

            hasNextPage = root.at("/data/products/pageInfo/hasNextPage").asBoolean();
            JsonNode lastCursorNode = edges.get(edges.size() - 1).get("cursor");
            cursor = lastCursorNode != null ? lastCursorNode.asText() : null;

        } while (hasNextPage && cursor != null);

        return results;
    }
}
