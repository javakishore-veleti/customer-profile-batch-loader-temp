package com.jk.apps.profiles.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_profile_batch")
@Data
public class CustomerProfileBatch {

    @Id
    private String batchId;

    private LocalDateTime createdDateTime;

    private int batchNumber;

    private int profileIdsCount;

    private String dataSyncStatus;

    private int dataSyncPurpose; // 1 = initial_load, 2 = daily_load

    private LocalDateTime batchRequestDateTime;

    // Getters and Setters


    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public int getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(int batchNumber) {
        this.batchNumber = batchNumber;
    }

    public int getProfileIdsCount() {
        return profileIdsCount;
    }

    public void setProfileIdsCount(int profileIdsCount) {
        this.profileIdsCount = profileIdsCount;
    }

    public String getDataSyncStatus() {
        return dataSyncStatus;
    }

    public void setDataSyncStatus(String dataSyncStatus) {
        this.dataSyncStatus = dataSyncStatus;
    }

    public int getDataSyncPurpose() {
        return dataSyncPurpose;
    }

    public void setDataSyncPurpose(int dataSyncPurpose) {
        this.dataSyncPurpose = dataSyncPurpose;
    }

    public LocalDateTime getBatchRequestDateTime() {
        return batchRequestDateTime;
    }

    public void setBatchRequestDateTime(LocalDateTime batchRequestDateTime) {
        this.batchRequestDateTime = batchRequestDateTime;
    }
}