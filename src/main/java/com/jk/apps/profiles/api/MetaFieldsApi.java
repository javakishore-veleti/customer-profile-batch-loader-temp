package com.jk.apps.profiles.api;

import com.jk.apps.profiles.service.admin.MetaFieldsSvc;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shopify/metafields")
@RequiredArgsConstructor
public class MetaFieldsApi {

    private final MetaFieldsSvc metaFieldsSvc;

    @PostMapping("/sync")
    public ResponseEntity<String> createMetafields() {
        metaFieldsSvc.syncMetaFieldsForAllProducts();
        return ResponseEntity.ok("Completed");
    }
}
