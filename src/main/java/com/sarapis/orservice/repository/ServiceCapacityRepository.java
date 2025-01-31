package com.sarapis.orservice.repository;

import com.sarapis.orservice.entity.ServiceCapacity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceCapacityRepository extends JpaRepository<ServiceCapacity, String> {
}
