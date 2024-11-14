package com.sarapis.orservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceAtLocationRepository extends JpaRepository<ServiceAtLocationRepository, Long> {
}
