package com.jk.apps.profiles.service.admin.impl.wfs.tasks;

import com.jk.apps.profiles.dto.ProductInfo;
import com.jk.apps.profiles.dto.admin.ProductMetaFieldSyncResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class IterateShopifyProductsTask {

    private final BatchCreateProductMetaFieldsTask batchCreateProductMetaFieldsTask;
    private final UpsertShopifyProductIdInDbTask upsertShopifyProductIdInDbTask;

    public List<ProductMetaFieldSyncResult> matchAndSync(List<ProductInfo> products) {
        List<ProductMetaFieldSyncResult> results = new ArrayList<>();
        for (ProductInfo product : products) {
            List<ProductMetaFieldSyncResult> batchResult = batchCreateProductMetaFieldsTask.execute(List.of(product));
            upsertShopifyProductIdInDbTask.persist(List.of(product));
            results.addAll(batchResult);
        }
        return results;
    }
}
