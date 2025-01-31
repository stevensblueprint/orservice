package com.sarapis.orservice.repository;

import com.sarapis.orservice.entity.core.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, String> {
}
