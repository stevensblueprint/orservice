package com.sarapis.orservice.repository;

import com.sarapis.orservice.entity.core.ServiceAtLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceAtLocationRepository extends JpaRepository<ServiceAtLocation, Long> {
}