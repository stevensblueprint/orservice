package com.sarapis.orservice.repository;

import com.sarapis.orservice.entity.RequiredDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequiredDocumentRepository extends JpaRepository<RequiredDocument, String> {
}
