package com.sarapis.orservice.repository;

import com.sarapis.orservice.entity.Metadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetadataRepository extends JpaRepository<Metadata, String> {
}
