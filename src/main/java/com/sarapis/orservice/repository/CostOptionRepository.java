package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.CostOption;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CostOptionRepository extends JpaRepository<CostOption, String> {
  List<CostOption> findByServiceId(String serviceId);
}
