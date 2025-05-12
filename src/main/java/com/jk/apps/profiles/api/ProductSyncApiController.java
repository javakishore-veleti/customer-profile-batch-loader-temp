package com.jk.apps.profiles.api;

import com.jk.apps.profiles.service.data_sync.DataSyncProductSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sync")
public class ProductSyncApiController {

    @Autowired
    private DataSyncProductSvc dataSyncProductSvc;

    @GetMapping("/products")
    public ResponseEntity<String> syncProductsToShopify() {
        dataSyncProductSvc.syncProductsFromDataset();
        return ResponseEntity.ok("Product sync initiated from local dataset to Shopify");
    }
}