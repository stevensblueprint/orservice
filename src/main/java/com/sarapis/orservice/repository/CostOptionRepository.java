package com.sarapis.orservice.repository;

import com.sarapis.orservice.entity.CostOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CostOptionRepository extends JpaRepository<CostOption, String> {
}
