package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeRepository extends JpaRepository<Exchange, String> {
    List<Exchange> findByUserId(String userId);
}
