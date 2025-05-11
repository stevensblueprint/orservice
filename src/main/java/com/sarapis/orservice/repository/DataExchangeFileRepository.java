package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.DataExchangeFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataExchangeFileRepository extends JpaRepository<DataExchangeFile, String> {
}
