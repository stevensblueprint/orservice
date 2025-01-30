package com.sarapis.orservice.repository;

import com.sarapis.orservice.entity.OrganizationIdentifier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationIdentifierRepository extends JpaRepository<OrganizationIdentifier, String> {
}
