package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.OrganizationIdentifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationIdentifierRepository extends JpaRepository<OrganizationIdentifier, String> {
}
