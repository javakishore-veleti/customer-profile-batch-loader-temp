package com.jk.apps.profiles.api;
import com.jk.apps.profiles.service.DataLoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/data-load")
public class DataLoadController {

    @Autowired
    private DataLoadService dataLoadService;

    @GetMapping
    public String triggerDataLoad(@RequestParam String folderPath,
                                  @RequestParam(defaultValue = "1") int dataSyncPurpose) {
        dataLoadService.loadData(folderPath, dataSyncPurpose);
        return "Data load triggered";
    }
}
