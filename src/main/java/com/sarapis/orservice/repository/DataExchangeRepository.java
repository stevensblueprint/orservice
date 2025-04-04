package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.DataExchange;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataExchangeRepository extends JpaRepository<DataExchange, String> {
    Page<DataExchange> findByUserId(String userId, Pageable pageable);
}
