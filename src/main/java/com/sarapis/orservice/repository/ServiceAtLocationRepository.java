package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.ServiceAtLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceAtLocationRepository extends JpaRepository<ServiceAtLocation, String>,
    JpaSpecificationExecutor<ServiceAtLocation> {
}
