package com.jk.apps.profiles.repository;

import com.jk.apps.profiles.entity.CustomerProfileBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerProfileBatchRepository extends JpaRepository<CustomerProfileBatch, String> {
}
