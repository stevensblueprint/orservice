package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.Location;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, String> {
  List<Location> findByOrganizationId(String organizationId);
}
