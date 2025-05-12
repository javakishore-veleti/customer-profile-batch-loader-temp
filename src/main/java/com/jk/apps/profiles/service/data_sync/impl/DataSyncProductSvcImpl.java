package com.jk.apps.profiles.service.data_sync.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jk.apps.profiles.dto.ShopifyBatchConfig;
import com.jk.apps.profiles.handler.shopify.GraphQLTemplateSvcImpl;
import com.jk.apps.profiles.handler.shopify.ShopifyGraphQLHandler;
import com.jk.apps.profiles.service.data_sync.DataSyncProductSvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.*;

@SuppressWarnings("ALL")
@Service
public class DataSyncProductSvcImpl implements DataSyncProductSvc {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSyncProductSvcImpl.class);

    @Autowired
    private ShopifyBatchConfig batchConfig;

    @Autowired
    private GraphQLTemplateSvcImpl templateService;

    @Autowired
    private ShopifyGraphQLHandler graphQLHandler;

    private static final String DATASET_FOLDER = "shopify-dataset/shopify-products";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void syncProductsFromDataset() {
        try {
            Path folderPath = Paths.get(DATASET_FOLDER);
            if (!Files.exists(folderPath)) {
                throw new RuntimeException("Dataset folder does not exist: " + folderPath.toAbsolutePath());
            }

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(folderPath, "*.json")) {
                for (Path filePath : stream) {
                    List<Map<String, Object>> allProducts = readJsonFile(filePath);
                    syncProductsInBatches(allProducts);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read product dataset", e);
        }
    }


    private List<Map<String, Object>> readJsonFile(Path filePath) throws IOException {
        try (InputStream is = Files.newInputStream(filePath)) {
            return objectMapper.readValue(is, new TypeReference<>() {});
        }
    }

    private void syncProductsInBatches(List<Map<String, Object>> products) {
        int batchSize = batchConfig.getProductSize();
        List<List<Map<String, Object>>> batches = partition(products, batchSize);

        for (List<Map<String, Object>> batch : batches) {
            Map<String, Object> model = new HashMap<>();
            model.put("products", batch);

            String query = templateService.render("productBatchCreate.tpl", model);
            String response = graphQLHandler.executeMutation(query);
            LOGGER.info("Batch Response:\n{}", response);
        }
    }

    private List<List<Map<String, Object>>> partition(List<Map<String, Object>> list, int size) {
        List<List<Map<String, Object>>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            partitions.add(list.subList(i, Math.min(i + size, list.size())));
        }
        return partitions;
    }
}
