package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.RequiredDocument;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequiredDocumentRepository extends JpaRepository<RequiredDocument, String> {
  List<RequiredDocument> findRequiredDocumentByServiceId(String serviceId);
}
