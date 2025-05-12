package com.jk.apps.profiles.service.admin.impl;

import com.jk.apps.profiles.dto.ProductInfo;
import com.jk.apps.profiles.service.admin.MetaFieldsSvc;
import com.jk.apps.profiles.service.admin.impl.wfs.tasks.GetShopifyProductsTask;
import com.jk.apps.profiles.service.admin.impl.wfs.tasks.IterateShopifyProductsTask;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MetaFieldsSvcImpl implements MetaFieldsSvc {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetaFieldsSvcImpl.class);

    private final GetShopifyProductsTask getShopifyProductsTask;
    private final IterateShopifyProductsTask iterateShopifyProductsTask;



    @Override
    public void syncMetaFieldsForAllProducts() {

        List<ProductInfo> products = getShopifyProductsTask.fetch();
        iterateShopifyProductsTask.matchAndSync(products);

    }
}
