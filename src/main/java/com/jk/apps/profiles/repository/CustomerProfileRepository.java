package com.jk.apps.profiles.repository;

import com.jk.apps.profiles.entity.CustomerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, String> {
    List<CustomerProfile> findByProfileIdIn(List<String> profileIds);
}
