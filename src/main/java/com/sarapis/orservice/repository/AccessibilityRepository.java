package com.sarapis.orservice.repository;

import com.sarapis.orservice.entity.Accessibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessibilityRepository extends JpaRepository<Accessibility, String> {
}
