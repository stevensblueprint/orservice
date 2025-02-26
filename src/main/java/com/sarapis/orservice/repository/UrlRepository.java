package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.Url;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends JpaRepository<Url, String> {

  List<Url> findByOrganizationId(String organizationId);

  List<Url> findByServiceId(String serviceId);

  Optional<Url> findByIdAndOrganizationId(String id, String organizationId);

  Optional<Url> findByIdAndServiceId(String id, String serviceId);

  boolean existsByUrl(String url);
}