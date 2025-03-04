package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.Funding;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundingRepository extends JpaRepository<Funding, String> {
  List<Funding> findByOrganizationId(String organizationId);
  List<Funding> findByServiceId(String serviceId);
}
