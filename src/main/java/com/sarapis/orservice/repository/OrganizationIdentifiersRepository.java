package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.OrganizationIdentifier;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationIdentifiersRepository extends JpaRepository<OrganizationIdentifier, String> {
  List<OrganizationIdentifier> findByOrganizationId(String organizationId);
}
