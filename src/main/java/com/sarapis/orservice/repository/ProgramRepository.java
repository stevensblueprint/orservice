package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.Program;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramRepository extends JpaRepository<Program, String> {
  List<Program> findByOrganizationId(String organizationId);
}
