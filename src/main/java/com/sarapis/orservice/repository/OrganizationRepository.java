package com.sarapis.orservice.repository;

import com.sarapis.orservice.entity.core.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, String> {
    @Query(value = "SELECT * FROM organization WHERE to_tsvector(name || ' ' || description) @@ to_tsquery(?1) OR ?1 IS NULL", nativeQuery = true)
    List<Organization> getAllOrganizations(String search);
}
