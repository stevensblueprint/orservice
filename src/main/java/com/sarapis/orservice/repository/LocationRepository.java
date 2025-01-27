package com.sarapis.orservice.repository;

import com.sarapis.orservice.entity.core.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, String> {
    @Query(value = "SELECT * FROM location WHERE to_tsvector(name || ' ' || description) @@ to_tsquery(?1) OR ?1 IS NULL", nativeQuery = true)
    List<Location> getAllLocations(String search);
}
