package com.jk.apps.profiles.service.impl;

import com.jk.apps.profiles.service.DataLoadService;
import com.jk.apps.profiles.entity.CustomerProfile;
import com.jk.apps.profiles.entity.CustomerProfileBatch;
import com.jk.apps.profiles.repository.CustomerProfileRepository;
import com.jk.apps.profiles.repository.CustomerProfileBatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class DataLoadServiceImpl implements DataLoadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoadServiceImpl.class);

    @Autowired
    private CustomerProfileRepository profileRepository;

    @Autowired
    private CustomerProfileBatchRepository batchRepository;

    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static final int DEFAULT_BATCH_SIZE = 10000;
    private static final int PROFILE_ID_LOOKUP_BATCH_SIZE = 100; // Configurable
    private static final int THREAD_POOL_SIZE = 8; // Configurable thread pool size

    @Override
    public void loadData(String folderPath, int dataSyncPurpose) {
        Path path = Paths.get(folderPath);
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            throw new IllegalArgumentException("Invalid folder path");
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, "*.csv")) {
            List<Future<?>> futures = new ArrayList<>();
            int threadCounter = 0;
            for (Path filePath : stream) {
                final int batchNumber = threadCounter++;
                futures.add(executor.submit(() -> processFile(filePath, batchNumber, dataSyncPurpose)));
            }
            // Wait for all tasks to complete
            for (Future<?> future : futures) {
                future.get();
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error processing folder", e);
        }
    }

    private void processFile(Path filePath, int batchNumber, int dataSyncPurpose) {
        try {
            List<String> allLines = Files.readAllLines(filePath);
            int totalLines = allLines.size();

            for (int i = 0; i < totalLines; i += DEFAULT_BATCH_SIZE) {
                List<String> batchLines = allLines.subList(i, Math.min(i + DEFAULT_BATCH_SIZE, totalLines));
                String batchId = UUID.randomUUID().toString();

                Set<String> existingIds = new HashSet<>();
                for (int j = 0; j < batchLines.size(); j += PROFILE_ID_LOOKUP_BATCH_SIZE) {
                    List<String> subList = batchLines.subList(j, Math.min(j + PROFILE_ID_LOOKUP_BATCH_SIZE, batchLines.size()));
                    LOGGER.info("Processing batch {} of {} profiles subList {}", batchId, subList.size(), subList);
                    List<CustomerProfile> partial = profileRepository.findByProfileIdIn(subList);
                    existingIds.addAll(partial.stream().map(CustomerProfile::getProfileId).collect(Collectors.toSet()));
                }

                List<CustomerProfile> newProfiles = batchLines.stream()
                        .filter(id -> !existingIds.contains(id))
                        .map(id -> {
                            CustomerProfile profile = new CustomerProfile();
                            profile.setProfileId(id);
                            profile.setBatchId(batchId);
                            profile.setDataSyncStatus("Pending");
                            profile.setCreatedDateTime(LocalDateTime.now());
                            return profile;
                        }).collect(Collectors.toList());

                if(!newProfiles.isEmpty()) {
                    CustomerProfileBatch batch = new CustomerProfileBatch();
                    batch.setBatchId(batchId);
                    batch.setBatchNumber(batchNumber);
                    batch.setCreatedDateTime(LocalDateTime.now());
                    batch.setProfileIdsCount(newProfiles.size());
                    batch.setDataSyncStatus("Pending");
                    batch.setDataSyncPurpose(dataSyncPurpose);
                    batch.setBatchRequestDateTime(LocalDateTime.now());
                    batchRepository.save(batch);
                    profileRepository.saveAll(newProfiles);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to process file: " + filePath, e);
        }
    }
}
