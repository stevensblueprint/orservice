package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.FileImport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileImportRepository extends JpaRepository<FileImport, String> {
}
