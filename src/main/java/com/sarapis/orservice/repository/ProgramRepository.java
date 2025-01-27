package com.sarapis.orservice.repository;

import com.sarapis.orservice.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramRepository extends JpaRepository<Program, String> {
}
