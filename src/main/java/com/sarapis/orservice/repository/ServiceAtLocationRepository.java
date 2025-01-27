package com.sarapis.orservice.repository;

import com.sarapis.orservice.entity.core.ServiceAtLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceAtLocationRepository extends JpaRepository<ServiceAtLocation, String> {
    @Query(value = "SELECT * FROM service_at_location WHERE to_tsvector(description) @@ to_tsquery(?1) OR ?1 IS NULL", nativeQuery = true)
    List<ServiceAtLocation> getAllServiceAtLocations(String search);
}
