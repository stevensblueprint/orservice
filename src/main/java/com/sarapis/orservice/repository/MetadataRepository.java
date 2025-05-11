package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.Metadata;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetadataRepository extends JpaRepository<Metadata, String> {
  List<Metadata> findByResourceIdAndResourceType(String resourceId, String resourceType);
  List<Metadata> findByDataExchangeFileIdAndResourceType(String dataExchangeFileId, String resourceType);
}
