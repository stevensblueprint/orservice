package com.sarapis.orservice.repository;

import com.sarapis.orservice.entity.Funding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundingRepository extends JpaRepository<Funding, String> {
}
