package com.sarapis.orservice.repository;

import com.sarapis.orservice.entity.core.ServiceAtLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceAtLocationRepository extends JpaRepository<ServiceAtLocation, String> {
    String query =
            "SELECT * " +
            "FROM service_at_location " +
            "WHERE EXISTS (SELECT * FROM service WHERE service.id = service_at_location.service_id AND to_tsvector(name || ' ' || coalesce(description, '')) @@ to_tsquery(?1)OR ?1 IS NULL) " +
            "  AND EXISTS (SELECT * FROM location WHERE location.id = service_at_location.location_id AND to_tsvector(coalesce(name, '') || ' ' || coalesce(description, '')) @@ to_tsquery(?2) OR ?2 IS NULL)";

    @Query(value = query, nativeQuery = true)
    List<ServiceAtLocation> getAllServiceAtLocations(String serviceQuery, String locationQuery);
}
