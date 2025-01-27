package com.sarapis.orservice.repository;

import com.sarapis.orservice.entity.core.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, String> {
    @Query(value = "SELECT * FROM service WHERE to_tsvector(name || ' ' || coalesce(description, '')) @@ to_tsquery(?1) OR ?1 IS NULL", nativeQuery = true)
    List<Service> getAllServices(String search);
}
