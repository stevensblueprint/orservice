package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.Url;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends JpaRepository<Url, String> {
  List<Url> findByOrganizationId(String organizationId);
}