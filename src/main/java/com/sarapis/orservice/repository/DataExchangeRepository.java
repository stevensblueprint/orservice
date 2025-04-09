package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.DataExchange;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface DataExchangeRepository extends JpaRepository<DataExchange, String> {
    @Query(value = "SELECT * FROM data_exchange WHERE user_id = ?1 AND timestamp BETWEEN coalesce(?2, '-infinity'::timestamp) AND coalesce(?3, 'infinity'::timestamp)",
            countQuery = "SELECT COUNT(*) FROM data_exchange WHERE user_id = ?1 AND timestamp BETWEEN coalesce(?2, '-infinity'::timestamp) AND coalesce(?3, 'infinity'::timestamp)",
            nativeQuery = true)
    Page<DataExchange> findDataExchanges(String userId, LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable);
}
