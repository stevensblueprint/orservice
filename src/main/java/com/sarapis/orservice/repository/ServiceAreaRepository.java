package com.sarapis.orservice.repository;

import com.sarapis.orservice.model.ServiceArea;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceAreaRepository extends JpaRepository<ServiceArea, String> {
  List<ServiceArea> findServiceAreaByServiceId(String serviceId);
}
